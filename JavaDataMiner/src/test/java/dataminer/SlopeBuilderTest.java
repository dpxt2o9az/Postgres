package dataminer;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Brad
 */
public class SlopeBuilderTest extends TransactionalTestBase {

    private List<Slope> expectedSlopes;

    public SlopeBuilderTest() {
    }

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();

        expectedSlopes = Collections.unmodifiableList(buildExpectedSlopes());

    }

    @Test
    public void streamedSlopeBuilderOutput_shouldMatchOriginal() throws SQLException, Exception {
        assertNotNull(expectedSlopes);
        assertFalse(expectedSlopes.isEmpty());
        try (ResultSet rs = selectAllRfs()) {
            SlopeBuilderPlus sb = new SlopeBuilderPlus(new BigDecimal("0.1"));
            while (rs.next()) {
                sb.addValue(rs.getBigDecimal(1));
            }
            List<Slope> slopes = sb.generateSlopes();
            assertTrue(slopes.equals(expectedSlopes));
        }
    }

    private List<Slope> buildExpectedSlopes() throws SQLException, IOException {
        try (ResultSet rs = selectAllRfs()) {
            List<BigDecimal> values = new ArrayList<>();
            while (rs.next()) {
                BigDecimal value = rs.getBigDecimal(1);
                values.add(value);
            }

            List<Slope> slopes = new SlopeBuilder().generateSlopes(values, new BigDecimal("0.1"));
            PrintWriter pw = new PrintWriter(new FileWriter("xxx.txt"));
            for (Slope s : slopes) {
                pw.printf("%s, %s\n", s.parm_value, s.slope);
            }
            pw.close();
            return slopes;
        }
    }

    private ResultSet selectAllRfs() throws SQLException {
        Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(20000);
        return stmt.executeQuery("select rf from sample.intercepts order by rf");
    }
}
