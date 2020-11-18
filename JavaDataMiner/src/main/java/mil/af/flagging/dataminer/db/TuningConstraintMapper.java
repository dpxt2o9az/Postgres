/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import mil.af.flagging.dataminer.model.TuningConstraints;
import mil.af.flagging.dataminer.model.TuningConstraints.Parameter;
import mil.af.flagging.dataminer.model.TuningConstraints.TuningConstraint;

/**
 *
 * @author Brad
 */
public class TuningConstraintMapper implements Mapper<TuningConstraint> {

    @Override
    public TuningConstraint map(ResultSet rs) throws SQLException, MappingException {
        Parameter parm = Parameter.valueOf(rs.getString("parm"));
        
        Double increment = rs.getDouble("increment");
        if (rs.wasNull()) {
            increment = null;
            throw new MappingException("increment cannot be null");
        }
        
        Double horizon = rs.getDouble("horizon");
        if (rs.wasNull()) {
            horizon = null;
            throw new MappingException("horizon cannot be null");
        }
        
        Double threshold = rs.getDouble("threshold");
        if (rs.wasNull()) {
            threshold = null;
            throw new MappingException("threshold cannot be null");
        }

        return new TuningConstraints.TuningConstraint.Builder(parm)
                .setIncrement(increment)
                .setThreshold(threshold)
                .setHorizon(horizon)
                .build();

    }

}
