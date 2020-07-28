// original C# from https://rianjs.net/2016/10/roll-forward-schema-updates-csharp
package org.zelmak.jdbc.schemaupdater;

import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.util.stream.Collectors;

public class SchemaUpdater {

    private static final Logger LOG = Logger.getLogger(SchemaUpdater.class.getName());

    private static final String DROP_ALL_TABLES
            = "DO $$ "
            + "DECLARE "
            + "    r record; "
            + "BEGIN "
            + "    FOR r IN SELECT quote_ident(tablename) AS tablename, quote_ident(schemaname) AS schemaname FROM pg_tables WHERE schemaname = 'public' "
            + "    loop "
            + "      BEGIN "
            + "        RAISE INFO 'Dropping table %.%', r.schemaname, r.tablename; "
            + "        EXECUTE format('DROP TABLE IF EXISTS %I.%I CASCADE', r.schemaname, r.tablename); "
            + "        exception "
            + "          when others then "
            + "            raise notice 'skipping %.%', quote_ident(r.schemaname), quote_ident(r.tablename); "
            + "          end; "
            + "    END LOOP; "
            + "end $$;";

    private static final String CREATE_AUDIT_TABLE
            = "CREATE TABLE IF NOT EXISTS SCHEMA_REVISIONS ("
            + "  revision_id SERIAL, "
            + "  file_name varchar(256) NOT NULL, "
            + "  file_contents TEXT NOT NULL,"
            + "  PRIMARY KEY ( revision_id )"
            + ")";
    
    private static final String INSERT_AUDIT_RECORD
            = "insert into schema_revisions "
            + "  ( revision_id, file_name, file_contents ) "
            + "  values "
            + "  ( DEFAULT, ?, ? )";
    
    private static final String FETCH_PREVIOUS_UPDATES
            = "SELECT File_name FROM Schema_Revisions order by REVISION_ID";

    private final Connection conn;
    private final String _schemaDirectory;

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: SchemaUpdater <cfg> <script-root>");
        } else {
            // first argument is the database connection properties
            DatabaseSettings dbCfg = DatabaseSettings.fromPropertiesFile(args[0]);
            Connection conn = DriverManager.getConnection(dbCfg.url, dbCfg.username, dbCfg.password);
            SchemaUpdater updater = new SchemaUpdater(conn, args[1]);
            updater.updateSchema();
        }

    }

    public SchemaUpdater(Connection conn, String schemaDirectory) {
        this.conn = conn;
        _schemaDirectory = schemaDirectory;
    }

    private List<File> enumerateSQLFilesIn(File directory) throws IOException {
        return Arrays.asList(directory.listFiles((File pathname) -> pathname.getName().toLowerCase().endsWith(".sql")));
    }

    public void updateSchema() throws SQLException, IOException {
        maybeCleanDatabase();
        maybeCreateAuditTable();
        Collection<String> previousUpdates = getPreviousSchemaUpdates();

        File schemaDirectory = new File(_schemaDirectory);
        List<File> schemaUpdates = enumerateSQLFilesIn(schemaDirectory);
        // remove from the list of files, those updates that have come before

        schemaUpdates = schemaUpdates.stream().filter(f -> !previousUpdates.contains(f.getName())).collect(Collectors.toList());

        try (PreparedStatement ps = conn.prepareStatement(INSERT_AUDIT_RECORD);
                Statement st = conn.createStatement()) {
            for (File update : schemaUpdates) {
                String text = Files.readAllLines(update.toPath()).stream().collect(Collectors.joining("\n"));
                for (String command : splitOnGo(text)) {
                    try {
                        LOG.log(Level.FINE, "attempting to apply command {0}", command);
                        st.executeUpdate(command);
                    } catch (SQLException e) {
                        LOG.log(Level.SEVERE, "unable to execute command {0}", command);
                        LOG.log(Level.SEVERE, "", e);
                        throw e;
                    }
                }
                // all the commands succeeded; mark as successful
                ps.setString(1, update.getName());
                ps.setString(2, text);
                ps.executeUpdate();
                LOG.log(Level.INFO, "applied {0}", new Object[]{update.getName()});
            }
        }
    }

    private void maybeCleanDatabase() throws SQLException {
        if (System.getProperty("DELETE.ALL.THE.TABLES") != null) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate(DROP_ALL_TABLES);
            }
        }
    }

    private void maybeCreateAuditTable() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(CREATE_AUDIT_TABLE);
        }
    }

    private Set<String> getPreviousSchemaUpdates() throws SQLException {
        try (Statement st = conn.createStatement()) {
            try (ResultSet rs = st.executeQuery(FETCH_PREVIOUS_UPDATES)) {
                Set<String> set = new HashSet<>();
                while (rs.next()) {
                    set.add(rs.getString(1));
                }
                return set;
            }
        }
    }

    static boolean isNullOrWhiteSpace(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static Collection<String> splitOnGo(String sqlScript) {
        // Split by "GO" statements
        Collection<String> statements = Arrays.asList(sqlScript.split(
                "^[\\t\\r\\n]*GO[\\t\\r\\n]*\\d*[\\t\\r\\n]*(?:--.*)?$"));
//                RegexOptions.Multiline
//                | RegexOptions.IgnorePatternWhitespace
//                | RegexOptions.IgnoreCase);

        // Remove empties, trim, and return
        Collection<String> materialized = statements.stream()
                .filter(x -> !isNullOrWhiteSpace(x))
                .map(x -> x.trim())
                .collect(Collectors.toList());

        return materialized;
    }

}
