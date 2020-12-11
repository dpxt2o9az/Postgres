/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author Brad
 */
public class DataSourceFactory {
    public static DataSource createDataSourceFrom(DatabaseConfiguration cfg) {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl(cfg.dbUrl);
        bds.setDriverClassName(cfg.dbDriver);
        bds.setUsername(cfg.dbUser);
        bds.setPassword(cfg.dbPassword);
        return bds;
    }
}
