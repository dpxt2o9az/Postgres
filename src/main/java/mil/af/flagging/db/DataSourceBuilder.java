/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.db;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author Brad
 */
public class DataSourceBuilder {

    private final String url;
    private final String username;
    private final String password;
    private String driver;
    private Integer maxConnections;

    public DataSourceBuilder(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public DataSource build() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driver);
        if (maxConnections != null) {
            ds.setMaxTotal(maxConnections);
        }
        return ds;
    }

    public DataSourceBuilder driver(String driver) {
        this.driver = driver;
        return this;
    }

    public DataSourceBuilder maxConnections(int maxConn) {
        this.maxConnections = maxConn;
        return this;
    }

}
