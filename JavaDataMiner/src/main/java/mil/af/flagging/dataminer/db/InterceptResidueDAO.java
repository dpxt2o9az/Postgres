/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.af.flagging.dataminer.model.Signal;

/**
 *
 * @author Brad
 */
public class InterceptResidueDAO {

    private static final Logger LOG = Logger.getLogger(InterceptResidueDAO.class.getName());

    SignalMapper signalMapper = new SignalMapper();


 public    SortedSet<String> fetchDistinctResidueNotations(Connection conn) throws SQLException {
        final String query = "select elnot, count(1) \"COUNT\" from env_residue_ints join intercepts using (intercept_id) group by elnot";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                Map<String, Integer> freqCounts = new TreeMap<>();
                while (rs.next()) {
                    final String elnot = rs.getString("ELNOT");
                    Integer count = rs.getInt("COUNT");
                    if (rs.wasNull()) {
                        count = null;
                    }
                    freqCounts.put(elnot, count);
                }
                return new TreeSet(freqCounts.keySet());
            }
        }
    }

    public void cleanResidueOld(Connection conn, String notation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Collection<Signal> fetchResidue(Connection conn, String notation) throws SQLException {
        final String interceptQuery 
                = "select * " 
                + "  from intercepts " 
                + "    join env_residue_ints " 
                + "      using (intercept_id) " 
                + "  where elnot = ?";
        final String priQuery 
                = "select * " 
                + "  from intercept_pris " 
                + "    join env_residue_ints " 
                + "      using (intercept_id) " 
                + "  where intercept_id = ? " 
                + "  order by pri_number";
        try (final PreparedStatement ps = conn.prepareStatement(interceptQuery);
                final PreparedStatement pps = conn.prepareStatement(priQuery)) {
            pps.setFetchSize(20);
            ps.setString(1, notation);
            try (final ResultSet rs = ps.executeQuery()) {
                List<Signal> signals = new ArrayList<>();
                while (rs.next()) {
                    try {
                        Signal s = signalMapper.map(rs);
                        pps.setLong(1, s.id);
                        try (final ResultSet prs = pps.executeQuery()) {
                            List<Double> pris = new ArrayList<>();
                            while (prs.next()) {
                                Integer index = prs.getInt("PRI_NUMBER");
                                if (prs.wasNull()) {
                                    LOG.log(Level.WARNING, "PRI_NUMBER was null");
                                }
                                Double value = prs.getDouble("PRI_VALUE");
                                if (prs.wasNull()) {
                                    LOG.log(Level.WARNING, "pri-value was null for id:{0}, pri_num:{1}", new Object[]{s.id, index});
                                    continue;
                                }
                                pris.add(value);
                            }
                            s.pri = pris;
                        }
                        signals.add(s);
                    } catch (Mapper.MappingException e) {
                        LOG.log(Level.WARNING, "skipping row", e);
                        continue;
                    }
                }
                return signals;
            }
        }
    }

}
