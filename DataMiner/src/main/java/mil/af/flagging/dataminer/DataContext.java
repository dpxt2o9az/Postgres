/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import javax.sql.DataSource;

/**
 *
 * @author Brad
 */
public class DataContext {

    public static DataSource ds;
    public static DataMinerDAO dataMinerDAO;
    public static EnvModeMapDAO modeMapDAO;
    public static ResidueInterceptDAO residueDAO;
}
