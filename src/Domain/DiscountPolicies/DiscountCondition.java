package Domain.DiscountPolicies;

import Domain.Operators.*;
import Domain.*;

import java.util.*;

public class DiscountCondition { //Compound object
    private List<Policy> discounts;
    private LogicOperator operator;

    public DiscountCondition(){
        this.discounts = new LinkedList<Policy>();
        this.operator = new NoneOperator();
    }

    public DiscountCondition(List<Policy> discounts, LogicOperator operator) {
        this.discounts = discounts;
        this.operator = operator;
    }

    public boolean validateCondition(User user, Date time, Bag bag) {
        if(this.operator instanceof NoneOperator)
            return discounts.get(0).validateCondition(user, time, bag);
        return operator.validateCondition(discounts, user, time, bag);
    }

    public void setOperator(LogicOperator operator) { this.operator = operator; }

    public void addDiscount(String policy, List<String> params) {
        DiscountPolicy dp;
        if (this.discounts == null)
            this.discounts = new LinkedList<>();

        switch (policy) {
            case "Minimal Amount":
                dp = new DiscountByMinimalAmount(Integer.parseInt(params.get(0)), Integer.parseInt(params.get(1)));
                this.discounts.add(dp);
                break;
            case "Minimal Cost":
                dp = new DiscountByMinimalCost(Double.parseDouble(params.get(0)));
                this.discounts.add(dp);
                break;
            case "Purchase Time":
                //params = boolean byDayInWeek, boolean byDayInMonth, boolean byHourInDay, int dayInWeek, int dayInMonth, int beginHour, int endHour
                dp = new DiscountByPurchaseTime(
                        Boolean.parseBoolean(params.get(0)),
                        Boolean.parseBoolean(params.get(1)),
                        Boolean.parseBoolean(params.get(2)),
                        Integer.parseInt(params.get(3)),
                        Integer.parseInt(params.get(4)),
                        Integer.parseInt(params.get(5)),
                        Integer.parseInt(params.get(6)));
                this.discounts.add(dp);
                break;

        }
    }
}
