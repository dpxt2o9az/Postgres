/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;
import mil.af.flagging.dataminer.model.EnvModeMap;
import mil.af.flagging.dataminer.model.EnvRange;
import mil.af.flagging.dataminer.model.PriSequence;
import mil.af.flagging.dataminer.model.TuningConstraints;

/**
 *
 * @author Brad
 */
public class ModeDAO {

    private static final Logger LOG = Logger.getLogger(ModeDAO.class.getName());
    
    public Collection<PriSequence> buildEnvSequences(Connection conn, String notation, Collection<EnvRange> pris, Collection<EnvRange> jprs) throws SQLException {
        return Collections.EMPTY_LIST;
    }
    
    public Collection<EnvModeMap> buildEnvModeMaps(Connection conn, String notation, Collection<PriSequence> sequences) throws SQLException {
        return Collections.EMPTY_LIST;
    }

    public void fillMap(Connection conn, EnvModeMap map, Map<TuningConstraints.Parameter, Collection<EnvRange>> envRanges) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
