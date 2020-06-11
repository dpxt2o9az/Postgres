package mil.af.flagging.model;

import javax.sql.DataSource;
import mil.af.flagging.dataload.db.DataSourceBuilder;

public class Main {

    private static final String DB_URL = "jdbc:postgresql://maelstrom.lan:32769/postgres";
    private static final String DB_PASS = "";
    private static final String DB_USER = "postgres";

    public static void main(String[] args) throws Exception {
        InterceptGenerator.seed(28L);
        DataSource ds = DataSourceBuilder.build(DB_URL, DB_USER, DB_PASS);
        DbRunner runner = new JDBCRunnerWithConflicts(ds, 5000);
        runner.run();
    }

}
