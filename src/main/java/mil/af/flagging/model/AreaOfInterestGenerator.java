/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import mil.af.flagging.aoi.AreaOfInterest;

/**
 *
 * @author Brad
 */
public class AreaOfInterestGenerator {

    RandomUtils rng = new RandomUtils();

    private final List<Country> countries;

    public AreaOfInterestGenerator(Collection<Country> countries) {
        this.countries = new ArrayList<>(countries);
    }

    public Collection<AreaOfInterest> generateAOIs(int count) {
        Set<AreaOfInterest> aois = new TreeSet<>();
        for (int i = 0; i < count; i++) {
            String code = rng.randomAlphaNumeric(2);
            String desc = code + " description";
            AreaOfInterest aoi = new AreaOfInterest(code, desc);
            for (int j = 0; j < rng.nextInt(5, countries.size()); j++) {
                Country c = countries.get(rng.nextInt(countries.size()));
                aoi.countryCodes.add(c.countryCode);
            }
            aois.add(aoi);
        }
        return aois;
    }

}
