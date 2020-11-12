/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.model;

import java.util.Objects;

/**
 *
 * @author Brad
 */
public class ModulationType {

    public String modType;
    public String description;

    public ModulationType(String modType, String description) {
        this.modType = modType;
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.modType);
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
        final ModulationType other = (ModulationType) obj;
        if (!Objects.equals(this.modType, other.modType)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ModulationType{modType:" + modType + ",description:" + description + "}";
    }
}
