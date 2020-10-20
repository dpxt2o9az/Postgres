/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import mil.af.flagging.db.Result;
import mil.af.flagging.model.Intercept;
import mil.af.flagging.model.InterceptGenerator;

/**
 *
 * @author Brad
 */
public class JDBCInterceptInserterWithConflicts extends InterceptInserter {

    private final InterceptGenerator g;
    private final Map<String, String> dialect;

    public JDBCInterceptInserterWithConflicts(DataSource ds, Map<String, String> dialect, InterceptGenerator g, int icptCount) {
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
        Collection<Intercept> icpts = g.createInterceptsWithConflicts(super.icptCount, (int) (super.icptCount * 0.02));
        Map<Intercept, Result> results = dao.storeNewIntercepts(icpts);
        System.out.println(results.values().stream().collect(Collectors.groupingBy(Function.<Result>identity(), Collectors.<Result>counting())));
        dao.close();
    }

}
