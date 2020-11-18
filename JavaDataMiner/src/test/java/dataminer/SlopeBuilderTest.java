package dataminer;

import mil.af.flagging.dataminer.SlopeBuilder;
import mil.af.flagging.dataminer.model.Slope;
import de.bytefish.sqlmapper.ResultSetMapping;
import de.bytefish.sqlmapper.SqlMapper;
import de.bytefish.sqlmapper.result.SqlMappingResult;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Brad
 */
public class SlopeBuilderTest extends TransactionalTestBase {

    private static final int FETCH_SIZE = 20000;
    private static final BigDecimal DEFAULT_RF_INCREMENT = new BigDecimal("0.1");

    private List<Slope> expectedSlopes;

    public SlopeBuilderTest() {
    }

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();

        expectedSlopes = Collections.unmodifiableList(buildExpectedSlopes());
    }

    static class RF {

        private BigDecimal rf;

        public void setRf(BigDecimal value) {
            this.rf = value;
        }

        public BigDecimal getRf() {
            return this.rf;
        }
    }

    static class BigDecimalMap extends ResultSetMapping<RF> {

        public BigDecimalMap() {
            this.map("rf", BigDecimal.class, RF::setRf);
        }
    }

    @Test
    public void streamedSlopeBuilderOutput_shouldMatchOriginal() throws SQLException, Exception {
        assertNotNull(expectedSlopes);
        assertFalse(expectedSlopes.isEmpty());
        SqlMapper sqlMapper = new SqlMapper(() -> new RF(), new BigDecimalMap());
        try (ResultSet resultSet = selectAllRfs()) {

            SqlMappingResult<RF> rf = sqlMapper.toEntity(resultSet);
            Stream<SqlMappingResult<RF>> stream = sqlMapper.toStream(resultSet);
            // Collect the Results as a List:
            stream.map(e -> e.getResult()).map(e->e.getRf()).forEach(System.out::println);
            // Assert the results:
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
        stmt.setFetchSize(FETCH_SIZE);
        return stmt.executeQuery("select rf from sample.intercepts order by rf");
    }
}
