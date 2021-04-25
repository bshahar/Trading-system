package Domain.Operators;

import Domain.Bag;
import Domain.DiscountPolicies.DiscountPolicy;
import Domain.Policy;
import Domain.PurchasePolicies.PurchasePolicy;
import Domain.User;

import java.util.Date;
import java.util.List;

public interface LogicOperator {
    boolean validateCondition(List<Policy> policies, User user, Date time, Bag bag);
}
