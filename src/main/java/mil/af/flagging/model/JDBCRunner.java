/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import javax.sql.DataSource;
import mil.af.flagging.dataload.db.ByRecordDataLoadInterceptDAO;

/**
 *
 * @author Brad
 */
public class JDBCRunner extends DbRunner {

    public JDBCRunner(DataSource ds) {
        super(ds);
    }
    
    @Override
    public void run() {
        try {
            setupSchema(ds);
            doJDBCWithOneTransaction(ds);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setupSchema(DataSource ds) throws SQLException {
        try (Connection c = ds.getConnection()) {
            SchemaCreator.dewIt(c);
        }
    }

    private static void doJDBCWithOneTransaction(DataSource ds) throws SQLException {
        ByRecordDataLoadInterceptDAO dao = new ByRecordDataLoadInterceptDAO(ds);
        Collection<Intercept> icpts = InterceptGenerator.createIntercepts(1000);
        dao.storeNewIntercepts(icpts);
    }

}
