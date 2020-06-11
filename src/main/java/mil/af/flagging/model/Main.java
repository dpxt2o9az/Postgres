package mil.af.flagging.model;

import javax.sql.DataSource;
import mil.af.flagging.dataload.db.DataSourceBuilder;
import org.zelmak.jdbc.schemaupdater.DatabaseSettings;

public class Main {

    public static void main(String[] args) throws Exception {
        DatabaseSettings db = DatabaseSettings.fromPropertiesFile(args[0]);
        InterceptGenerator.seed(28L);
        DataSource ds = DataSourceBuilder.build(db.url, db.username, db.password);
        DbRunner runner = new JDBCRunnerWithConflicts(ds, 5000);
        runner.run();
    }

}
