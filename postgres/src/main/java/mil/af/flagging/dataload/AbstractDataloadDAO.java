/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload;

import java.util.Map;
import mil.af.flagging.db.DAO;
import javax.sql.DataSource;

/**
 *
 * @author Brad
 */
public abstract class AbstractDataloadDAO extends DAO implements DataloadDAO {

    public AbstractDataloadDAO(DataSource ds) {
        super(ds);
    }
    
}
