package Domain.Operators;

import Domain.Bag;
import Domain.DiscountPolicies.DiscountPolicy;

import java.util.Date;
import java.util.List;

public interface LogicOperator {
    boolean validateCondition(List<DiscountPolicy> discounts, int userId, Date time, Bag bag);
}
