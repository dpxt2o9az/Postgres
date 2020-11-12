/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.inputfilter;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import mil.af.flagging.db.DataSourceBuilder;
import org.zelmak.jdbc.schemaupdater.DatabaseSettings;

/**
 *
 * @author Brad
 */
public class MainApp implements Runnable {

    private static final Logger LOG = Logger.getLogger(MainApp.class.getName());
    private static final String INTERCEPT_QUERY
            = "select * "
            + "  from intercepts "
            + "    join idb_states "
            + "      using (intercept_id) "
            + "  where flow_control = 'new-data-proc' "
            + "  order by intercept_id";

    public static void main(String[] args) throws Exception {
        DatabaseSettings db = DatabaseSettings.fromPropertiesFile(new File(System.getProperty("user.home"), "projects/postgres/src/main/resources/cfg"));
        DataSource ds = new DataSourceBuilder(db.url, db.username, db.password).build();
        MainApp app = new MainApp(ds);
        app.run();
    }

//    private final DataSource ds;
    private final Connection conn;
    private final FilterConfiguration cfg;
    private final Acceptor acceptor;

    private final Converter converter = new Converter() {
        @Override
        public FilterIntercept convert(ResultSet rs) throws SQLException {
            FilterIntercept i = new FilterIntercept();
            i.interceptId = rs.getLong("intercept_id");
            i.wranglerId = rs.getString("wrangler_id");
            i.elnot = rs.getString("elnot");
            i.modType = rs.getString("mod_type");
            i.readoutStation = rs.getString("read_out_station");
            i.burstCount = rs.getInt("num_of_burst");
            return i;
        }

    };

    public MainApp(DataSource ds) throws SQLException {
        this.conn = ds.getConnection();
        this.cfg = FilterConfiguration.fromDatabase(conn);
        this.acceptor = randomAcceptor;
    }

    @Override
    public void run() {
        try {
            conn.setAutoCommit(false);

            markRecordsForProcessing();
            try (PreparedStatement ps = conn.prepareStatement(INTERCEPT_QUERY)) {

                try (ResultSet rs = ps.executeQuery()) {
                    processResultSet(rs);

                }
            }
            conn.commit();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "", e);
            throw new RuntimeException(e);
        }
    }

    private void markRecordsForProcessing() throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("update idb_states set flow_control = ? where flow_control = ?")) {
            ps.setString(1, "new-data-proc");
            ps.setString(2, "NEW-DATA");
            ps.executeUpdate();
        }
    }

    private void processResultSet(final ResultSet rs) throws SQLException {
        int counter = 0;
        while (rs.next()) {
            processResultSetRow(rs);
            counter++;
            if (counter % 5000 == 0) {
                System.out.println("processed " + counter);
            }
        }
    }

    private void processResultSetRow(final ResultSet rs) throws SQLException {
        FilterIntercept i = converter.convert(rs);
        if (meetsFilterCriteria(i)) {
            putIntoComparisonQueue(i);
        }
        removeFromFilterQueue(i);
    }

    private boolean meetsFilterCriteria(FilterIntercept i) {
        return acceptor.accepts(i);
    }

    private void putIntoComparisonQueue(FilterIntercept i) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("insert into comparison_queue ( intercept_id ) values ( ? )")) {
            ps.setLong(1, i.interceptId);
            ps.executeUpdate();
        }
    }

    private void removeFromFilterQueue(FilterIntercept i) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("delete from idb_states where intercept_id = ? and flow_control = ?")) {
            ps.setLong(1, i.interceptId);
            ps.setString(2, "new-data-proc");
            ps.executeUpdate();
        }
    }

    private final Acceptor randomAcceptor = new Acceptor() {
        @Override
        public boolean accepts(FilterIntercept i) {
            return Math.random() * 100 > 90;
        }

    };

    private final Acceptor realAcceptor = new Acceptor() {
        @Override
        public boolean accepts(FilterIntercept i) {
            return (i.burstCount > 4 || i.modType.equals("CC"))
                    && cfg.includedModulationTypes.contains(i.modType)
                    && cfg.includedReadoutStations.contains(i.readoutStation)
                    && !cfg.excludedElnots.contains(i.elnot);
        }
    };

    interface Acceptor {

        boolean accepts(FilterIntercept i);
    }

    interface Converter {

        FilterIntercept convert(ResultSet rs) throws SQLException;
    }

}
