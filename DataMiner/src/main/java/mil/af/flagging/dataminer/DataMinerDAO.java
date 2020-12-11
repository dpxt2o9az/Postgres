/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.af.flagging.dataminer.model.EnvModeMap;
import mil.af.flagging.dataminer.model.EnvModeMapping;
import mil.af.flagging.dataminer.model.EnvRange;
import mil.af.flagging.dataminer.model.EnvSequence;
import mil.af.flagging.dataminer.model.Parameter;
import mil.af.flagging.dataminer.model.Signal;
import mil.af.flagging.dataminer.model.TuningDefaults;
import mil.af.flagging.dataminer.model.TuningParameter;
import mil.af.flagging.dataminer.model.TuningParameters;

/**
 *
 * @author Brad
 */
public class DataMinerDAO {

    private static final Logger LOG = Logger.getLogger(DataMinerDAO.class.getName());

    public static final AtomicLong modeID = new AtomicLong(0);
    public static final AtomicLong seqID = new AtomicLong(0);
    public static final AtomicLong priRangeID = new AtomicLong(0);
    public static final AtomicLong rfRangeID = new AtomicLong(0);
    public static final AtomicLong pdRangeID = new AtomicLong(0);
    public static final AtomicLong spRangeID = new AtomicLong(0);
    public static final AtomicLong irRangeID = new AtomicLong(0);

    Map<String, TuningParameter> fetchTuning(Connection conn, String NOTATION) throws SQLException {
        LOG.log(Level.INFO, "fetch tuning called for {0}", NOTATION);
        Map<String, TuningParameter> ret = defaultTuningParametersFor();
        String query = "select * from direct_tune_parms where elnot = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, NOTATION);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String parmString = rs.getString("PARM");
                    Parameter parm = Parameter.valueOf(parmString);
                    BigDecimal incr = rs.getBigDecimal("PARM_INCR");
                    BigDecimal thresh = rs.getBigDecimal("PARM_THRESH");
                    BigDecimal horizon = rs.getBigDecimal("PARM_HORIZ");
                    BigDecimal min = rs.getBigDecimal("PARM_MIN");
                    BigDecimal max = rs.getBigDecimal("PARM_MAX");
                    TuningParameter tp = new TuningParameter(incr, thresh, horizon, min, max);
                    TuningParameter orig = ret.get(parmString).applyParmsFrom(tp);
                    ret.put(parmString, tp);

                }
                LOG.log(Level.INFO, "fetch tuning completed for {0}", NOTATION);
                return ret;
            }
        }
    }

    Map<String, TuningParameter> defaultTuningParametersFor() {
        Map<String, TuningParameter> ret = new HashMap<>();
        ret.put("RF", new TuningParameter(TuningDefaults.RF_EXTRACT_INCR.val, TuningDefaults.RF_THRESH.val, TuningDefaults.RF_HORIZON.val));
        ret.put("PD", new TuningParameter(TuningDefaults.PD_EXTRACT_INCR.val, TuningDefaults.PD_THRESH.val, TuningDefaults.PD_HORIZON.val));
        ret.put("SP", new TuningParameter(TuningDefaults.SP_EXTRACT_INCR.val, TuningDefaults.SP_THRESH.val, TuningDefaults.SP_HORIZON.val));
        ret.put("IR", new TuningParameter(TuningDefaults.IR_EXTRACT_INCR.val, TuningDefaults.IR_THRESH.val, TuningDefaults.IR_HORIZON.val));
        ret.put("PRI", new TuningParameter(TuningDefaults.PRI_EXTRACT_INCR.val, TuningDefaults.PRI_THRESH.val, TuningDefaults.PRI_HORIZON.val));
        ret.put("JPR", new TuningParameter(TuningDefaults.JPR_EXTRACT_INCR.val, TuningDefaults.JPR_THRESH.val, TuningDefaults.JPR_HORIZON.val));
        return ret;
    }

    SortedSet<EnvRange> fetchEnvRanges(Connection conn, String NOTATION, String parm) throws SQLException {
        LOG.log(Level.INFO, "fetching env ranges for {0}, {1}", new Object[]{NOTATION, parm});
        SortedSet<EnvRange> ret = new TreeSet<>();

        String query = "select clstr_id, parm_min, parm_max, x_min, x_max from env_parm_clstrs where elnot = ? and parm = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, NOTATION);
            ps.setString(2, parm);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ret.add(new EnvRange(rs.getLong("CLSTR_ID"),
                            rs.getBigDecimal("PARM_MIN"),
                            rs.getBigDecimal("PARM_MAX"),
                            rs.getBigDecimal("X_MIN"),
                            rs.getBigDecimal("X_MAX")));
                }
                LOG.log(Level.INFO, "fetching env ranges complete for {0}, {1}", new Object[]{NOTATION, parm});
                return ret;
            }
        }
    }

    EnvSequence findSequence(Long id, Set<EnvSequence> envSeqs) {
        for (EnvSequence seq : envSeqs) {
            if (id.equals(seq.id)) {
                return seq;
            }
        }
        return null;
    }

    EnvRange findRange(Integer id, SortedSet<EnvRange> ranges) {
        for (EnvRange range : ranges) {
            if (range.getId().equals(id)) {
                return range;
            }
        }
        return null;
    }

    void storeModeMap(Connection conn, EnvModeMap map, String notation, int min_mode_ints) throws SQLException {
        boolean updatePRIs = false;
        for (EnvModeMapping mapping : map.mappings) {
            updatePRIs = true;
            if (map.id < 0) {
                map.id = modeID.incrementAndGet();
            }
            if (map.priSequence.id < 0) {
                map.priSequence.id = seqID.incrementAndGet();
            }
            updateClusters(conn, map, mapping, notation);
            DataContext.modeMapDAO.storeMapping(conn, map, mapping, notation, this);

        }
        if (updatePRIs) {
            updatePriSequence(conn, map, notation);
        }
        conn.commit();
    }

    void updatePriSequence(Connection conn, EnvModeMap map, String notation) throws SQLException {
        String parm = "PRI";
        if (map.MT.equals("J")) {
            parm = "JPR";
        }
        updatePriSequence(conn, parm, map.priSequence, notation);
    }

    void updatePriSequence(Connection conn, String parm, EnvSequence priSequence, String notation) throws SQLException {
        for (int ndx = 0; ndx < priSequence.seq.length; ndx++) {
            EnvRange priRange = priSequence.seq[ndx];
            if (priRange.getId() < 0) {
                priRange = new EnvRange(priRangeID.incrementAndGet(), priRange.getMinValue(), priRange.getMaxValue());
                insertCluster(conn, notation, parm, priRange);
            } else {
                updateCluster(conn, notation, parm, priRange);
            }
            mergeSequence(conn, priSequence.id, priRange.getId(), ndx + 1);
        }
    }

    void mergeSequence(Connection conn, long seqId, long id, int position) throws SQLException {
        String query = "select count(1) cnt from env_sequence_elements where seq_id = ? and element_id = ? and position = ?";
        int cnt = 0;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, seqId);
            ps.setLong(2, id);
            ps.setInt(3, position);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cnt = rs.getInt("CNT");
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ERROR CHECKING SEQUENCE", e);
            throw new RuntimeException(e);
        }
        if (cnt == 0) {
            query = "INSERT INTO ENV_SEQUENCE_ELEMENTS(SEQ_ID, ELEMENT_ID, POSITION) VALUES ( ?, ?, ? )";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setLong(1, seqId);
                ps.setLong(2, id);
                ps.setInt(3, position);
                ps.executeUpdate();
            } catch (SQLException e) {
                LOG.log(Level.SEVERE, "ERROR STORING SEQUENCE", e);
                throw new RuntimeException(e);
            }

        }

    }

    void updateClusters(Connection conn, EnvModeMap map, EnvModeMapping modeMap, String notation) throws SQLException {
        EnvRange rf = modeMap.RF;
        EnvRange pd = modeMap.PD;
        EnvRange sp = modeMap.SP;
        EnvRange ir = modeMap.IR;
        String st = modeMap.ST;
        if (rf.getId() < 0) {
            rf = new EnvRange(rfRangeID.incrementAndGet(), rf);
            insertCluster(conn, notation, "RF", rf);
        }
        if (pd.getId() < 0) {
            pd = new EnvRange(pdRangeID.incrementAndGet(), pd);
            insertCluster(conn, notation, "PD", pd);
        }
        if (sp.getId() < 0) {
            sp = new EnvRange(spRangeID.incrementAndGet(), sp);
            insertCluster(conn, notation, "SP", sp);
        }
        if (ir.getId() < 0) {
            ir = new EnvRange(irRangeID.incrementAndGet(), ir);
            insertCluster(conn, notation, "IR", ir);
        }
    }

    void insertCluster(Connection conn, String notation, String parm, EnvRange range) throws SQLException {
        String query = "INSERT INTO ENV_PARM_CLSTRS(ELNOT, PARM, CLSTR_ID, PARM_MIN, PARM_MAX, X_MIN, X_MAX ) VALUES ( ?, ?, ?, ?, ?, ?, ? )";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, notation);
            ps.setString(2, parm);
            ps.setLong(3, range.getId());
            ps.setBigDecimal(4, range.getMinValue());
            ps.setBigDecimal(5, range.getMaxValue());
            ps.setBigDecimal(6, range.getXMIN());
            ps.setBigDecimal(7, range.getXMAX());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ERROR STORING " + parm + " CLUSTER RESIDUE FOR " + notation, e);
            throw new RuntimeException(e);
        }
    }

    void updateCluster(Connection conn, String notation, String parm, EnvRange range) throws SQLException {
        String query = "UPDATE ENV_PARM_CLSTRS SET PARM_MIN = ?, PARM_MAX = ?, X_MIN = ?, X_MAX = ? WHERE PARM = ? AND CLSTR_ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setBigDecimal(1, range.getMinValue());
            ps.setBigDecimal(2, range.getMaxValue());
            ps.setBigDecimal(3, range.getXMIN());
            ps.setBigDecimal(4, range.getXMAX());
            ps.setString(5, parm);
            ps.setLong(6, range.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ERROR UPDATING " + parm + " CLUSTER RESIDEU FOR " + notation, e);
            throw new RuntimeException(e);
        }
    }

    void storeSignalAssociation(Connection conn, EnvModeMap map, Set<Signal> newSignals) throws SQLException {
        String insert = "INSERT INTO ENV_OP_MODE_INTS (MODE_ID, INTERCEPT_id) VALUES ( ?, ? )";
        String delete = "DELETE FROM ENV_RESIDUE_INTS WHERE INTERCEPT_ID = ? AND R_TYPE = ?";
        try (PreparedStatement ps = conn.prepareStatement(insert);
                PreparedStatement ps2 = conn.prepareStatement(delete)) {
            for (Signal signal : newSignals) {
                ps.setLong(1, map.id);
                ps.setLong(2, signal.id);
                ps.executeUpdate();
                ps2.setLong(1, signal.id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ERROR STORING SIGNAL ASSOCIATION", e);
            throw new RuntimeException(e);
        }
    }

    void initializeSequences(Connection conn) throws SQLException {
        try (Statement ps = conn.createStatement()) {
            try (ResultSet rs = ps.executeQuery("SELECT MAX(OP_MODE_ID) ID FROM ENV_MODE_MAPS")) {
                if (rs.next()) {
                    modeID.set(rs.getLong("ID"));
                }
            }
            try (ResultSet rs = ps.executeQuery("SELECT MAX(SEQ_ID) ID FROM ENV_SEQUENCE_ELEMENTS")) {
                if (rs.next()) {
                    seqID.set(rs.getLong("ID"));
                }
            }
            try (ResultSet rs = ps.executeQuery("SELECT MAX(CLSTR_ID) ID, PARM FROM ENV_PARM_CLSTRS GROUP BY PARM")) {
                while (rs.next()) {
                    final long id = rs.getLong("ID");
                    if (rs.wasNull()) {
                        // ???
                    }
                    final String parm = rs.getString("PARM");
                    switch (parm) {
                        case "PRI":
                        case "JPR":
                            if (priRangeID == null || id > priRangeID.get()) {
                                priRangeID.set(id);
                            }
                            break;
                        case "RF":
                            rfRangeID.set(id);
                            break;
                        case "PD":
                            pdRangeID.set(id);
                            break;
                        case "SP":
                            spRangeID.set(id);
                            break;
                        case "IR":
                            irRangeID.set(id);
                            break;
                        default:
                            LOG.log(Level.WARNING, "UNKNOWN/UNHANDLED PARAMETER TYPE {0}", parm);
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ERROR RETRIEVING LAST CLUSTER IDS", e);
            throw new RuntimeException(e);
        }
    }
}
