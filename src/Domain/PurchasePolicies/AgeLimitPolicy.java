package Domain.PurchasePolicies;

import Domain.Bag;
import Domain.User;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AgeLimitPolicy extends PurchasePolicy{
    private int ageLimit;

    public AgeLimitPolicy(int ageLimit){
        this.ageLimit = ageLimit;
    }

    @Override
    public boolean validateCondition(User user, Date time, Bag bag) {
        return user.getAge() >= ageLimit;
    }

    @Override
    public String getPolicyName() {
        return "Age Limit";
    }

    @Override
    public List<String> getPolicyParams() {
        List<String> params = new LinkedList<>();
        params.add(String.valueOf(ageLimit));
        return params;
    }
}
