package Domain.DiscountPolicies.Operators;

import Domain.Bag;
import Domain.DiscountPolicies.DiscountPolicy;

import java.util.*;

public class OrOperator implements LogicOperator {

    @Override
    public boolean validateCondition(List<DiscountPolicy> discounts, int userId, Date time, Bag bag) {
        boolean result = false;
        for (DiscountPolicy e : discounts) {
            result = result | e.validateCondition(userId, time, bag);
        }
        return result;
    }
}
