package Domain.Operators;

import Domain.Bag;
import Domain.DiscountPolicies.DiscountPolicy;

import java.util.*;

public class XorOperator implements LogicOperator {
    @Override
    public boolean validateCondition(List<DiscountPolicy> discounts, int userId, Date time, Bag bag) {
        int count = 0;
        for (DiscountPolicy e : discounts) {
            if (e.validateCondition(userId, time, bag))
                count++;
            if(count > 1)
                return false;
        }
        return count == 1;
    }
}
