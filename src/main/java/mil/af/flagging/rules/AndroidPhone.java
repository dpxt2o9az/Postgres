
package mil.af.flagging.rules;

import static mil.af.flagging.rules.Phone.OSType.ANDROID;

public class AndroidPhone implements RuleI<Phone, Phone> {

    @Override
    public Phone process(Phone input) {
        input.setModel("Pixel 2");
        return input;
    }

    @Override
    public boolean matches(Phone input) {
        return input.getOsType().equals(ANDROID);
    }
    
}
