/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.model;

import javax.sql.DataSource;

/**
 *
 * @author Brad
 */
public abstract class DbRunner implements Runnable {

    protected final DataSource ds;

    public DbRunner(DataSource ds) {
        this.ds = ds;
    }

}
