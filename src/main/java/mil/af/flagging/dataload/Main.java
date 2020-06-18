package mil.af.flagging.dataload;

import javax.sql.DataSource;
import mil.af.flagging.dataload.db.InterceptInserter;
import mil.af.flagging.dataload.db.JDBCInterceptInserterWithConflicts;
import mil.af.flagging.db.DataSourceBuilder;
import mil.af.flagging.model.InterceptGenerator;
import org.zelmak.jdbc.schemaupdater.DatabaseSettings;

public class Main {

    public static void main(String[] args) throws Exception {
        DatabaseSettings db = DatabaseSettings.fromPropertiesFile(args[0]);
        DataSource ds = DataSourceBuilder.build(db.url, db.username, db.password);
        InterceptGenerator.seed(28L);
//        for (int i = 0; i < 100; i++) {
            InterceptInserter runner = new JDBCInterceptInserterWithConflicts(ds, 7300);
            runner.run();
//        }
    }

}
