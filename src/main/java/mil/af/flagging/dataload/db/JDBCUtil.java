/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload.db;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Brad
 */
public class JDBCUtil {

    private static final Logger LOG = Logger.getLogger(JDBCUtil.class.getName());

    public static void closeQuietly(AutoCloseable c) {
        try {
            c.close();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "close quietly: ", e);
        }
    }
}
