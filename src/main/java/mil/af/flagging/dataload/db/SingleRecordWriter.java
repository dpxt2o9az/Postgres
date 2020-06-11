package mil.af.flagging.dataload.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mil.af.flagging.model.Intercept;

public class SingleRecordWriter {

    public static final String PARENT_RECORD_INSERTION
            = "INSERT INTO intercept"
            + " ( intercept_id, wrangler_id, elnot, mod_type, scan_type, scan_period, time_process, int_up_time, int_down_time, latitude, longitude, major, minor, orientation ) "
            + "   values "
            + " ( DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
    public static final  String RF_RECORD_INSERTION
            = "INSERT INTO intercept_rfs "
            + "  ( intercept_id, sequence, value ) "
            + "     values "
            + "   ( ?, ?, ? )";
    public static final  String PRI_RECORD_INSERTION
            = "INSERT INTO intercept_pris "
            + "  ( intercept_id, sequence, value ) "
            + "     values "
            + "   ( ?, ?, ? )";
    public static final  String PD_RECORD_INSERTION
            = "INSERT INTO intercept_pds "
            + "  ( intercept_id, sequence, value ) "
            + "     values "
            + "   ( ?, ?, ? )";

    private final Connection conn;
    private PreparedStatement iPs;
    private PreparedStatement rfPs;
    private PreparedStatement priPs;
    private PreparedStatement pdPs;

    public SingleRecordWriter(Connection c) throws SQLException {
        this.conn = c;
        initPreparedStatements();
    }

    private void initPreparedStatements() throws SQLException {
        this.iPs = conn.prepareStatement(PARENT_RECORD_INSERTION, new String[]{"intercept_id"});
        this.rfPs = conn.prepareStatement(RF_RECORD_INSERTION);
        this.priPs = conn.prepareStatement(PRI_RECORD_INSERTION);
        this.pdPs = conn.prepareStatement(PD_RECORD_INSERTION);
    }

    public Long writeRecord(Intercept i) throws SQLException {
        Long key = null;
        int col = 1;
        iPs.setString(col++, i.getWranglerId());
        iPs.setString(col++, i.getElnot());
        int affected = iPs.executeUpdate();
        try (ResultSet rs = iPs.getGeneratedKeys()) {
            if (rs.next()) {
                key = rs.getLong(1);
                i.setInterceptId(key);
                for (int index = 0; index < i.getRfs().size(); index++) {
                    col = 1;
                    rfPs.setLong(col++, i.getInterceptId());
                    rfPs.setInt(col++, index);
                    rfPs.setDouble(col++, i.getRfs().get(index));
                    rfPs.executeUpdate();
                }
                for (int index = 0; index < i.getPris().size(); index++) {
                    col = 1;
                    priPs.setLong(col++, i.getInterceptId());
                    priPs.setInt(col++, index);
                    priPs.setDouble(col++, i.getPris().get(index));
                    priPs.executeUpdate();
                }
                for (int index = 0; index < i.getPds().size(); index++) {
                    col = 1;
                    pdPs.setLong(col++, i.getInterceptId());
                    pdPs.setInt(col++, index);
                    pdPs.setDouble(col++, i.getPds().get(index));
                    pdPs.executeUpdate();
                }

            }
        }
        return key;
    }
}
