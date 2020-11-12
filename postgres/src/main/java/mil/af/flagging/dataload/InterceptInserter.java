/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload;

import javax.sql.DataSource;

/**
 *
 * @author Brad
 */
public abstract class InterceptInserter implements Runnable {

    protected final DataSource ds;
    protected final int icptCount;

    public InterceptInserter(DataSource ds, int icptCount) {
        this.ds = ds;
        this.icptCount = icptCount;
    }

}
