package Domain.DiscountPolicies;

import Domain.Bag;

import java.util.Date;

public class DiscountByStore extends DiscountPolicy {

    public DiscountByStore() {

    }


    @Override
    public boolean validateCondition(int userId, Date time, Bag bag) {
        return false;
        //todo
    }
}
