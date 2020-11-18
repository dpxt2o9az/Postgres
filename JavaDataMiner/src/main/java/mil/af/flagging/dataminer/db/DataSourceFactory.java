/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import javax.sql.DataSource;

/**
 *
 * @author Brad
 */
public class DataSourceFactory {

    public static DataSource createDataSource(DatabaseConfiguration cfg) {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setJdbcUrl(cfg.url);
        cpds.setUser(cfg.username);
        cpds.setPassword(cfg.password);

//        cpds.setInitialPoolSize(5);
//        cpds.setMinPoolSize(5);
//        cpds.setAcquireIncrement(5);
//        cpds.setMaxPoolSize(50);
//        cpds.setMaxStatements(100);
        return cpds;
    }
}
