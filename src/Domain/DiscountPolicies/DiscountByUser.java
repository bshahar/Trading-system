package Domain.DiscountPolicies;

import Domain.*;

import java.util.Date;

public class DiscountByUser extends DiscountPolicy{

    public DiscountByUser() {
    }

    @Override
    public boolean validateCondition(User user, Date time, Bag bag) {
        return user.isRegistered();
    }
}
