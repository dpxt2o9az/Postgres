/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload.db;

import mil.af.flagging.db.Result;
import mil.af.flagging.db.JDBCUtil;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import mil.af.flagging.model.*;

/**
 *
 * @author Brad
 */
public class ByRecordDataLoadInterceptDAO extends AbstractDataloadDAO {

    private static final Logger LOG = Logger.getLogger(ByRecordDataLoadInterceptDAO.class.getName());
    private static final String INSERT_AOI_REQ = "insert into idb_states ( intercept_id, flow_control, last_modified ) values ( ?, 'AOI-REQ', DEFAULT )";

    private final Connection conn;
    private final PreparedStatement icptPs;
    private final PreparedStatement rfPs;
    private final PreparedStatement priPs;
    private final PreparedStatement pdPs;
    private final PreparedStatement statePs;

    public ByRecordDataLoadInterceptDAO(DataSource ds) throws SQLException {
        super(ds);
        conn = ds.getConnection();
        icptPs = conn.prepareStatement(SingleRecordWriter.PARENT_RECORD_INSERTION, new String[]{"intercept_id"});
        rfPs = conn.prepareStatement(SingleRecordWriter.RF_RECORD_INSERTION);
        priPs = conn.prepareStatement(SingleRecordWriter.PRI_RECORD_INSERTION);
        pdPs = conn.prepareStatement(SingleRecordWriter.PD_RECORD_INSERTION);
        statePs = conn.prepareStatement(INSERT_AOI_REQ);
    }

    @Override
    public void close() {
        JDBCUtil.closeQuietly(pdPs);
        JDBCUtil.closeQuietly(priPs);
        JDBCUtil.closeQuietly(rfPs);
        JDBCUtil.closeQuietly(icptPs);
        JDBCUtil.closeQuietly(conn);
    }

    @Override
    public Result storeNewIntercept(Intercept icpt) throws SQLException {
        List<Intercept> standInList = new ArrayList<>();
        standInList.add(icpt);
        return storeNewIntercepts(standInList).get(icpt);
    }

    @Override
    public Map<Intercept, Result> storeNewIntercepts(Collection<Intercept> icpts) throws SQLException {
        Map<Intercept, Result> results = new HashMap<>();
        boolean originalAutoCommit;
        originalAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        for (Intercept i : icpts) {
            int col = 1;
            icptPs.setString(col++, i.getWranglerId());
            icptPs.setString(col++, i.getElnot());
            icptPs.setString(col++, i.getModType());
            icptPs.setString(col++, i.getScanType());
            icptPs.setDouble(col++, i.getScanPeriod());
            icptPs.setTimestamp(col++, sqlTimestampFrom(i.getTimeProcessed()));
            icptPs.setTimestamp(col++, sqlTimestampFrom(i.getIntUpTime()));
            icptPs.setTimestamp(col++, sqlTimestampFrom(i.getIntDownTime()));
            icptPs.setString(col++, i.getCountryCode());
            icptPs.setDouble(col++, i.getLatitude());
            icptPs.setDouble(col++, i.getLongitude());
            icptPs.setDouble(col++, i.getMajor());
            icptPs.setDouble(col++, i.getMinor());
            icptPs.setDouble(col++, i.getOrientation());
            icptPs.executeUpdate();
            try (ResultSet rs = icptPs.getGeneratedKeys()) {
                if (rs.next()) {
                    i.setInterceptId(rs.getLong(1));
                } else {
                    LOG.log(Level.WARNING, "failed to insert wrangler-id {0}; probable duplicate id", i.getWranglerId());
                    results.put(i, Result.CONSTRAINT_FAILURE);
                    continue;
                }
            }
            for (int idx = 0; idx < i.getRfs().size(); idx++) {
                rfPs.setLong(1, i.getInterceptId());
                rfPs.setInt(2, idx + 1);
                rfPs.setDouble(3, i.getRfs().get(idx));
                rfPs.addBatch();
            }
            rfPs.executeBatch();
            for (int idx = 0; idx < i.getPris().size(); idx++) {
                priPs.setLong(1, i.getInterceptId());
                priPs.setInt(2, idx + 1);
                priPs.setDouble(3, i.getPris().get(idx));
                priPs.addBatch();
            }
            priPs.executeBatch();
            for (int idx = 0; idx < i.getPds().size(); idx++) {
                pdPs.setLong(1, i.getInterceptId());
                pdPs.setInt(2, idx + 1);
                pdPs.setDouble(3, i.getPds().get(idx));
                pdPs.addBatch();
            }
            pdPs.executeBatch();

            statePs.setLong(1, i.getInterceptId());
            statePs.executeUpdate();
            
            results.put(i, Result.SUCCESS);
        }

        conn.commit();

        conn.setAutoCommit(originalAutoCommit);
        return results;
    }

    private Timestamp sqlTimestampFrom(final Date timeProcessed) {
        final long longTime = timeProcessed.getTime();
        final Timestamp timestamp = new java.sql.Timestamp(longTime);
        return timestamp;
    }

}
