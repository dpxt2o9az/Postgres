package mil.af.flagging.aoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import mil.af.flagging.db.DAO;
import mil.af.flagging.db.Result;
import mil.af.flagging.model.Country;

public class CountryDAO extends DAO {

    public CountryDAO(DataSource ds) throws SQLException {
        super(ds);
    }

    public Result storeCountry(Country c) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("insert into country_codes ( country_code, description ) values ( ?, ? ) on conflict do nothing")) {
                ps.setString(1, c.countryCode);
                ps.setString(2, c.description);
                int count = ps.executeUpdate();
                return count != 1 ? Result.OTHER_FAILURE : Result.SUCCESS;
            }
        }
    }

    public List<Country> fetchCountries() throws SQLException {
        try (Connection conn = ds.getConnection()) {
            try (Statement st = conn.createStatement()) {
                try (ResultSet rs = st.executeQuery("select * from country_codes")) {
                    List<Country> countries = new ArrayList<>();
                    while (rs.next()) {
                        Country c = new Country(rs.getString(1), rs.getString(2));
                        countries.add(c);
                    }
                    return countries;
                }
            }
        }
    }

    @Override
    public void close() throws Exception {
        
    }

}
