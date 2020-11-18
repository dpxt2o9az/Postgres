/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.model;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Brad
 */
public class TuningConstraints extends HashMap<TuningConstraints.Parameter, TuningConstraints.TuningConstraint> {

    private static final Map<Parameter, TuningConstraint> DEFAULT_CONSTRAINTS = defaultTuningConstraints();

    private static Map<Parameter, TuningConstraint> defaultTuningConstraints() {
        Map<Parameter, TuningConstraint> temp = new HashMap<>();
        temp.put(Parameter.RF, new TuningConstraint(Parameter.RF, 0.1, 0.5, 50.0));
        temp.put(Parameter.PD, new TuningConstraint(Parameter.PD, 0.1, 0.5, 10.0));
        temp.put(Parameter.SP, new TuningConstraint(Parameter.SP, 0.1, 0.5, 10.0));
        temp.put(Parameter.IR, new TuningConstraint(Parameter.IR, 0.1, 0.5, 10.0));
        temp.put(Parameter.PRI, new TuningConstraint(Parameter.PRI, 0.1, 0.5, 10.0));
        temp.put(Parameter.JPR, new TuningConstraint(Parameter.JPR, 0.1, 0.5, 10.0));
        return Collections.unmodifiableMap(temp);
    }

    public static enum Parameter {
        RF, PD, SP, IR, PRI, JPR;
    }

    public TuningConstraints() {
        this.putAll(defaultTuningConstraints());
    }

    public static class TuningConstraint {

        public static class Builder {

            final private Parameter parm;
            private Double horizon;
            private Double increment;
            private Double threshold;

            public Builder(Parameter parm) {
                this.parm = parm;
            }

            public TuningConstraint build() {
                return new TuningConstraint(this.parm, this.increment, this.threshold, this.horizon);
            }

            public Builder setHorizon(Double horizon) {
                this.horizon = horizon;
                return this;
            }

            public Builder setIncrement(Double increment) {
                this.increment = increment;
                return this;
            }

            public Builder setThreshold(Double threshold) {
                this.threshold = threshold;
                return this;
            }
        }

        public TuningConstraint() {
        }

        public TuningConstraint(Parameter parm, Double increment, Double threshold, Double horizon) {
            this.parm = parm;
            this.increment = increment;
            this.threshold = threshold;
            this.horizon = horizon;
        }

        public Parameter parm;
        public Double horizon;
        public Double increment;
        public Double threshold;

    }
}
