/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload.db;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import javax.sql.DataSource;
import mil.af.flagging.model.Country;
import mil.af.flagging.model.Intercept;
import mil.af.flagging.model.InterceptGenerator;

/**
 *
 * @author Brad
 */
public class JDBCInterceptInserter extends InterceptInserter {

    private final List<Country> countries;

    public JDBCInterceptInserter(DataSource ds, List<Country> countries, int icptCount) {
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
        Collection<Intercept> icpts = gen.createIntercepts(super.icptCount);
        dao.storeNewIntercepts(icpts);
    }

}
