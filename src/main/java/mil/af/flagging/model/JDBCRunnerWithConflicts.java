/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.model;

import mil.af.flagging.dataload.db.SchemaCreator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import javax.sql.DataSource;
import mil.af.flagging.dataload.db.ByRecordDataLoadInterceptDAO;

/**
 *
 * @author Brad
 */
public class JDBCRunnerWithConflicts extends DbRunner {

    public JDBCRunnerWithConflicts(DataSource ds, int icptCount) {
        super(ds, icptCount);
    }

    @Override
    public void run() {
        try {
            doJDBCWithOneTransaction();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupSchema() throws SQLException {
        try (Connection c = super.ds.getConnection()) {
            SchemaCreator.dewIt(c);
        }
    }

    private void doJDBCWithOneTransaction() throws SQLException {
        ByRecordDataLoadInterceptDAO dao = new ByRecordDataLoadInterceptDAO(super.ds);
        Collection<Intercept> icpts = InterceptGenerator.createInterceptsWithConflicts(super.icptCount, (int) (super.icptCount * 0.02));
        dao.storeNewIntercepts(icpts);
    }

}
