/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Brad
 */
public class ViprDeltaStatsDAO {

    private static final Logger LOG = Logger.getLogger(ViprDeltaStatsDAO.class.getName());

    public void storeTimeStampForDelta(Connection conn, LocalDateTime startTime, LocalDateTime finishTime, String source) throws SQLException {
        final String query = "insert into vipr_delta_stats ( id, start_time, end_time, source ) values ( ?, ?, ?, ? )";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, 1); // should get this value based on DB contents (trigger? auto-gen?)
            ps.setTimestamp(2, Timestamp.valueOf(startTime));
            ps.setTimestamp(3, Timestamp.valueOf(finishTime));
            ps.setString(4, source);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                LOG.log(Level.SEVERE, "no rows affected when inserting into VIPR_DELTA_STATS");
                throw new SQLException("no rows affected when inserting into VIPR_DELTA_STATS");
            }
        }
    }
}
