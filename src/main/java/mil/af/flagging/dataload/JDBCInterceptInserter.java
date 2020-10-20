/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import javax.sql.DataSource;
import mil.af.flagging.model.Intercept;
import mil.af.flagging.model.InterceptGenerator;

/**
 *
 * @author Brad
 */
public class JDBCInterceptInserter extends InterceptInserter {

    private final InterceptGenerator g;
    private final Map<String, String> dialect;

    public JDBCInterceptInserter(DataSource ds, Map<String, String> dialect, InterceptGenerator g, int icptCount) {
        super(ds, icptCount);
        this.g = g;
        this.dialect = dialect;
    }

    @Override
    public void run() {
        try {
            doJDBCWithOneTransaction();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void doJDBCWithOneTransaction() throws SQLException {
        ByRecordDataLoadInterceptDAO dao = new ByRecordDataLoadInterceptDAO(super.ds, dialect);
        Collection<Intercept> icpts = g.createIntercepts(super.icptCount);
        dao.storeNewIntercepts(icpts);
    }

}
