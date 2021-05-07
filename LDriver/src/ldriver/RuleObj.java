package ldriver;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Eleni Oliveira
 */
public class RuleObj {

    private ArrayList<String> rules;
    private int ruleCount;
    private Random random;

    public RuleObj() {
        rules = new ArrayList<String>();
        ruleCount = 0;
        random = new Random();
    }

    // Adicione uma regra à lista
    public void addRule(String rule) {
        rules.add(rule);
        ++ruleCount;
    }

    //Escolha uma regra aleatoriamente. Cada regra tem uma chance igual de ser escolhida.
    public String getRule() {
        int choice = random.nextInt(ruleCount);
        return rules.get(choice);
    }
}
