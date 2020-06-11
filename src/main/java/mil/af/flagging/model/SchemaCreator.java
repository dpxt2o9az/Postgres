package mil.af.flagging.model;

import java.sql.*;

public class SchemaCreator {

    static String[] script = {
        "drop table if exists intercept cascade ",
        "create table if not exists intercept ( "
        + "  intercept_id serial, "
        + "  wrangler_id varchar(20) not null, "
        + "  elnot varchar(5) not null, "
        + "  mod_type varchar(2) not null, "
        + "  scan_type varchar(2) not null, "
        + "  scan_period numeric not null, "
        + "  time_process timestamp not null, "
        + "  int_up_time timestamp not null, "
        + "  int_down_time timestamp not null, "
        + "  latitude numeric not null, "
        + "  longitude numeric not null, "
        + "  major numeric not null, "
        + "  minor numeric not null, "
        + "  orientation numeric not null,"
        + "  CONSTRAINT unique_wrangler_id UNIQUE (wrangler_id), "
        + "  CONSTRAINT intercept_pk PRIMARY KEY (intercept_id) "
        + ")",
        "alter sequence intercept_intercept_id_seq restart 1024",
        "drop table if exists intercept_rfs",
        "create table if not exists intercept_rfs ( "
        + "  intercept_id int not null, "
        + "  sequence int not null, "
        + "  value numeric not null, "
        + "  PRIMARY KEY ( intercept_id, sequence ),"
        + "  CONSTRAINT intercept_rf_fk "
        + "    FOREIGN KEY (intercept_id) REFERENCES intercept (intercept_id) "
        + "    ON DELETE CASCADE "
        + ")",
        "drop table if exists intercept_pris",
        "create table if not exists intercept_pris ( "
        + "  intercept_id int not null, "
        + "  sequence int not null, "
        + "  value numeric not null, "
        + "  PRIMARY KEY ( intercept_id, sequence ), "
        + "  CONSTRAINT intercept_pri_fk "
        + "    FOREIGN KEY (intercept_id) REFERENCES intercept (intercept_id) "
        + "    ON DELETE CASCADE "
        + ")",
        "drop table if exists intercept_pds",
        "create table if not exists intercept_pds ( "
        + "  intercept_id int not null, "
        + "  sequence int not null, "
        + "  value numeric not null, "
        + "  PRIMARY KEY ( intercept_id, sequence ), "
        + "  CONSTRAINT intercept_pd_fk "
        + "    FOREIGN KEY (intercept_id) REFERENCES intercept (intercept_id) "
        + "    ON DELETE CASCADE "
        + ")"
    };

    public static void dewIt(Connection c) throws SQLException {
        try (Statement s = c.createStatement()) {
            for (String scriptEntry : script) {
                System.out.println("executing query: " + scriptEntry);
                s.executeUpdate(scriptEntry);
            }
        }
    }

}
