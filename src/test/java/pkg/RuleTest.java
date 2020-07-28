/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import mil.af.flagging.model.Intercept;
import org.junit.Test;

/**
 *
 * @author Brad
 */
public class RuleTest {
    @Test
    public void testRule() {
//        RuleBase rule = (new InterceptRule.Builder()).withElnot("xxxxx").build();
        Intercept i = new Intercept();
        i.setElnot("xxxxx");
//        assertTrue(rule.shouldFire(i));
    }
}
