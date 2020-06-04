
package mil.af.flagging.model;

import java.sql.*;

public class SchemaCreator {

    static String[] script = {
        "drop table if exists intercepts cascade ",
        "create table if not exists intercepts ( "
        + "  intercept_id serial, "
        + "  wrangler_id varchar(20) not null, "
        + "  elnot varchar(5) not null, "
        + "  CONSTRAINT unique_data UNIQUE (wrangler_id), "
        + "  CONSTRAINT intercept_pk PRIMARY KEY (intercept_id) "
        + ")",
        "alter sequence intercepts_intercept_id_seq restart 1024",
        "drop table if exists intercept_rfs",
        "create table if not exists intercept_rfs ( "
        + "  intercept_id int not null, "
        + "  sequence int not null, "
        + "  value numeric not null, "
        + "  CONSTRAINT intercept_rf_fk "
        + "    FOREIGN KEY (intercept_id) REFERENCES intercepts (intercept_id) "
        + "    ON DELETE CASCADE "
        + ")",
        "drop table if exists intercept_pris",
        "create table if not exists intercept_pris ( "
        + "  intercept_id int not null, "
        + "  sequence int not null, "
        + "  value numeric not null, "
        + "  CONSTRAINT intercept_pri_fk "
        + "    FOREIGN KEY (intercept_id) REFERENCES intercepts (intercept_id) "
        + "    ON DELETE CASCADE "
        + ")",
        "drop table if exists intercept_pds",
        "create table if not exists intercept_pds ( "
        + "  intercept_id int not null, "
        + "  sequence int not null, "
        + "  value numeric not null, "
        + "  CONSTRAINT intercept_pd_fk "
        + "    FOREIGN KEY (intercept_id) REFERENCES intercepts (intercept_id) "
        + "    ON DELETE CASCADE "
        + ")"
    };

    public static void dewIt(Connection c) throws SQLException {
        try (Statement s = c.createStatement()) {
            for (String scriptEntry : script) {
                s.executeUpdate(scriptEntry);
            }
        }
    }

}
