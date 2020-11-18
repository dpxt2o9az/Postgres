/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import mil.af.flagging.dataminer.model.Signal;
import static mil.af.flagging.dataminer.db.Mapper.MappingException;

/**
 *
 * @author Brad
 */
public class SignalMapper implements Mapper<Signal> {

    @Override
    public Signal map(final ResultSet rs) throws SQLException, MappingException {
        Signal s = new Signal();
        s.id = rs.getLong("INTERCEPT_ID");
        if (rs.wasNull()) {
            throw new MappingException("ID cannot be null");
        }
        s.elnot = rs.getString("ELNOT");
        s.rf = rs.getDouble("RF");
        if (rs.wasNull()) {
            s.rf = null;
        }
        s.pd = rs.getDouble("PD");
        if (rs.wasNull()) {
            s.pd = null;
        }
        s.sp = rs.getDouble("SP");
        if (rs.wasNull()) {
            s.sp = null;
        }
        s.ir = rs.getDouble("IR");
        if (rs.wasNull()) {
            s.ir = null;
        }
        s.mt = rs.getString("MOD_TYPE");
        s.st = rs.getString("SCAN_TYPE");
        return s;
    }
}
