/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.model;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Brad
 */
public class CountryGenerator {

    final RandomUtils rng = new RandomUtils();

    public CountryGenerator() {
    }

    public Collection<Country> generateCountries(int count) {
        Set<Country> countries = new TreeSet<>();
        for (int i = 0; i < count; i++) {
            String cc = rng.randomAlphaNumeric(2);
            String d = cc + " description";
            Country c = new Country(cc, d);
            countries.add(c);
        }
        return countries;
    }
}
