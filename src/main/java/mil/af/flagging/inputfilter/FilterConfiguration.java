/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.inputfilter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Brad
 */
public class FilterConfiguration {

    Set<String> excludedElnots = new HashSet<>();
    Set<String> includedReadoutStations = new HashSet<>();
    Set<String> includedModulationTypes = new HashSet<>();

    static FilterConfiguration fromDatabase(Connection conn) throws SQLException {
        FilterConfiguration cfg = new FilterConfiguration();
        try (Statement st = conn.createStatement()) {
            try (ResultSet rs = st.executeQuery("select mt, mt_desc from mod_types")) {
                while (rs.next()) {
                    cfg.includedModulationTypes.add(rs.getString("MT"));
                }
            }
            try (ResultSet rs = st.executeQuery("select elnot from excluded_elnots")) {
                while (rs.next()) {
                    cfg.excludedElnots.add(rs.getString("ELNOT"));
                }
            }
            try (ResultSet rs = st.executeQuery("select rd_out_stat from included_rd_out_stat")) {
                while (rs.next()) {
                    cfg.includedReadoutStations.add(rs.getString("RD_OUT_STAT"));
                }
            }
        }

        return cfg;
    }

}
