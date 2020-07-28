/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.rules;

/**
 *
 * @author Brad
 */
public interface RuleI<I, O> {
    boolean matches(I input);
    O process(I input);
}
