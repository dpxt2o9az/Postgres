/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.model;

import static mil.af.flagging.dataminer.utils.MathUtils.max;
import static mil.af.flagging.dataminer.utils.MathUtils.min;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Brad
 */
public class Ranges extends ArrayList<Range> {
    final OverlapChecker overlapChecker;
    final Consolidator consolidator;
    
    public Ranges() {
        overlapChecker = new OverlapChecker() {
            @Override
            public boolean overlaps(Range r1, Range r2) {
                return r1.overlaps(r2);
            }
        };
        
        consolidator = new StandardConsolidator();
    }
    
    public Ranges(BigDecimal slop) {
        overlapChecker = new OverlapChecker() {
            @Override 
            public boolean overlaps(Range r1, Range r2) {
                return r1.overlapsWithSlop(r2, slop);
            }
        };
        consolidator = new StandardConsolidator();
    }
    
    @Override
    public boolean add(Range r) {
        boolean result = super.add(r);
        consolidate();
        return result;
    }
    
    @Override
    public boolean addAll(int i, Collection<? extends Range> collection) {
        final boolean result = super.addAll(i, collection);
        consolidate();
        return result;
    }
    
    @Override
    public boolean addAll(Collection<? extends Range> collection) {
        final boolean result = super.addAll(collection);
        consolidate();
        return result;
    }
    
    public boolean contains(BigDecimal value) {
        for (Range r : this) {
            if (r.contains(value)) {
                return true;
            }
        }
        return false;
    }
    
    private void consolidate() {
        for (int z = this.size() -1; z>=1; z--) {
            for (int y = z-1; y>=0 ; y--) {
                if (overlapChecker.overlaps(this.get(z), this.get(y))) {
                    this.set(y, consolidator.consolidate(this.get(z), this.get(y)));
                    this.remove(z);
                    break;
                }
            }
        }
    }
    
    static interface OverlapChecker {
        public boolean overlaps(Range r1, Range r2);
    }
    
    static interface Consolidator {
        public Range consolidate(Range r1, Range r2);
    }
    
    static class StandardConsolidator implements Consolidator {

        @Override
        public Range consolidate(Range first, Range o) {
            return new Range(min(min(first.getMinValue(), first.getMaxValue()), min(o.getMinValue(), o.getMaxValue())),
            max(max(first.getMinValue(), first.getMaxValue()), max(o.getMinValue(), o.getMaxValue())));
        }
        
    }
}
