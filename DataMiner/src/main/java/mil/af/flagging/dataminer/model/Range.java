/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.model;

import static mil.af.flagging.dataminer.utils.MathUtils.max;
import static mil.af.flagging.dataminer.utils.MathUtils.min;
import java.math.BigDecimal;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 *
 * @author Brad
 */
public class Range implements Comparable<Range> {

    private final ObjectProperty<BigDecimal> minValueProperty;
    private final ObjectProperty<BigDecimal> maxValueProperty;

    public Range(BigDecimal first, BigDecimal second) {
        this.minValueProperty = new ReadOnlyObjectWrapper<>(first);
        this.maxValueProperty = new ReadOnlyObjectWrapper<>(second);
    }

    public ObjectProperty<BigDecimal> minValueProperty() {
        return minValueProperty;
    }

    public ObjectProperty<BigDecimal> maxValueProperty() {
        return maxValueProperty;
    }

    public Range normalize() {
        return new Range(min(this.getMinValue(), this.getMaxValue()), max(this.getMinValue(), this.getMaxValue()));
    }

    public boolean contains(BigDecimal value) {
        return this.getMinValue().compareTo(value) <= 0 && value.compareTo(this.getMaxValue()) <= 0;
    }

    public boolean containsRange(Range o) {
        return this.getMinValue().compareTo(o.getMinValue()) <= 0 && o.getMinValue().compareTo(this.getMaxValue()) <= 0
                && this.getMinValue().compareTo(o.getMaxValue()) <= 0 && o.getMaxValue().compareTo(this.getMaxValue()) <= 0;
    }

    public boolean overlaps(Range o) {
        if (max(this.getMinValue(), this.getMaxValue()).compareTo(max(o.getMinValue(), o.getMaxValue())) > 0) {
            // max(o) >= min(this) ?
            return max(o.getMinValue(), o.getMaxValue()).compareTo(min(this.getMinValue(), this.getMaxValue())) >= 0;

        } else {
            return max(this.getMinValue(), this.getMaxValue()).compareTo(min(o.getMinValue(), o.getMaxValue())) >= 0;
        }
    }

    public BigDecimal getMinValue() {
        return minValueProperty.get();
    }

    public BigDecimal getMaxValue() {
        return maxValueProperty.get();
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + getMinValue() +"," + getMaxValue() + "}";
    }
    
    public boolean overlapsWithSlop(Range o, BigDecimal slop) {
        Range sloppyUpper = new Range(this.getMinValue(), this.getMaxValue().add(slop));
        Range sloppyLower = new Range(this.getMinValue().subtract(slop), this.getMaxValue());
        return sloppyUpper.overlaps(o) || sloppyLower.overlaps(o);
    }
    
    @Override
    public int compareTo(Range that) {
        return this.getMinValue().compareTo(that.getMinValue());
        
    }
}
