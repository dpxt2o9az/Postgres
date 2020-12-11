/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Brad
 */
public class TuningParameters extends HashMap<String, TuningParameter> {

    public static final Map<String, TuningParameter> DEFAULT_CONSTRAINTS = defaultTuningParametersFor();
    public static final BigDecimal DEFAULT_INCR = new BigDecimal("0.1");
    public static final BigDecimal DEFAULT_THRESH = new BigDecimal("0.5");
    public static final BigDecimal DEFAULT_RF_HORIZON = new BigDecimal("50.0");
    public static final BigDecimal DEFAULT_HORIZON = new BigDecimal("10.0");

    private static Map<String, TuningParameter> defaultTuningParametersFor() {
        Map<String, TuningParameter> temp = new HashMap<>();
        temp.put("RF", new TuningParameter(DEFAULT_INCR, DEFAULT_THRESH, DEFAULT_RF_HORIZON));
        temp.put("PD", new TuningParameter(DEFAULT_INCR, DEFAULT_THRESH, DEFAULT_HORIZON));
        temp.put("SP", new TuningParameter(DEFAULT_INCR, DEFAULT_THRESH, DEFAULT_HORIZON));
        temp.put("IR", new TuningParameter(DEFAULT_INCR, DEFAULT_THRESH, DEFAULT_HORIZON));
        temp.put("PRI", new TuningParameter(DEFAULT_INCR, DEFAULT_THRESH, DEFAULT_HORIZON));
        temp.put("JPR", new TuningParameter(DEFAULT_INCR, DEFAULT_THRESH, DEFAULT_HORIZON));
        return Collections.unmodifiableMap(temp);
    }

    public TuningParameters() {
        this.putAll(DEFAULT_CONSTRAINTS);
    }

}
