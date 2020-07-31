/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Brad
 */
public class RandomUtils extends Random {

    public RandomUtils() {
        super();
    }

    public RandomUtils(long seed) {
        super(seed);
    }

    public int nextInt(int min, int max) {
        return this.nextInt(max - min) + min;
    }

    public String randomAlphaNumeric(int stringLength) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        char[] array = new char[stringLength];
        for (int i = 0; i < array.length; i++) {
            array[i] = chars.charAt(nextInt(chars.length()));
        }
        return String.valueOf(array);
    }

    public List<Double> randomDoubleList(int length) {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(nextDouble());
        }
        return list;
    }

}
