/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.model;

/**
 *
 * @author Brad
 */
public class TuningConstraint {
    
    public static class Builder {

        private final Parameter parm;
        private Double horizon;
        private Double increment;
        private Double threshold;
        private Double min;
        private Double max;

        public Builder(Parameter parm) {
            this.parm = parm;
        }

        public TuningConstraint build() {
            return new TuningConstraint(this.parm, this.increment, this.threshold, this.horizon, this.min, this.max);
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

        public Builder setMin(Double min) {
            this.min = min;
            return this;
        }

        public Builder setMax(Double max) {
            this.max = max;
            return this;
        }
    }

    public TuningConstraint() {
    }

    public TuningConstraint(Parameter parm, Double increment, Double threshold, Double horizon, Double min, Double max) {
        this.parm = parm;
        this.increment = increment;
        this.threshold = threshold;
        this.horizon = horizon;
        this.min = min;
        this.max = max;
    }
    public Parameter parm;
    public Double horizon;
    public Double increment;
    public Double threshold;
    public Double min;
    public Double max;
    
}
