package mil.af.flagging.dataload;

import java.util.Collection;
import javax.sql.DataSource;
import mil.af.flagging.aoi.db.AoiDAO;
import mil.af.flagging.aoi.db.CountryDAO;
import mil.af.flagging.aoi.model.AreaOfInterest;
import mil.af.flagging.dataload.db.InterceptInserter;
import mil.af.flagging.dataload.db.JDBCInterceptInserterWithConflicts;
import mil.af.flagging.db.DataSourceBuilder;
import mil.af.flagging.model.AreaOfInterestGenerator;
import mil.af.flagging.model.Country;
import mil.af.flagging.model.Environment;
import mil.af.flagging.model.InterceptGenerator;
import org.zelmak.jdbc.schemaupdater.DatabaseSettings;

public class Main {

    public static void main(String[] args) throws Exception {
        DatabaseSettings db = DatabaseSettings.fromPropertiesFile(args[0]);
        DataSource ds = DataSourceBuilder.build(db.url, db.username, db.password);

        Environment e = Environment.randomEnvironment(0l);
        Collection<AreaOfInterest> aois = new AreaOfInterestGenerator(e.countries).generateAOIs(150);

        CountryDAO ccDao = new CountryDAO(ds);
        for (Country c : e.countries) {
            ccDao.storeCountry(c);
        }

        AoiDAO aDao = new AoiDAO(ds);
        for (AreaOfInterest aoi : aois) {
            aDao.storeAOI(aoi);
        }

        InterceptGenerator g = new InterceptGenerator(e);
        
        for (int i = 0; i < 200; i++) {
            InterceptInserter runner = new JDBCInterceptInserterWithConflicts(ds, g, 10000);
            runner.run();
        }
    }

}
