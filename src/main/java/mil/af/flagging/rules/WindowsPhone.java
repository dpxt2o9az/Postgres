package mil.af.flagging.rules;

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
