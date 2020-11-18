/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.utils;

import java.math.BigDecimal;

/**
 *
 * @author Brad
 */
public class MathUtils {

    public static BigDecimal min(BigDecimal first, BigDecimal second) {
        return first.min(second);
    }
    
    public static BigDecimal max(BigDecimal first, BigDecimal second) {
        return first.max(second);
    }
}
