
package dataminer;

import java.math.BigDecimal;
import java.util.Objects;

public class Slope {

    public final BigDecimal parm_value;
    public final BigDecimal slope;

    public Slope(BigDecimal parmValue, BigDecimal slope) {
        this.parm_value = parmValue;
        this.slope = slope;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.parm_value);
        hash = 43 * hash + Objects.hashCode(this.slope);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Slope other = (Slope) obj;
        if (!Objects.equals(this.parm_value, other.parm_value)) {
            return false;
        }
        if (!Objects.equals(this.slope, other.slope)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "Slope{parm_value=" + parm_value + ", slope=" + slope + "}";
    }
}
