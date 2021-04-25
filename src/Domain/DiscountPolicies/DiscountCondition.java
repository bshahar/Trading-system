package Domain.DiscountPolicies;

import Domain.Bag;
import Domain.Operators.*;
import java.util.*;

public class DiscountCondition { //Compound object
    private List<DiscountPolicy> discounts;
    private LogicOperator operator;

    public DiscountCondition(){
        this.discounts = new LinkedList<>();
        this.operator = new NoneOperator();
    }

    public DiscountCondition(List<DiscountPolicy> discounts, LogicOperator operator) {
        this.discounts = discounts;
        this.operator = operator;
    }

    public boolean validateCondition(int userId, Date time, Bag bag) {
        if(this.operator instanceof NoneOperator)
            return discounts.get(0).validateCondition(userId, time, bag);
        return operator.validateCondition(discounts, userId, time, bag);
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
                dp = new DiscountByMinimalCost(Integer.parseInt(params.get(0)));
                this.discounts.add(dp);
                break;
/*
            case "All Products":
                dp = new DiscountByStore();
                this.discounts.add(dp);
                break;
            case "Category":
                dp = new DiscountBy();
                this.discounts.add(dp);
                break;
*/

        }
    }
}
