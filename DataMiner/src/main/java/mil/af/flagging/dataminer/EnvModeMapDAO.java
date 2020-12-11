/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.af.flagging.dataminer.model.EnvModeMap;
import mil.af.flagging.dataminer.model.EnvModeMapping;
import mil.af.flagging.dataminer.model.EnvRange;
import mil.af.flagging.dataminer.model.EnvSequence;

/**
 *
 * @author Brad
 */
public class EnvModeMapDAO {

    private static final Logger LOG = Logger.getLogger(EnvModeMapDAO.class.getName());

    Set<EnvSequence> buildENVSequences(Connection conn, String notation, SortedSet<EnvRange> envRangesPRI, SortedSet<EnvRange> envRangesJPR) throws SQLException {
        LOG.log(Level.INFO, "building env sequences called for {0}", notation);
        Set<EnvSequence> ret = new HashSet<>();
        String query
                = "SELECT SEQ_ID, ELEMENT_ID, POSITION "
                + "  FROM ENV_SEQUENCE_ELEMENTS "
                + "  WHERE EXISTS ("
                + "    SELECT * FROM ENV_MODE_MAPS "
                + "      WHERE PRI_SEQ = SEQ_ID "
                + "        AND ELNOT = ? ) "
                + "  ORDER BY SEQ_ID, POSITION DESC";
        try (final PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, notation);
            try (final ResultSet rs = ps.executeQuery()) {
                EnvSequence seq = null;
                while (rs.next()) {
                    long id = rs.getLong("SEQ_ID");
                    if (seq == null || id != seq.id) {
                        if (seq != null) {
                            ret.add(seq);
                        }
                        seq = new EnvSequence(id);
                    }
                    int pos = rs.getInt("POSITION");
                    int element = rs.getInt("ELEMENT_ID");
                    EnvRange range = DataContext.dataMinerDAO.findRange(element, envRangesPRI);
                    if (range == null) {
                        range = DataContext.dataMinerDAO.findRange(element, envRangesJPR);
                    }
                    seq.addRange(range);
                }
                if (seq != null && !ret.contains(seq)) {
                    ret.add(seq);
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ERROR RETRIEVING SEQUENCES FOR " + notation, e);
            throw new RuntimeException(e);
        }
        LOG.log(Level.INFO, "building env seqeuences for {0}", notation);
        return ret;
    }

    SortedSet<EnvModeMap> buildEnvModeMaps(Connection conn, String notation, Set<EnvSequence> envSeqs) throws SQLException {
        SortedSet<EnvModeMap> ret = new TreeSet<>();
        String query
                = "SELECT DISTINCT OP_MODE_ID, MOD_TYPE, PRI_SEQ "
                + "  FROM ENV_MODE_MAPS "
                + "  WHERE ELNOT = ?";
        try (final PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, notation);
            try (final ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long opModeId = rs.getLong("OP_MODE_ID");
                    final String modType = rs.getString("MOD_TYPE");
                    final long priSeqId = rs.getLong("PRI_SEQ");
                    final EnvSequence sequence = DataContext.dataMinerDAO.findSequence(priSeqId, envSeqs);
                    LOG.log(Level.INFO, "adding sequence {0} to return", sequence.id);
                    ret.add(new EnvModeMap(opModeId, modType, sequence));
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ERROR RETRIEVING MODES FOR " + notation, e);
            throw new RuntimeException(e);
        }
        LOG.log(Level.INFO, "buildEnvModeMaps complete for {0}", notation);
        return ret;
    }

    void fillMap(Connection conn, EnvModeMap map, SortedSet<EnvRange> envRangesRF, SortedSet<EnvRange> envRangesPD, SortedSet<EnvRange> envRangesSP, SortedSet<EnvRange> envRangesIR) throws SQLException {
        String query
                = "SELECT RF_MODE, PD_MODE, SP_MODE, IR_MODE, SCAN_TYPE FROM ENV_MODE_MAPS "
                + "  WHERE OP_MODE_ID = ?";
        try (final PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, map.id);
            try (final ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int RF = rs.getInt("RF_MODE");
                    EnvRange rfRange = DataContext.dataMinerDAO.findRange(RF, envRangesRF);
                    if (rfRange != null) {
                        int PD = rs.getInt("PD_MODE");
                        int SP = rs.getInt("SP_MODE");
                        int IR = rs.getInt("IR_MODE");
                        String ST = rs.getString("SCAN_TYPE");
                        EnvRange pdRange = DataContext.dataMinerDAO.findRange(PD, envRangesPD);
                        EnvRange spRange = DataContext.dataMinerDAO.findRange(SP, envRangesSP);
                        EnvRange irRange = DataContext.dataMinerDAO.findRange(IR, envRangesIR);
                        if (pdRange == null) {
                            pdRange = EnvRange.ZERO;
                        }
                        if (spRange == null) {
                            spRange = EnvRange.ZERO;
                        }
                        if (irRange == null) {
                            irRange = EnvRange.ZERO;
                        }
                        map.addMapping(rfRange, pdRange, spRange, irRange, ST);
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ERROR RETRIEVING MAPS FOR MODE " + map.id, e);
            throw new RuntimeException(e);
        }
    }

    void storeMapping(Connection conn, EnvModeMap map,
            EnvModeMapping mapping, String notation,
            DataMinerDAO aThis
    ) throws SQLException {
        String query = "INSERT INTO ENV_MODE_MAPS "
                + "  ( OP_MODE_ID, ELNOT, MOD_TYPE, PRI_SEQ, RF_MODE, PD_MODE, SP_MODE, IR_MODE, SCAN_TYPE ) "
                + "  VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) "
                + "  FROM DUAL "
                + "  WHERE NOT EXISTS "
                + "    ( SELECT *"
                + "      FROM ENV_MODE_MAPS "
                + "      WHERE OP_MODE_ID = ? "
                + "        AND ELNOT = ? "
                + "        AND MOD_TYPE = ? "
                + "        AND PRI_SEQ = ? "
                + "        AND RF_MODE = ? "
                + "        AND PD_MODE = ? "
                + "        AND SP_MODE = ? "
                + "        AND IR_MODE = ? "
                + "        AND SCAN_TYPE = ? "
                + "    )";
        try (final PreparedStatement ps = conn.prepareStatement(query)) {
            int c = 1;
            ps.setLong(c++, map.id);
            ps.setString(c++, notation);
            ps.setString(c++, map.MT);
            ps.setLong(c++, map.priSequence.id);
            ps.setLong(c++, mapping.RF.getId());
            ps.setLong(c++, mapping.PD.getId());
            ps.setLong(c++, mapping.SP.getId());
            ps.setLong(c++, mapping.IR.getId());
            ps.setString(c++, mapping.ST);
            ps.setLong(c++, map.id);
            ps.setString(c++, notation);
            ps.setString(c++, map.MT);
            ps.setLong(c++, map.priSequence.id);
            ps.setLong(c++, mapping.RF.getId());
            ps.setLong(c++, mapping.PD.getId());
            ps.setLong(c++, mapping.SP.getId());
            ps.setLong(c++, mapping.IR.getId());
            ps.setString(c++, mapping.ST);
            ps.executeUpdate();
            ps.close();
            DataContext.dataMinerDAO.storeSignalAssociation(conn, map, mapping.newSignals);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ERROR STORING MODE MAP FORM " + notation, e);
            throw new RuntimeException(e);
        }
    }

}
