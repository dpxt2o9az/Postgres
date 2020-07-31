/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.aoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import mil.af.flagging.db.DAO;
import mil.af.flagging.db.Result;

/**
 *
 * @author Brad
 */
public class AoiDAO extends DAO {

    private static final String INSERT_AOI_COUNTRY_CODES = "insert into aoi_country_codes ( aoi_code, country_code ) values ( ?, ? ) on conflict do nothing";
    private static final String INSERT_AOI_CODES = "insert into aoi_codes ( aoi_code, description ) values ( ?, ? ) on conflict do nothing";
    private static final String SELECT_AOI_COUNTRIES = "select * from aoi_country_codes where aoi_code = ?";
    private static final String SELECT_ALL_AOI_CODES = "select * from aoi_codes";

    public AoiDAO(DataSource ds) throws SQLException {
        super(ds);
    }

    public Result storeAOI(AreaOfInterest aoi) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement mainPs = conn.prepareStatement(INSERT_AOI_CODES);
                    PreparedStatement aoiCcPs = conn.prepareStatement(INSERT_AOI_COUNTRY_CODES)) {
                mainPs.setString(1, aoi.aoiCode);
                mainPs.setString(2, aoi.description);
                int count = mainPs.executeUpdate();
                if (count != 1) {
                    return Result.OTHER_FAILURE;
                }
                for (String cc : aoi.countryCodes) {
                    aoiCcPs.setString(1, aoi.aoiCode);
                    aoiCcPs.setString(2, cc);
                    aoiCcPs.executeUpdate();
                }
                return Result.SUCCESS;
            }
        }
    }

    public List<AreaOfInterest> fetchAOIs() throws SQLException {
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement mainPs = conn.prepareStatement(SELECT_ALL_AOI_CODES);
                    PreparedStatement aoiCcPs = conn.prepareStatement(SELECT_AOI_COUNTRIES)) {
                try (ResultSet rs = mainPs.executeQuery()) {
                    List<AreaOfInterest> all = new ArrayList<>();
                    while (rs.next()) {

                        AreaOfInterest aoi = new AreaOfInterest(rs.getString(1), rs.getString(2));
                        aoiCcPs.setString(1, aoi.aoiCode);
                        try (ResultSet rs2 = aoiCcPs.executeQuery()) {
                            while (rs2.next()) {
                                aoi.countryCodes.add(rs.getString(1));
                            }
                        }

                        all.add(aoi);
                    }
                    return all;
                }
            }
        }
    }

    @Override
    public void close() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
