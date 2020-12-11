/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
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
import mil.af.flagging.dataminer.model.EnvSequence;
import mil.af.flagging.dataminer.model.Signal;

/**
 *
 * @author Brad
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static final String MIN_MODE_INT_COUNT_KEY = "MIN_MODE_INT_COUNT";
    public static final int DEFAULT_MIN_MODE_INTS = 5;

    static int numRuns = 1;

//    private static CountDownLatch latch;

    public static final int MIN_MODE_INTS = Integer.getInteger(MIN_MODE_INT_COUNT_KEY, DEFAULT_MIN_MODE_INTS);

    public static void main(String[] args) throws Exception {
        if (args.length < 1 || args[0].toUpperCase(Locale.US).equals("HELP")) {
            usage();
            return;
        } else if (args.length > 1) {
            numRuns = Integer.parseInt(args[1]);
        }
        try {
            LOG.log(Level.INFO, "Data Miner starting");
            final Date startDateTime = Date.valueOf(LocalDate.now());
            LOG.log(Level.INFO, "creating data source");
            DataContext.ds = DataSourceFactory.createDataSourceFrom(DatabaseConfiguration.from(args[0]));
            LOG.log(Level.INFO, "creating daos");
            DataContext.dataMinerDAO = new DataMinerDAO();
            DataContext.modeMapDAO = new EnvModeMapDAO();
            DataContext.residueDAO = new ResidueInterceptDAO();

//            ExecutorService exec = Executors.newFixedThreadPool(4);

            try (Connection conn = DataContext.ds.getConnection()) {
                LOG.log(Level.INFO, "initializing sequences");
                // FIXME: this is a race condition
                DataContext.dataMinerDAO.initializeSequences(conn);

                LOG.log(Level.INFO, "fetching distinct residue notations");
                SortedSet<String> notationsToProcess = DataContext.residueDAO.fetchDistinctResidueNotations(conn);

                LOG.log(Level.INFO, "creating latch for {0} tasks", notationsToProcess.size());
//                latch = new CountDownLatch(notationsToProcess.size());

                LOG.log(Level.INFO, "iterating over notations");
                for (String notation : notationsToProcess) {
                    ModeGeneratorWorkUnit wu = new ModeGeneratorWorkUnit();
                    wu.baselineRuns = numRuns;
                    wu.notation = notation;
                    wu.minModeInts = MIN_MODE_INTS;
                    wu.tuning = DataContext.dataMinerDAO.fetchTuning(conn, notation);

                    DataContext.residueDAO.cleanResidueOLD(conn, notation);
                    LOG.log(Level.INFO, "calling fetch residue for {0}", notation);
                    wu.signals = DataContext.residueDAO.fetchResidue(conn, notation);
                    LOG.log(Level.INFO, "fetched {0} records for {1}", new Object[]{wu.signals.size(), wu.notation});
                    int cnt = 0;
                    for (Signal sig : wu.signals) {
                        if (sig.modeId < 0) {
                            cnt++;
                        }
                    }
                    if (cnt < wu.minModeInts) {
                        LOG.log(Level.INFO, "not enough residue for {0}", notation);
                        continue;
                    }

                    LOG.log(Level.INFO, "separating constants for {0}", notation);
                    wu.cwSignals = separateCW(wu.signals);
                    wu.envRangesPRI = DataContext.dataMinerDAO.fetchEnvRanges(conn, wu.notation, "PRI");
                    wu.envRangesJPR = DataContext.dataMinerDAO.fetchEnvRanges(conn, wu.notation, "JPR");
                    wu.envRangesRF = DataContext.dataMinerDAO.fetchEnvRanges(conn, wu.notation, "RF");
                    wu.envRangesPD = DataContext.dataMinerDAO.fetchEnvRanges(conn, wu.notation, "PD");
                    wu.envRangesSP = DataContext.dataMinerDAO.fetchEnvRanges(conn, wu.notation, "SP");
                    wu.envRangesIR = DataContext.dataMinerDAO.fetchEnvRanges(conn, wu.notation, "IR");

                    wu.envSequences = DataContext.modeMapDAO.buildENVSequences(conn, notation, wu.envRangesPRI, wu.envRangesJPR);
                    wu.envSequences.add(new EnvSequence(0L));
                    wu.envModeMaps = DataContext.modeMapDAO.buildEnvModeMaps(conn, notation, wu.envSequences);

                    if ((!wu.envRangesPRI.isEmpty() || !wu.envRangesJPR.isEmpty()) && wu.baselineRuns > 1) {
                        LOG.log(Level.SEVERE, "preserved environment exists for {0} and cannot use baseline building. please remove baseline building option.", notation);
                        return;
                    }

//                    wu.latch = latch;
                    ModeGenerator modeGen = new ModeGenerator(wu);
                    ModeGeneratorWorkUnit results = modeGen.call();

//                    Future<ModeGeneratorWorkUnit> x = exec.submit(modeGen);
//                    while (!x.isDone()) {
//                        // spin baby!
//                        Thread.sleep(100);
//                    }
//
                    storeNewModeMapsIfNecessary(conn, results);

                }
            } catch (Exception e) {
                LOG.log(Level.WARNING, "", e);
            }

            LOG.log(Level.INFO, "Data Miner complete");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            throw new RuntimeException(e);
        }
    }

    private static Collection<Signal> separateCW(Collection<Signal> signals) {
        Set<Signal> ret = new HashSet<>();
        signals.stream()
                .filter(signal -> signal.mt.equals("B")
                ).forEachOrdered(signal -> {
                    ret.add(signal);
                });
        // FIXME: side effect
        signals.removeAll(ret);
        return ret;
    }

    static void usage() {
        System.err.println("no command line options provided");
    }

    private static void storeNewModeMapsIfNecessary(Connection conn, ModeGeneratorWorkUnit wu) throws SQLException {
        for (EnvModeMap map : wu.envModeMaps) {
            if (map.stillWorthy(wu.minModeInts)) {
                if (map.id > 0) {
                    DataContext.modeMapDAO.fillMap(conn, map, wu.envRangesRF, wu.envRangesPD, wu.envRangesSP, wu.envRangesIR);
                }
                map.fill(wu.envRangesRF, wu.envRangesPD, wu.envRangesSP, wu.envRangesIR);
                if (!map.mappings.isEmpty() && map.stillWorthy(wu.minModeInts)) {
                    DataContext.dataMinerDAO.storeModeMap(conn, map, wu.notation, wu.minModeInts);
                }
            }
        }
    }
}
