package Domain.PurchasePolicies;

import Domain.*;
import Domain.User;

import java.util.Date;

public abstract class PurchasePolicy implements Policy {
    public abstract boolean validateCondition(User user, Date time, Bag bag);
}
