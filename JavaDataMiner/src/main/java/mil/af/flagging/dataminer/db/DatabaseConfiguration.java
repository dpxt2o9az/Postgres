/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.db;

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
        cfg.url = "";
        cfg.username ="";
        cfg.password = "";
        return cfg;
    }
}
