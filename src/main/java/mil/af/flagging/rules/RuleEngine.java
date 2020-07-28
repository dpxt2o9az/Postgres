/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.rules;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Brad
 */
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
