/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.db;

import mil.af.flagging.dataminer.db.Mapper.MappingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.af.flagging.dataminer.model.EnvModeMap;
import mil.af.flagging.dataminer.model.EnvRange;
import mil.af.flagging.dataminer.model.TuningConstraints;
import mil.af.flagging.dataminer.model.Parameter;
import mil.af.flagging.dataminer.model.TuningConstraint;

/**
 *
 * @author Brad
 */
public class DataMinerDAO {

    private static final Logger LOG = Logger.getLogger(DataMinerDAO.class.getName());

    static void doNothing() {

    }

    public void initializeSequences(Connection conn) throws SQLException {
        doNothing();
    }

    public TuningConstraints fetchTuning(Connection conn, String notation) throws SQLException {
        String query
                = "select * "
                + "  from direct_tuning_parms "
                + "  where elnot = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, notation);
            try (ResultSet rs = ps.executeQuery()) {
                TuningConstraints tcs = new TuningConstraints();
                TuningConstraintMapper mapper = new TuningConstraintMapper();
                while (rs.next()) {
                    try {
                        TuningConstraint tc = mapper.map(rs);
                        tcs.put(tc.parm, tc);
                    } catch (MappingException e) {
                        LOG.log(Level.WARNING, "skipping ", e);

                    }
                }
                return tcs;
            }
        }
    }

    public Collection<EnvRange> fetchEnvRangesForElnotAndParm(Connection conn, String notation, Parameter parm) throws SQLException {
        final String query = "select * env_parm_clstrs where elnot = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, notation);
            try (ResultSet rs = ps.executeQuery()) {
                List<EnvRange> envRanges = new ArrayList<>();
                while(rs.next()) {
                    
                }
                return envRanges;
            }
        }
    }

    public Map<Parameter, Collection<EnvRange>> fetchEnvRangesForElnot(Connection conn, String notation) throws SQLException {
        Map<Parameter, Collection<EnvRange>> map = new HashMap<>();
        for (Parameter p : Parameter.values()) {
            Collection<EnvRange> envRanges = fetchEnvRangesForElnotAndParm(conn, notation, p);
            map.put(p, envRanges);
        }
        return map;
    }

    public void storeModeMap(Connection conn, EnvModeMap map, String notation, int minModeInts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
