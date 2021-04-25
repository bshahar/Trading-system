package Domain.DiscountPolicies;

import Domain.Bag;
import Domain.Policy;

import java.util.Date;

public abstract class DiscountPolicy implements Policy {
    public abstract boolean validateCondition(int userId, Date time, Bag bag);
}
