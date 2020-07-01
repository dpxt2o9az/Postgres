/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload.db;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import mil.af.flagging.db.Result;
import mil.af.flagging.model.Country;
import mil.af.flagging.model.Intercept;
import mil.af.flagging.model.InterceptGenerator;

/**
 *
 * @author Brad
 */
public class JDBCInterceptInserterWithConflicts extends InterceptInserter {

    private final Collection<Country> countries;

    public JDBCInterceptInserterWithConflicts(DataSource ds, Collection<Country> countries, int icptCount) {
        super(ds, icptCount);
        this.countries = countries;
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
        ByRecordDataLoadInterceptDAO dao = new ByRecordDataLoadInterceptDAO(super.ds);
        InterceptGenerator gen = new InterceptGenerator(countries);
        Collection<Intercept> icpts = gen.createInterceptsWithConflicts(super.icptCount, (int) (super.icptCount * 0.02));
        Map<Intercept, Result> results = dao.storeNewIntercepts(icpts);
        System.out.println(results.values().stream().collect(Collectors.groupingBy(Function.<Result>identity(), Collectors.<Result>counting())));
        dao.close();
    }

}
