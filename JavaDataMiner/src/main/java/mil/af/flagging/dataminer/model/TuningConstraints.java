/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Brad
 */
public class TuningConstraints extends HashMap<Parameter, TuningConstraint> {

    private static final Map<Parameter, TuningConstraint> DEFAULT_CONSTRAINTS = defaultTuningConstraints();

    private static Map<Parameter, TuningConstraint> defaultTuningConstraints() {
        Map<Parameter, TuningConstraint> temp = new HashMap<>();
        temp.put(Parameter.RF, new TuningConstraint(Parameter.RF, 0.1, 0.5, 50.0, null, null));
        temp.put(Parameter.PD, new TuningConstraint(Parameter.PD, 0.1, 0.5, 10.0, null, null));
        temp.put(Parameter.SP, new TuningConstraint(Parameter.SP, 0.1, 0.5, 10.0, null, null));
        temp.put(Parameter.IR, new TuningConstraint(Parameter.IR, 0.1, 0.5, 10.0, null, null));
        temp.put(Parameter.PRI, new TuningConstraint(Parameter.PRI, 0.1, 0.5, 10.0, null, null));
        temp.put(Parameter.JPR, new TuningConstraint(Parameter.JPR, 0.1, 0.5, 10.0, null, null));
        return Collections.unmodifiableMap(temp);
    }


    public TuningConstraints() {
        this.putAll(DEFAULT_CONSTRAINTS);
    }

}
