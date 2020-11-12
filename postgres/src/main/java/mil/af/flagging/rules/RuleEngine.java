
package mil.af.flagging.rules;

import java.util.ArrayList;
import java.util.List;

public class RuleEngine {

    List<RuleI<Phone, Phone>> rules;

    public RuleEngine() {
        rules = new ArrayList<>();
    }

    public RuleEngine registerRule(RuleI<Phone, Phone> rule) {
        rules.add(rule);
        return this;
    }
    
    public Phone rule(Phone phone) {
        return rules.stream().filter(rule -> rule.matches(phone))
                .map(rule -> (Phone) rule.process(phone))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no matching phone found"));
    }

}
