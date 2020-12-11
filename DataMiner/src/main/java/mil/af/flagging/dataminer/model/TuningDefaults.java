/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.model;

import java.math.BigDecimal;

/**
 *
 * @author Brad
 */
public enum TuningDefaults {
    RF_EXTRACT_INCR(new BigDecimal("0.1")),
    RF_THRESH(new BigDecimal("0.5")),
    RF_HORIZON(new BigDecimal("50.0")),
    PD_EXTRACT_INCR(new BigDecimal("0.1")),
    PD_THRESH(new BigDecimal("0.5")),
    PD_HORIZON(new BigDecimal("10.0")),
    SP_EXTRACT_INCR(new BigDecimal("0.1")),
    SP_THRESH(new BigDecimal("0.5")),
    SP_HORIZON(new BigDecimal("10.0")),
    IR_EXTRACT_INCR(new BigDecimal("0.1")),
    IR_THRESH(new BigDecimal("0.5")),
    IR_HORIZON(new BigDecimal("10.0")),
    PRI_EXTRACT_INCR(new BigDecimal("0.1")),
    PRI_THRESH(new BigDecimal("0.5")),
    PRI_HORIZON(new BigDecimal("10.0")),
    JPR_EXTRACT_INCR(new BigDecimal("0.1")),
    JPR_THRESH(new BigDecimal("0.5")),
    JPR_HORIZON(new BigDecimal("10.0")),
    ;
    
    public final BigDecimal val;
    
    TuningDefaults(BigDecimal val) {
        this.val = val;
    }
    
}
