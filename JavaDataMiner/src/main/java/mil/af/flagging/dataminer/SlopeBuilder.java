
package mil.af.flagging.dataminer;

import mil.af.flagging.dataminer.model.Slope;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class SlopeBuilder implements ISlopeBuilder {

    private static final Logger LOG = Logger.getLogger(SlopeBuilder.class.getName());
    
    private final List<BigDecimal> input;
    private final BigDecimal delta_x;
    
    public SlopeBuilder(List<BigDecimal> input, BigDecimal delta_x) {
        this.input = Collections.unmodifiableList(input);
        this.delta_x = delta_x;
    }
    
    @Override
    public List<Slope> generateSlopes() {
        List<Slope> slopes = new ArrayList<>();
        
        BigDecimal start_value = null;
        
        BigDecimal N = BigDecimal.ZERO;
        
        for (BigDecimal present_value : input) {
            if (start_value == null) {
                start_value = present_value;
            }
            
//            LOG.log(Level.INFO, "present_value: {0}, start_value: {1} ({2})", new Object[]{present_value, start_value, start_value.add(delta_x)});
            
            if (present_value.compareTo(start_value.add(delta_x))< 0) {
                N = N.add(BigDecimal.ONE);
            } else {
                BigDecimal slope = N.divide(delta_x, RoundingMode.FLOOR);
                slopes.add(new Slope(start_value, slope));
                start_value = start_value.add(delta_x);
                N = BigDecimal.ONE;
                while(start_value.compareTo(present_value.subtract(delta_x))<0) {
                    slopes.add(new Slope(start_value, BigDecimal.ZERO));
                    start_value = start_value.add(delta_x);
                }
            }
        }
        
        if (N.compareTo(BigDecimal.ZERO)> 0) {
            BigDecimal slope = N.divide(delta_x);
            slopes.add(new Slope(start_value, slope));
        }
        
        return slopes;
    }
}
