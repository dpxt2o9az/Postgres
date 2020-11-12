package pkg;

import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class PostgresBatchFailureTest {

    private static final String CREATE_TABLE
            = "CREATE TABLE IF NOT EXISTS test ( id integer, data varchar(50),CONSTRAINT test_pk PRIMARY KEY (id))";
    private static final String DROP_TABLE
            = "DROP TABLE test";
    private static List<Connection> connections;

    @BeforeClass
    public static void setupAll() throws IOException, SQLException {
        connections = new ArrayList<>();

        connections.add(DriverManager.getConnection("jdbc:postgresql://<db-hostname>:5432/<db-name>", "<db-user>", ""));
        connections.add(DriverManager.getConnection("jdbc:h2:mem:test;MODE=PostgreSQL"));

        for (Connection conn : connections) {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(CREATE_TABLE);
            conn.commit();
        }

    }

    @AfterClass
    public static void shutdownAll() throws IOException, SQLException {
        for (Connection conn : connections) {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(DROP_TABLE);
            conn.commit();
        }
    }

    @Before
    public void setUp() throws IOException, SQLException {

        for (Connection conn : connections) {

            try (Statement stmt = conn.createStatement()) {
                // initial data
                stmt.addBatch("insert into test (id, data) values (1, 'a')");
                stmt.addBatch("insert into test (id, data) values (2, 'b')");
                stmt.addBatch("insert into test (id, data) values (3, 'c')");
                System.out.println(Arrays.toString(stmt.executeBatch()));
            }
            conn.commit();
        }
    }

    @Test
    @Ignore
    public void testBatchUpdate() throws SQLException {
        List<List<String>> updateResults = new ArrayList<>();

        for (Connection conn : connections) {
            try (Statement stmt = conn.createStatement()) {
                // batch containing one row violating constraint
                stmt.addBatch("insert into test (id, data) values (4, 'd')");
                stmt.addBatch("insert into test (id, data) values (3, 'c')");
                stmt.addBatch("insert into test (id, data) values (5, 'e')");
                stmt.executeBatch();
                conn.commit();
            } catch (BatchUpdateException e) {
                System.out.println(Arrays.toString(e.getUpdateCounts()));
                updateResults.add(
                        Arrays.stream(e.getUpdateCounts())
                                .mapToObj(i -> {
                                    if (i == Statement.EXECUTE_FAILED) {
                                        return "FAILED";
                                    } else {
                                        return "DONE";
                                    }
                                }).collect(Collectors.toList())
                );
                System.out.println("> " + conn.getMetaData().getDatabaseProductName() + " database batch execution results: "
                        + updateResults.get(updateResults.size() - 1));
            }
            conn.rollback();
        }

        // check if the batch update counts are the same
        List<String> previousResults = null;
        for (List<String> currentResults : updateResults) {
            if (previousResults != null) {
                assertThat(previousResults, is(currentResults));
            }
            previousResults = currentResults;
        }

    }

    @After
    public void tearDown() throws SQLException {

    }
}
