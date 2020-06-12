/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.aoi;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.sql.DataSource;
import mil.af.flagging.db.DataSourceBuilder;
import org.zelmak.jdbc.schemaupdater.DatabaseSettings;

/**
 *
 * @author Brad
 */
public class Main {

    private static final String MERGE_QUERY
            = "insert into intercept_aois as a"
            + " (select intercept_id, listagg(aoi_code, ';') within group (order by aoi_code) || ';W1' aoi_code "
            + "  from idb_states "
            + "  join intercepts "
            + "    using(intercept_id) "
            + "  join aoi_country_codes "
            + "    using(country_code) "
            + "  where flow_control = ? "
            + "  group by intercept_id) as b "
            + "  on (a.intercept_id = b.intercept_id) "
            + "  when matched "
            + "    then update set aoi_code = b.aoi_code "
            + "  when not matched then "
            + "    insert values (b.intercept_id, b.aoi_code)";

    public static void main(String[] args) throws Exception {
        DatabaseSettings db = DatabaseSettings.fromPropertiesFile(new File(System.getProperty("user.home"), "projects/postgres/src/main/resources/cfg"));
        DataSource ds = DataSourceBuilder.build(db.url, db.username, db.password);
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(MERGE_QUERY)) {
                ps.setString(1, "AOI_REQ");
                ps.executeUpdate();
            }
        }
    }
    
}
