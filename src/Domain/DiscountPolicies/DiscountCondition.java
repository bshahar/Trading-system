package Domain.DiscountPolicies;

import Domain.Operators.*;
import Domain.*;
import Domain.User;

import java.util.*;

public class DiscountCondition { //Compound object

    private int id;
    private List<Policy> discounts;
    private LogicOperator operator;

    public DiscountCondition(int id){
        this.id = id;
        this.discounts = new LinkedList<Policy>();
        this.operator = new NoneOperator();
    }

    public DiscountCondition(int id, List<Policy> discounts, LogicOperator operator) {
        this.id = id;
        this.discounts = discounts;
        this.operator = operator;
    }

    public boolean validateCondition(User user, Date time, Bag bag) {
        if(this.operator instanceof NoneOperator)
            return discounts.get(0).validateCondition(user, time, bag);
        return operator.validateCondition(discounts, user, time, bag);
    }

    public int getId() { return this.id; }

    public void setOperator(LogicOperator operator) { this.operator = operator; }

    public String getOperatorStr() {
        if(this.operator instanceof OrOperator)
            return "Or";
        else if(this.operator instanceof AndOperator)
            return "And";
        else if(this.operator instanceof XorOperator)
            return "Xor";
        else
            return "";
    }

    public List<Policy> getDiscounts() {
        return discounts;
    }

    public LogicOperator getOperator() {
        return operator;
    }

    public void addDiscountPolicy(PolicyCondition policy) {
        DiscountPolicy dp;
        if (this.discounts == null)
            this.discounts = new LinkedList<>();

        switch (policy.getPolicyName()) {
            case "Minimal Amount":
                dp = new DiscountByMinimalAmount(Integer.parseInt(policy.getPolicyParams().get(0)), Integer.parseInt(policy.getPolicyParams().get(1)));
                this.discounts.add(dp);
                break;
            case "Minimal Cost":
                dp = new DiscountByMinimalCost(Double.parseDouble(policy.getPolicyParams().get(0)));
                this.discounts.add(dp);
                break;
            case "Purchase Time":
                //params = boolean byDayInWeek, boolean byDayInMonth, boolean byHourInDay, int dayInWeek, int dayInMonth, int beginHour, int endHour
                dp = new DiscountByPurchaseTime(
                        Boolean.parseBoolean(policy.getPolicyParams().get(0)),
                        Boolean.parseBoolean(policy.getPolicyParams().get(1)),
                        Boolean.parseBoolean(policy.getPolicyParams().get(2)),
                        Integer.parseInt(policy.getPolicyParams().get(3)),
                        Integer.parseInt(policy.getPolicyParams().get(4)),
                        Integer.parseInt(policy.getPolicyParams().get(5)),
                        Integer.parseInt(policy.getPolicyParams().get(6)));
                this.discounts.add(dp);
                break;

        }
    }
}
