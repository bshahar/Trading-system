package Domain.DiscountPolicies;

import Domain.*;

import java.util.Date;

public abstract class DiscountPolicy implements Policy {

    public abstract boolean validateCondition(User user, Date time, Bag bag);
}
