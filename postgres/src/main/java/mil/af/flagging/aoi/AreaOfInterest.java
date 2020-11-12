/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.aoi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Brad
 */
public class AreaOfInterest implements Comparable<AreaOfInterest> {

    public final String aoiCode;
    public final String description;

    public final List<String> countryCodes;

    public AreaOfInterest(String aoiCode, String description) {
        this.aoiCode = aoiCode;
        this.description = description;
        countryCodes = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.aoiCode);
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
        final AreaOfInterest other = (AreaOfInterest) obj;
        if (!Objects.equals(this.aoiCode, other.aoiCode)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(AreaOfInterest o) {
        return this.aoiCode.compareTo(o.aoiCode);
    }
    
}
