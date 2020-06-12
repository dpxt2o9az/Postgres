/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload.db;

import mil.af.flagging.db.SchemaCreator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import javax.sql.DataSource;
import mil.af.flagging.dataload.db.ByRecordDataLoadInterceptDAO;
import mil.af.flagging.model.Intercept;
import mil.af.flagging.model.InterceptGenerator;

/**
 *
 * @author Brad
 */
public class JDBCInterceptInserter extends InterceptInserter {

    public JDBCInterceptInserter(DataSource ds, int icptCount) {
        super(ds, icptCount);
    }

    @Override
    public void run() {
        try {
            setupSchema();
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
        Collection<Intercept> icpts = InterceptGenerator.createIntercepts(super.icptCount);
        dao.storeNewIntercepts(icpts);
    }

}
