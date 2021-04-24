package Domain.DiscountPolicies;

import Domain.Bag;
import Domain.DiscountPolicies.Operators.*;
import java.util.*;

public class DiscountCondition { //Compound object
    private List<DiscountPolicy> discounts;
    private LogicOperator operator;

    public DiscountCondition(List<DiscountPolicy> discounts, LogicOperator operator) {
        this.discounts = discounts;
        this.operator = operator;
    }

    public boolean validateCondition(int userId, Date time, Bag bag) {
        if(this.operator instanceof NoneOperator)
            return discounts.get(0).validateCondition(userId, time, bag);
        return operator.validateCondition(discounts, userId, time, bag);
    }

}
