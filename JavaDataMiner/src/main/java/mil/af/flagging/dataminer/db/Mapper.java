/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Brad
 */
public interface Mapper<T> {
    T map(ResultSet rs) throws SQLException, MappingException;
    
    public static class MappingException extends Exception {

        public MappingException() {
        }

        public MappingException(String string) {
            super(string);
        }
        
        public MappingException(String string, Throwable thrwbl) {
            super(string, thrwbl);
        }

        public MappingException(Throwable thrwbl) {
            super(thrwbl);
        }

        public MappingException(String string, Throwable thrwbl, boolean bln, boolean bln1) {
            super(string, thrwbl, bln, bln1);
        }
        
    }
}
