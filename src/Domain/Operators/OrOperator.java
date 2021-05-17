package Domain.Operators;

import Domain.Bag;
import Domain.Policy;
import Persistance.User;

import java.util.*;

public class OrOperator implements LogicOperator {

    @Override
    public boolean validateCondition(List<Policy> policies, User user, Date time, Bag bag) {
        boolean result = false;
        for (Policy p: policies) {
            result = result | p.validateCondition(user, time, bag);
        }
        return result;
    }
}
