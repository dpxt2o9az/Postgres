/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataminer;

import mil.af.flagging.dataminer.RangeBuilder;
import mil.af.flagging.dataminer.SlopeBuilder;
import mil.af.flagging.dataminer.model.Slope;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Brad
 */
public class RangeBuilderTest extends TransactionalTestBase {

    private static final BigDecimal DEFAULT_RF_HORIZON = new BigDecimal("50.0");
    private static final BigDecimal DEFAULT_RF_THRESHOLD = new BigDecimal("0.5");
    private static final BigDecimal DEFAULT_RF_INCREMENT = new BigDecimal("0.1");

    @Test
    public void xxx() throws Exception {
        try (ResultSet rs = selectAllRfs()) {
            List<BigDecimal> values = new ArrayList<>();
            while (rs.next()) {
                final BigDecimal value = rs.getBigDecimal(1);
                values.add(value);
            }

            SlopeBuilder sb = new SlopeBuilder(values, DEFAULT_RF_INCREMENT);
            RangeBuilder rb = new RangeBuilder(sb);
            rb.generateRanges(DEFAULT_RF_INCREMENT, DEFAULT_RF_THRESHOLD, DEFAULT_RF_HORIZON);
        }
    }

    private List<Slope> buildExpectedSlopes() throws SQLException, IOException {
        try (ResultSet rs = selectAllRfs()) {
            List<BigDecimal> values = new ArrayList<>();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal(1);
                values.add(value);
            }

            List<Slope> slopes = new SlopeBuilder(values, DEFAULT_RF_INCREMENT).generateSlopes();
            PrintWriter pw = new PrintWriter(new FileWriter("xxx.txt"));
            for (Slope s : slopes) {
                pw.printf("%s, %s\n", s.parm_value, s.slope);
            }
            pw.close();
            return slopes;
        }
    }

    public ResultSet selectAllRfs() throws SQLException {
        Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(20000);
        return stmt.executeQuery("select rf from sample.intercepts order by rf");
    }

}
