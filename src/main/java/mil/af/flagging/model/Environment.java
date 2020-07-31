/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Brad
 */
public class Environment {

    public List<ModulationType> modTypes;
    public List<String> elnots;
    public List<String> readoutStations;
    public List<String> scanTypes;
    public List<Country> countries;

    private Environment() {
        modTypes = new ArrayList<>();
        elnots = new ArrayList<>();
        readoutStations = new ArrayList<>();
        scanTypes = new ArrayList<>();
        countries = new ArrayList<>();
    }

    public static Environment randomEnvironment() {
        return Environment.randomEnvironment(System.currentTimeMillis());
    }

    public static Environment randomEnvironment(long seed) {
        RandomUtils r = new RandomUtils(seed);
        Environment e = new Environment();
        for (int i = 0; i < r.nextInt(20, 50); i++) {
            String mt = r.randomAlphaNumeric(2);

            e.modTypes.add(new ModulationType(mt, mt));
        }

        for (int i = 0; i < r.nextInt(25, 50); i++) {
            String elnot = r.randomAlphaNumeric(5);
            e.elnots.add(elnot);
        }

        for (int i = 0; i < r.nextInt(10, 20); i++) {
            String station = r.randomAlphaNumeric(2);
            e.readoutStations.add(station);
        }

        CountryGenerator g = new CountryGenerator();
        e.countries = new ArrayList<>(g.generateCountries(r.nextInt(50, 80)));
        return e;
    }

}
