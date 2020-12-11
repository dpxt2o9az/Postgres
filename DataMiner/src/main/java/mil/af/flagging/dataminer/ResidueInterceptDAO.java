/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.af.flagging.dataminer.model.Signal;

/**
 *
 * @author Brad
 */
public class ResidueInterceptDAO {

    private static final Logger LOG = Logger.getLogger(ResidueInterceptDAO.class.getName());

    public static final String MAX_RESIDUE_DAYS_KEY = "MAX_RESIDUE_DAYS";
    public static final String PROCESSING_LZIPS_KEY = "PROCESSING_LZIPS";
    public static final String MAX_RESIDUE_THRESHOLD_KEY = "MAX_RESIDUE_THRESHOLD";

    public static final int DEFAULT_RESIDUE_THRESHOLD = 1000;
    public static final int DEFAULT_RESIDUE_DAYS = 180;
    public static final boolean DEFAULT_PROCESSING_LZIPS = true;

    public static final int RESIDUE_THRESHOLD;
    public static final int RESIDUE_DAYS;
    public static final boolean PROCESSING_LZIPS;

    static {
        RESIDUE_DAYS = Integer.getInteger(MAX_RESIDUE_DAYS_KEY, DEFAULT_RESIDUE_DAYS);
        RESIDUE_THRESHOLD = Integer.getInteger(MAX_RESIDUE_THRESHOLD_KEY, DEFAULT_RESIDUE_THRESHOLD);
        PROCESSING_LZIPS = Boolean.getBoolean(PROCESSING_LZIPS_KEY);
    }

    private static final String FETCH_RESIDUE_QUERY
            = "SELECT -1 MODE_ID, R.INTERCEPT_ID, RF.VALUE RF, NVL(PD1.VALUE,0) PD, SCAN_PERIOD SP, IR, NVL(SCAN_TYPE, 'Z') SCAN_TYPE, MOD_TYPE "
            + "  FROM ENV_RESIDUE_INTS R "
            + "    JOIN INTERCEPTS I1"
            + "      ON R.INTERCEPT_ID = I1.INTERCEPT_ID "
            + "    JOIN INTERCEPT_RFS RF"
            + "      ON I1.INTERCEPT_ID = RF.INTERCEPT_ID"
            + "      AND RF.SEQUENCE = 1"
            + "    JOIN INTERCEPT_PDS PD1"
            + "      ON I1.INTERCEPT_ID = PD1.INTERCEPT_ID"
            + "      AND PD1.SEQUENCE = 1"
            + "  WHERE ELNOT = ? "
            + "    AND R_TYPE = 'P' "
            + "UNION "
            + "SELECT MODE_ID, OMI.INTERCEPT_ID, RF2.VALUE RF, NVL(PD2.VALUE,0) PD, SCAN_PERIOD SP, IR, NVL(SCAN_TYPE, 'Z') SCAN_TYPE, MOD_TYPE "
            + "  FROM ENV_OP_MODE_INTS OMI"
            + "    JOIN INTERCEPTS I2"
            + "      ON OMI.INTERCEPT_ID = I2.INTERCEPT_ID "
            + "    JOIN INTERCEPT_RFS RF2"
            + "      ON I2.INTERCEPT_ID = RF2.INTERCEPT_ID"
            + "      AND RF2.SEQUENCE = 1"
            + "    JOIN INTERCEPT_PDS PD2"
            + "      ON I2.INTERCEPT_ID = PD2.INTERCEPT_ID"
            + "      AND PD2.SEQUENCE = 1"
            + "  WHERE ELNOT = ? "
            + "    AND TIME_PROCESS > ?";

    private static final String PRI_QUERY
            = "SELECT SEQUENCE PRI_NUMBER, VALUE PRI_VALUE "
            + "  FROM INTERCEPT_PRIS "
            + "  WHERE INTERCEPT_ID = ? "
            + "  ORDER BY SEQUENCE DESC";

    private static final String RESIDUE_NOTATIONS_QUERY
            = "SELECT ELNOT, COUNT(1) "
            + "  FROM ENV_RESIDUE_INTS "
            + "    JOIN INTERCEPTS"
            + "      USING (INTERCEPT_ID) "
            + "  WHERE R_TYPE = 'P' "
            + "    AND ELNOT != 'L0000' "
            + "  GROUP BY ELNOT";

    private static final String OLD_RESIDUE_COUNT_BY_ELNOT_QUERY
            = "SELECT COUNT(1) CNT "
            + "  FROM INTERCEPTS "
            + "  WHERE ELNOT = ? "
            + "    AND INT_UP_TIME > SYSDATE - ?";

    private static final String RESIDUE_COUNT_BY_ELNOT_QUERY
            = "SELECT COUNT(1) CNT "
            + "  FROM ENV_RESIDUE_INTS "
            + "    JOIN INTERCEPTS "
            + "      USING (INTERCEPT_ID) "
            + "  WHERE R_TYPE = 'P' "
            + "    AND ELNOT = ? "
            + "    AND INT_UP_TIME > SYSDATE - ?";

    private static boolean residueCountExceedsThreshold(Connection conn, String notation) {
        LOG.log(Level.FINE, "fetching count of intercepts with notation {0}, newer than {1} days", new Object[]{notation, RESIDUE_DAYS});
        try (final PreparedStatement ps = conn.prepareStatement(RESIDUE_COUNT_BY_ELNOT_QUERY)) {
            ps.setString(1, notation);
            ps.setInt(2, RESIDUE_DAYS);
            try (final ResultSet rs = ps.executeQuery()) {
                int cnt = 0;
                if (rs.next()) {
                    cnt = rs.getInt("CNT");
                }
                return cnt > RESIDUE_THRESHOLD;
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ERROR RETRIEVING RESIDUE COUNT FOR " + notation, e);
            throw new RuntimeException(e);
        }
    }

    private BigDecimal[] fetchPrisForIntercept(Connection conn, Long id) throws SQLException {
        // todo: do this in bulk... need to staticfy the higher level query! going id-by-id is slow!
        LOG.log(Level.FINER, "fetch pris called for intercept-id {0}", id);
        BigDecimal[] ret = new BigDecimal[0];
        try (PreparedStatement ps = conn.prepareStatement(PRI_QUERY)) {
            ps.setFetchSize(20);
            ps.setLong(1, id);
            try (final ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BigDecimal value = rs.getBigDecimal("PRI_VALUE");
                    int pos = rs.getInt("PRI_NUMBER");
                    if (ret.length == 0) {
                        ret = new BigDecimal[pos];
                    }
                    ret[pos - 1] = value;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ERROR RETRIEVING PRIS FOR " + id, e);
            throw new RuntimeException(e);
        }
        LOG.log(Level.FINER, "fetch pris complete for intercept-id {0}", id);
        return ret;
    }

    SortedSet<String> fetchDistinctResidueNotations(Connection conn) throws SQLException {
        SortedSet<String> ret = new TreeSet<>();
        try (final PreparedStatement ps = conn.prepareStatement(RESIDUE_NOTATIONS_QUERY)) {
            try (final ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ret.add(rs.getString("ELNOT"));
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ERROR RETRIEVING ELNOTS FOR PROCESSING", e);
            throw new RuntimeException(e);
        }
        if (PROCESSING_LZIPS) {
            cleanResidue(conn, "L0000");
            ret.add("L0001");
            ret.add("L0002");
            ret.add("L0003");
            ret.add("L0004");
            ret.add("L0005");
            ret.add("L00J1");
            ret.add("L00J2");
            ret.add("L00J3");
            ret.add("L00J4");
            ret.add("L00J5");

        }
        return ret;
    }

    void cleanResidueOLD(Connection conn, String notation) throws SQLException {
        LOG.log(Level.INFO, "clean residue old called for {0}", notation);
        // todo: I think this should be pulling from residue instead of intercepts?
        if (residueCountExceedsThreshold(conn, notation)) {
            // TODO: this whole section is dumbe; don't look first, use the query to delete!
            LOG.log(Level.FINE, "{0} residue exceeeded threshold", notation);
            final String query2
                    = "SELECT INTERCEPT_ID "
                    + "  FROM ENV_RESIDUE_INTS "
                    + "    JOIN INTERCEPTS "
                    + "      USING (INTERCEPT_ID) "
                    + "  WHERE R_TYPE = 'P' "
                    + "    AND ELNOT = ? "
                    + "    AND INT_UP_TIME < SYSDATE - ?";

            final String delete
                    = "DELETE FROM ENV_RESIDUE_INTS RI "
                    + "  WHERE R_TYPE = 'P'"
                    + "    AND INTERCEPT_ID = ?";

            LOG.log(Level.FINE, "fetching residue ids for {0}, older than {1} days", new Object[]{notation, RESIDUE_DAYS});
            try (final PreparedStatement ps = conn.prepareStatement(query2)) {
                ps.setString(1, notation);
                ps.setInt(2, RESIDUE_DAYS);
                try (ResultSet rs = ps.executeQuery()) {
                    try (final PreparedStatement ps2 = conn.prepareStatement(delete)) {
                        final boolean originalAutoCommit = conn.getAutoCommit();
                        conn.setAutoCommit(false);
                        while (rs.next()) {
                            final long interceptId = rs.getLong("INTERCEPT_ID");
                            ps2.setLong(1, interceptId);
                            LOG.log(Level.FINEST, "deleting intercept-id {0} from parametric residue", interceptId);
                            ps2.addBatch();
                        }
                        ps2.executeBatch();
                        conn.commit();
                        conn.setAutoCommit(originalAutoCommit);
                    }
                }
            } catch (SQLException e) {
                LOG.log(Level.SEVERE, "ERROR REMOVING RESIDUE FOR " + notation, e);
                throw new RuntimeException(e);
            }
        }
        LOG.log(Level.INFO, "clean residue old completed for {0}", notation);
    }

    void cleanResidue(Connection conn, String notation) throws SQLException {
        LOG.log(Level.INFO, "clean residue called for {0}", notation);
        if (residueCountExceedsThreshold(conn, notation)) {
            // TODO: this section is wrong, too; don't iterate over individual 
            // intercept-ids; just delete all the ones that match the query criteria
            String query2
                    = "SELECT INTERCEPT_ID "
                    + "  FROM ENV_RESIDUE_INTS "
                    + "    JOIN INTERCEPTS "
                    + "      USING (INTERCEPT_ID) "
                    + "  WHERE R_TYPE = 'P' "
                    + "    AND ELNOT = ? "
                    + "    AND INT_UP_TIME < SYSDATE - ?";
            String delete
                    = "DELETE "
                    + "  FROM ENV_RESIDUE_INTS "
                    + "  WHERE R_TYPE = 'P'"
                    + "    AND INTERCEPT_ID = ? ";
            try (final PreparedStatement ps2 = conn.prepareStatement(query2)) {
                ps2.setString(1, notation);
                ps2.setInt(2, RESIDUE_DAYS);
                try (ResultSet rs = ps2.executeQuery()) {
                    try (PreparedStatement ps3 = conn.prepareStatement(delete)) {
                        final boolean originalAutoCommit = conn.getAutoCommit();
                        conn.setAutoCommit(false);
                        while (rs.next()) {
                            final long interceptId = rs.getLong("INTERCEPT_ID");
                            ps3.setLong(1, interceptId);
                            ps3.addBatch();
                        }
                        ps3.executeBatch();
                        conn.commit();
                        conn.setAutoCommit(originalAutoCommit);
                    }
                }
            } catch (SQLException e) {
                LOG.log(Level.SEVERE, "ERROR REMOVING RESIDUE FOR " + notation, e);
                throw new RuntimeException(e);
            }
        }
        LOG.log(Level.INFO, "clean residue completed for {0}", notation);
    }

    private Date calculateMinimumResidueDate() {
        LocalDate minDate = LocalDate.now().minusDays(RESIDUE_DAYS);
        return Date.valueOf(minDate);
    }

    Collection<Signal> fetchResidue(Connection conn, String notation) throws SQLException {
        LOG.log(Level.INFO, "fetch residue called for {0}", notation);
        Set<Signal> ret = new HashSet<>();
        try (final PreparedStatement ps = conn.prepareStatement(FETCH_RESIDUE_QUERY)) {
            LOG.log(Level.INFO, "current fetch size: {0}", ps.getFetchSize());
            ps.setFetchSize(20000);
            LOG.log(Level.INFO, "current fetch size: {0}", ps.getFetchSize());
            int recordCount = 0;
            if (notation.startsWith("L")) {
                ps.setString(1, "L0000");
                ps.setString(2, "L0000");
            } else {
                ps.setString(1, notation);
                ps.setString(2, notation);
            }
            Date minDate = calculateMinimumResidueDate();
            ps.setDate(3, minDate);
            try (final ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    boolean add = true;
                    final Long id = rs.getLong("INTERCEPT_ID");
                    final Long mode = rs.getLong("MODE_ID");
                    final BigDecimal rf = rs.getBigDecimal("RF");
                    final String mt = rs.getString("MOD_TYPE");
                    final BigDecimal pd = rs.getBigDecimal("PD");
                    final BigDecimal sp = rs.getBigDecimal("SP");
                    final BigDecimal ir = rs.getBigDecimal("IR");
                    final String st = rs.getString("SCAN_TYPE");
                    final String adjustedModType = ModTypeAdjuster.getAdjustedModType(mt);
                    if (notation.startsWith("L")) {
                        String adjustedElnot = ElnotAdjuster.adjustedElnot("L0000", rf.doubleValue(), mt);
                        if (!notation.equals(adjustedElnot)) {
                            add = false;
                        }
                    }
                    if (add) {
                        BigDecimal[] pris = fetchPrisForIntercept(conn, id);
                        ret.add(new Signal(id, mode, rf, pd, sp, ir, st, pris, adjustedModType));
                    }
                    recordCount++;
                    if (recordCount % 5000 == 0) {
                        LOG.log(Level.FINE, "{0} records pulled for {1}", new Object[]{recordCount, notation});
                    }
                }
            }
        } catch(SQLException e) {
            LOG.log(Level.SEVERE, "ERROR RETREIVING INTERCEPTS FOR " +notation, e);
            throw new RuntimeException(e);
        }
        LOG.log(Level.INFO, "fetch residue completed for {0}", notation);
        return ret;
    }
}
