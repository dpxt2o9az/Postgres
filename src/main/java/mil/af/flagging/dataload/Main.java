package mil.af.flagging.dataload;

import java.util.Collection;
import java.util.Map;
import javax.sql.DataSource;
import mil.af.flagging.aoi.AoiDAO;
import mil.af.flagging.aoi.CountryDAO;
import mil.af.flagging.aoi.AreaOfInterest;
import mil.af.flagging.db.DataSourceBuilder;
import mil.af.flagging.model.AreaOfInterestGenerator;
import mil.af.flagging.model.Country;
import mil.af.flagging.model.Environment;
import mil.af.flagging.model.InterceptGenerator;
import org.zelmak.jdbc.schemaupdater.DatabaseSettings;

public class Main {

    public static void main(String[] args) throws Exception {

        DatabaseSettings dbCfg = DatabaseSettings.fromPropertiesFile(args[0]);

        DataSource ds = createDataSourceFrom(dbCfg);

        Map<String, String> dialect = new OracleDialect();

        Environment e = Environment.randomEnvironment(0l);
        Collection<AreaOfInterest> aois = new AreaOfInterestGenerator(e.countries).generateAOIs(150);

        CountryDAO ccDao = new CountryDAO(ds, dialect);
        for (Country c : e.countries) {
            ccDao.storeCountry(c);
        }

        AoiDAO aDao = new AoiDAO(ds, dialect);
        for (AreaOfInterest aoi : aois) {
            aDao.storeAOI(aoi);
        }

        InterceptGenerator g = new InterceptGenerator(e);

        for (int i = 0; i < 200; i++) {
            InterceptInserter runner = new JPAInterceptInserter(ds, g, 10_000);
            runner.run();
        }
    }

    private static DataSource createDataSourceFrom(DatabaseSettings dbCfg) {
        return new DataSourceBuilder(dbCfg.url, dbCfg.username, dbCfg.password)
                .driver(dbCfg.driver)
                .maxConnections(10)
                .build();
    }

}
