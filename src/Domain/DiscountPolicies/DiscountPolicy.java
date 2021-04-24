package Domain.DiscountPolicies;

import Domain.Bag;
import java.util.Date;

public abstract class DiscountPolicy {
    public abstract boolean validateCondition(int userId, Date time, Bag bag);
}
