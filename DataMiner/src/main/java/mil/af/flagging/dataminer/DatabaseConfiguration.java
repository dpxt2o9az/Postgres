/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Brad
 */
public class DatabaseConfiguration {

    public static DatabaseConfiguration from(String filename) throws IOException {
        return from(new File(filename));
    }

    public static DatabaseConfiguration from(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            Properties p = new Properties();
            p.load(fis);
            return from(p);
        }
    }

    public static DatabaseConfiguration from(Properties props) {
        DatabaseConfiguration dbCfg = new DatabaseConfiguration();
        dbCfg.dbUrl = props.getProperty("db.url");
        dbCfg.dbDriver = props.getProperty("db.driver");
        dbCfg.dbSchema = props.getProperty("db.schema");
        dbCfg.dbUser = props.getProperty("db.username");
        dbCfg.dbPassword = props.getProperty("db.password");
        return dbCfg;
    }
    public String dbUrl;
    public String dbDriver;
    public String dbSchema;
    public String dbUser;
    public String dbPassword;
}
