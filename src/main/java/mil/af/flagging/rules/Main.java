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
public class Main {

    public static void main(String[] args) {
        RuleEngine ruleEngine = new RuleEngine();
        ruleEngine
                .registerRule(new IPhone())
                .registerRule(new AndroidPhone())
                .registerRule(new WindowsPhone());

        Phone androidPhone = new Phone(Phone.OSType.ANDROID);

        Phone phone = ruleEngine.rule(androidPhone);

        System.out.println("Output Phone: " + phone);

    }

}
