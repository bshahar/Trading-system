package Domain.PurchasePolicies;

import Domain.Bag;
import Domain.User;

import java.util.Date;

public class AgeLimitPolicy extends PurchasePolicy{
    private int ageLimit;

    public AgeLimitPolicy(int ageLimit){
        this.ageLimit = ageLimit;
    }

    @Override
    public boolean validateCondition(User user, Date time, Bag bag) {
        if (user.getAge() >= ageLimit)
            return true;
        return false;
    }
}
