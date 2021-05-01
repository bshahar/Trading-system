package Domain.DiscountPolicies;

import Domain.*;

import java.util.Date;
import java.util.List;

public abstract class DiscountPolicy implements Policy {

    public abstract boolean validateCondition(User user, Date time, Bag bag);

    public abstract String getPolicyName();

    public abstract List<String> getPolicyParams();

}
