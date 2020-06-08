/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import mil.af.flagging.model.Intercept;
import mil.af.flagging.model.SingleRecordWriter;

/**
 *
 * @author Brad
 */
public class ByRecordDataLoadInterceptDAO extends AbstractDataloadDAO {

    public ByRecordDataLoadInterceptDAO(DataSource ds) {
        super(ds);
    }

    @Override
    public Collection<Result> storeNewIntercept(Intercept icpt) throws SQLException {
        List<Intercept> standInList = new ArrayList<>();
        standInList.add(icpt);
        return storeNewIntercepts(standInList);
    }

    @Override
    public Collection<Result> storeNewIntercepts(Collection<Intercept> icpts) throws SQLException {
        Collection<Result> results = new ArrayList<>();
        boolean originalAutoCommit;
        try (Connection conn = ds.getConnection()) {
            originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement icptPs = conn.prepareStatement(SingleRecordWriter.PARENT_RECORD_INSERTION, new String[]{"intercept_id"});
                    PreparedStatement rfPs = conn.prepareStatement(SingleRecordWriter.RF_RECORD_INSERTION);
                    PreparedStatement priPs = conn.prepareStatement(SingleRecordWriter.PRI_RECORD_INSERTION);
                    PreparedStatement pdPs = conn.prepareStatement(SingleRecordWriter.PD_RECORD_INSERTION)) {
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
                    icptPs.setDouble(col++, i.getLatitude());
                    icptPs.setDouble(col++, i.getLongitude());
                    icptPs.setDouble(col++, i.getMajor());
                    icptPs.setDouble(col++, i.getMinor());
                    icptPs.setDouble(col++, i.getOrientation());
                    icptPs.executeUpdate();
                    try (ResultSet rs = icptPs.getGeneratedKeys()) {
                        if (rs.next()) {
                            i.setInterceptId(rs.getInt(1));
                        } else {
                            throw new SQLException("no generated keys");
                        }
                    }
                    for (int idx = 0; idx < i.getRfs().size(); idx++) {
                        rfPs.setInt(1, i.getInterceptId());
                        rfPs.setInt(2, idx + 1);
                        rfPs.setDouble(3, i.getRfs().get(idx));
                        rfPs.addBatch();
                    }
                    rfPs.executeBatch();
                    for (int idx = 0; idx < i.getPris().size(); idx++) {
                        priPs.setInt(1, i.getInterceptId());
                        priPs.setInt(2, idx + 1);
                        priPs.setDouble(3, i.getPris().get(idx));
                        priPs.addBatch();
                    }
                    priPs.executeBatch();
                    for (int idx = 0; idx < i.getPds().size(); idx++) {
                        pdPs.setInt(1, i.getInterceptId());
                        pdPs.setInt(2, idx + 1);
                        pdPs.setDouble(3, i.getPds().get(idx));
                        pdPs.addBatch();
                    }
                    pdPs.executeBatch();
                    
                }
                results.add(Result.SUCCESS);
            }

            conn.commit();
            conn.setAutoCommit(originalAutoCommit);
        }
        return results;
    }

    private Timestamp sqlTimestampFrom(final Date timeProcessed) {
        final long longTime = timeProcessed.getTime();
        final Timestamp timestamp = new java.sql.Timestamp(longTime);
        return timestamp;
    }

}
