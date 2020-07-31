
package org.zelmak.jdbc.schemaupdater;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class DatabaseSettings {
    
    public String url;
    public String username;
    public String password;

    public static DatabaseSettings fromPropertiesFile(File file) throws IOException {
        Properties p = new Properties();
        try (final Reader r = new FileReader(file)) {
            p.load(r);
        }
        return fromProperties(p);
    }
    
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
