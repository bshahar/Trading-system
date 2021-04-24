package Domain.DiscountPolicies.Operators;

import Domain.Bag;
import Domain.DiscountPolicies.DiscountPolicy;

import java.util.*;

public class NoneOperator implements LogicOperator {
    @Override
    public boolean validateCondition(List<DiscountPolicy> discounts, int userId, Date time, Bag bag) {
        return true;
    }
}
