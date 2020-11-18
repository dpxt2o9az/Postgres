/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 *
 * @author Brad
 */
public class DatabaseConfiguration {

    public String driverName;
    public String url;
    public String schemaName;
    public String username;
    public String password;

    public static DatabaseConfiguration from(String x) {
        DatabaseConfiguration cfg = new DatabaseConfiguration();
        cfg.url = "jdbc:postgresql://127.0.0.1:5432/sampledb";
//        cfg.url = "jdbc:oracle:thin:@oracle19c.lan:1521:devdb19";
        cfg.username ="philipp";
        cfg.password = "test_pwd";
        return cfg;
    }
}
