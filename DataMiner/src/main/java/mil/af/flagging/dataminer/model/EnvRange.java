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
public class EnvRange extends Range {

    public static final EnvRange ZERO = new EnvRange(0L);

    public Long id;
    public BigDecimal xMin;
    public BigDecimal xMax;

    public EnvRange(Long id, BigDecimal parmMin, BigDecimal parmMax, BigDecimal xMin, BigDecimal xMax) {
        super(parmMin, parmMax);
        this.id = id;
        this.xMin = xMin;
        this.xMax = xMax;
    }

    public EnvRange(Long id, BigDecimal parmMin, BigDecimal parmMax) {
        this(id, parmMin, parmMax, null, null);
    }

    public EnvRange(Long newId, EnvRange range) {
        this(newId, range.getMinValue(), range.getMaxValue(), range.xMin, range.xMax);
    }

    public EnvRange(Long id) {
        this(id, null, null, null, null);
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getXMIN() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public BigDecimal getXMAX() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
