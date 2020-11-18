/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import mil.af.flagging.dataminer.db.ViprDeltaStatsDAO;
import mil.af.flagging.dataminer.db.InterceptResidueDAO;
import mil.af.flagging.dataminer.db.DataMinerDAO;
import javax.sql.DataSource;
import mil.af.flagging.dataminer.db.ModeDAO;

/**
 *
 * @author Brad
 */
public class DataContext {
    public static DataSource ds;
    public static DataMinerDAO dataMinerDAO;
    public static InterceptResidueDAO residueDAO;
    public static ModeDAO modeDAO;
    public static ViprDeltaStatsDAO statsDAO;
}
