/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload;

import mil.af.flagging.db.Result;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import mil.af.flagging.model.Intercept;

/**
 *
 * @author Brad
 */
public interface DataloadDAO {
    Result storeNewIntercept(Intercept icpt) throws SQLException;
    Map<Intercept, Result> storeNewIntercepts(Collection<Intercept> icpts) throws SQLException;
}
