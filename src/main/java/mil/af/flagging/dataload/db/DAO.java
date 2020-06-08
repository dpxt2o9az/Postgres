/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload.db;

import javax.sql.DataSource;

/**
 *
 * @author Brad
 */
public abstract class DAO {
    
    protected final DataSource ds;
    
    public DAO(DataSource ds) {
        this.ds = ds;
    }
}
