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
public class WindowsPhone implements RuleI<Phone, Phone> {

    @Override
    public boolean matches(Phone input) {
        return input.getOsType().equals(Phone.OSType.WINDOWS);
    }

    @Override
    public Phone process(Phone input) {
        input.setModel("Nokia 920");
        return input;
    }

}
