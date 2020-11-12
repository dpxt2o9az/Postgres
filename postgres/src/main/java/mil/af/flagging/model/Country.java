/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Brad
 */
@Entity
public class Country implements Comparable<Country> {

    @Id
    public int id;
    public String countryCode;
    public String description;

    public Country() {
        
    }
    
    public Country(String countryCode, String description) {
        this.countryCode = countryCode;
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.countryCode);
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
        final Country other = (Country) obj;
        if (!Objects.equals(this.countryCode, other.countryCode)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Country o) {
        return this.countryCode.compareTo(o.countryCode);
    }

}
