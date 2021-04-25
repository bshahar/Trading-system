package Domain.DiscountPolicies;

import Domain.*;

import java.util.Date;

public class DiscountByUser extends DiscountPolicy{

    private User user;

    public DiscountByUser(User user) {
        this.user = user;
    }

    @Override
    public boolean validateCondition(int userId, Date time, Bag bag) {
        return false;
        //todo
    }
}
