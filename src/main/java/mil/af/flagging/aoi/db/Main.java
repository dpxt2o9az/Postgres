/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.aoi.db;

import javax.sql.DataSource;
import mil.af.flagging.db.DataSourceBuilder;
import org.zelmak.jdbc.schemaupdater.DatabaseSettings;

/**
 *
 * @author Brad
 */
public class Main {

    public static void main(String[] args) throws Exception {
        DatabaseSettings db = DatabaseSettings.fromPropertiesFile(args[0]);
        DataSource ds = DataSourceBuilder.build(db.url, db.username, db.password);
        
    }
}
