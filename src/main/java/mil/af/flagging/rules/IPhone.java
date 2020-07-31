
package mil.af.flagging.rules;

import static mil.af.flagging.rules.Phone.OSType.IOS;

public class IPhone implements RuleI<Phone, Phone>{

    @Override
    public Phone process(Phone input) {
        input.setModel("iPhone X");
        return input;
    }

    @Override
    public boolean matches(Phone input) {
        return input.getOsType().equals(IOS);
    }
    
}
