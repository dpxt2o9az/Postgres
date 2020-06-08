/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload.db;

import java.sql.SQLException;
import java.util.Collection;
import mil.af.flagging.model.Intercept;

/**
 *
 * @author Brad
 */
public interface DataloadDAO {
    Collection<Result> storeNewIntercept(Intercept icpt) throws SQLException;
    Collection<Result> storeNewIntercepts(Collection<Intercept> icpts) throws SQLException;
}
