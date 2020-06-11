/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zelmak.jdbc.schemaupdater;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

/**
 *
 * @author Brad
 */
public class DatabaseSettings {
    
    public String url;
    public String username;
    public String password;

    public static DatabaseSettings fromPropertiesFile(String filename) throws IOException {
        Properties p = new Properties();
        try (final Reader r = new FileReader(filename)) {
            p.load(r);
        }
        return fromProperties(p);
    }

    public static DatabaseSettings fromProperties(Properties p) {
        DatabaseSettings db = new DatabaseSettings();
        db.url = p.getProperty("db.url");
        db.username = p.getProperty("db.username");
        db.password = p.getProperty("db.password");
        return db;
    }
    
}
