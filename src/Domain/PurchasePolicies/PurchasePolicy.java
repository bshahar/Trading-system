package Domain.PurchasePolicies;

import Domain.*;
import Persistance.User;

import java.util.Date;

public abstract class PurchasePolicy implements Policy {
    public abstract boolean validateCondition(User user, Date time, Bag bag);
}
