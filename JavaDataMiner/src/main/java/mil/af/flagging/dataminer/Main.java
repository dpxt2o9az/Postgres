/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import mil.af.flagging.dataminer.model.Signal;
import mil.af.flagging.dataminer.db.InterceptResidueDAO;
import mil.af.flagging.dataminer.db.DataMinerDAO;
import mil.af.flagging.dataminer.db.DataSourceFactory;
import mil.af.flagging.dataminer.db.DatabaseConfiguration;
import static mil.af.flagging.dataminer.DataContext.ds;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.af.flagging.dataminer.model.EnvModeMap;
import mil.af.flagging.dataminer.model.PriSequence;
import mil.af.flagging.dataminer.model.Parameter;

/**
 *
 * @author Brad
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static final String MIN_MODE_INT_COUNT_KEY = "MIN_MODE_INT_COUNT";
    public static final int DEFAULT_MIN_MODE_INTS = 5;

    static int numRuns = 1;
    private static CountDownLatch latch;

    private static final int MIN_MODE_INTS = Integer.parseInt(System.getProperty(MIN_MODE_INT_COUNT_KEY, String.valueOf(DEFAULT_MIN_MODE_INTS)));

    public static void main(String[] args) {
//        if (args.length < 1 || args[0].toUpperCase(Locale.US).equals("HELP")) {
//            usage();
//            return;
//        } else if (args.length > 1) {
//        numRuns = Integer.parseInt(args[1]);
//        }

        try {
            LOG.log(Level.INFO, "data miner start");

            final LocalDateTime startTime = LocalDateTime.now();

            LOG.log(Level.INFO, "creating data source");
            DataContext.ds = DataSourceFactory.createDataSource(DatabaseConfiguration.from("xxx"));

            LOG.log(Level.INFO, "creating DAOs");
            DataContext.dataMinerDAO = new DataMinerDAO();
            DataContext.residueDAO = new InterceptResidueDAO();

            ExecutorService exec = Executors.newFixedThreadPool(4);

            try (Connection conn = DataContext.ds.getConnection()) {

                LOG.log(Level.INFO, "initializing sequences");
                // FIXME: this is a race condition
                DataContext.dataMinerDAO.initializeSequences(conn);

                LOG.log(Level.INFO, "fetching distinct residue notations");
                SortedSet<String> notationsToProcess = DataContext.residueDAO.fetchDistinctResidueNotations(conn);

                LOG.log(Level.INFO, "creating countdown latch for {0} tasks", notationsToProcess.size());
                latch = new CountDownLatch(notationsToProcess.size());

                LOG.log(Level.INFO, "iterating over notations");
                for (String notation : notationsToProcess) {

                    ModeGeneratorWorkUnit wu = new ModeGeneratorWorkUnit();
                    wu.baselineRuns = numRuns;
                    wu.notation = notation;
                    wu.minModeInts = MIN_MODE_INTS;
                    wu.tuning = DataContext.dataMinerDAO.fetchTuning(conn, notation);

                    DataContext.residueDAO.cleanResidueOld(conn, notation);
                    // DataContext.residueDAO.cleanResidue(conn, notation);

                    LOG.log(Level.INFO, "calling fetchResidue for {0}", wu.notation);
                    wu.signals = DataContext.residueDAO.fetchResidue(conn, notation);

                    LOG.log(Level.INFO, "fetchResidue return {0} records for {1}", new Object[]{wu.signals.size(), wu.notation});

                    int cnt = 0;
                    for (Signal sig : wu.signals) {
                        if (sig.modeId < 0) {
                            cnt++;
                        }
                    }

                    if (cnt < wu.minModeInts) {
                        LOG.log(Level.INFO, "not enough residue for {0}", wu.minModeInts);
                        continue;
                    }

                    LOG.log(Level.INFO, "separatring constants for {0}", wu.notation);
                    wu.cwSignals = separateCW(wu.signals);
                    wu.envRanges = DataContext.dataMinerDAO.fetchEnvRangesForElnot(conn, wu.notation);

                    wu.envSequences = DataContext.modeDAO.buildEnvSequences(conn, wu.notation, wu.envRanges.get(Parameter.PRI), wu.envRanges.get(Parameter.JPR));
                    wu.envSequences.add(new PriSequence(0)); // why are we adding a "null" one?
                    wu.envModeMaps = DataContext.modeDAO.buildEnvModeMaps(conn, notation, wu.envSequences);

                    if (wu.baselineRuns > 1) {
                        if (!wu.envRanges.get(Parameter.PRI).isEmpty() || !wu.envRanges.get(Parameter.JPR).isEmpty()) {
                            LOG.log(Level.SEVERE, "Preserved environment exists for {0} and cannot use baseline builder mode. Please remove baseline runs argument from command-line or clear the environment of modes for {0}.", wu.notation);
                            return;
                        }
                    }

                    wu.latch = latch;

                    ModeGenerator modeGen = new ModeGenerator(wu);
                    Future<ModeGeneratorWorkUnit> x = exec.submit(modeGen);

                    while (!x.isDone()) {
                        // spin baby
                        Thread.sleep(100);
                    }

                    storeNewModeMapsIfNecessary(conn, x);
                }

                LocalDateTime finishTime = LocalDateTime.now();
                DataContext.statsDAO.storeTimeStampForDelta(conn, startTime, finishTime, "DATA MINER");
                LOG.log(Level.INFO, "data miner ending");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        LOG.log(Level.INFO, "data miner complete");
    }

    private static void processNotation(String notation, final Connection conn) throws SQLException {
        LOG.log(Level.INFO, "starting {0}", notation);

        Collection<Signal> signals = DataContext.residueDAO.fetchResidue(conn, notation);
        int counter = 0;
        for (Signal s : signals) {
            LOG.log(Level.FINEST, "processing id:{0}", s.id);
            counter++;
        }

        System.out.println("counted " + counter + " records");
    }

    private static void test() throws SQLException {
        try (Connection conn = ds.getConnection()) {
            try (Statement st = conn.createStatement()) {
                try (ResultSet rs = st.executeQuery("select * from intercept")) {

                    int counter = 0;
                    while (rs.next()) {
                        counter++;
                    }
                    System.out.println("counter: " + counter);
                }
            }
        }
    }

    private static void usage() {

    }

    private static Collection<Signal> separateCW(Collection<Signal> signals) {
        Set<Signal> ret = new HashSet<>();
        signals.stream()
                .filter((signal) -> (signal.mt.equals("B")))
                .forEachOrdered((signal) -> {
                    ret.add(signal);
                });
        signals.removeAll(ret);
        return ret;
    }

    private static void storeNewModeMapsIfNecessary(final Connection conn, final Future<ModeGeneratorWorkUnit> wuf) throws InterruptedException, ExecutionException, SQLException {
        ModeGeneratorWorkUnit wu = wuf.get();
        for (EnvModeMap map: wu.envModeMaps) {
            if (map.stillWorthy(wu.minModeInts)) {
                if (map.id > 0) {
                    DataContext.modeDAO.fillMap(conn, map, wu.envRanges);
                    map.fill(wu.envRanges);
                    if (!map.mappings.isEmpty() && map.stillWorthy(wu.minModeInts)) {
                        DataContext.dataMinerDAO.storeModeMap(conn, map, wu.notation, wu.minModeInts);
                    }
                }
            }
        }
    }
}
