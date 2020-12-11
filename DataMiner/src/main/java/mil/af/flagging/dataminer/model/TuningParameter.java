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
public class TuningParameter {

    public TuningParameter applyParmsFrom(TuningParameter tp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static class Builder {

        private BigDecimal horizon;
        private BigDecimal increment;
        private BigDecimal threshold;
        private BigDecimal min;
        private BigDecimal max;

        public Builder() {
        }

        public TuningParameter build() {
            return new TuningParameter(this.increment, this.threshold, this.horizon, this.min, this.max);
        }

        public Builder setHorizon(BigDecimal horizon) {
            this.horizon = horizon;
            return this;
        }

        public Builder setIncrement(BigDecimal increment) {
            this.increment = increment;
            return this;
        }

        public Builder setThreshold(BigDecimal threshold) {
            this.threshold = threshold;
            return this;
        }
        
        public Builder setMin(BigDecimal min) {
            this.min = min;
            return this;
        }
        
        public Builder setMax(BigDecimal max) {
            this.max = max;
            return this;
        }
    }

    public TuningParameter() {
    }

    public TuningParameter(BigDecimal increment, BigDecimal threshold, BigDecimal horizon) {
        this(increment, threshold, horizon, null, null);
    }

    public TuningParameter(BigDecimal horizon, BigDecimal increment, BigDecimal threshold, BigDecimal min, BigDecimal max) {
        this.horizon = horizon;
        this.increment = increment;
        this.threshold = threshold;
        this.min = min;
        this.max = max;
    }

    public BigDecimal horizon;
    public BigDecimal increment;
    public BigDecimal threshold;
    public BigDecimal min;
    public BigDecimal max;

}
