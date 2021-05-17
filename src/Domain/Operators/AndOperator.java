package Domain.Operators;

import Domain.Bag;
import Domain.Policy;
import Persistance.User;

import java.util.*;

public class AndOperator implements LogicOperator {

    @Override
    public boolean validateCondition(List<Policy> policies, User user, Date time, Bag bag) {
        boolean result = true;
        for (Policy p: policies) {
            result = result & p.validateCondition(user, time, bag);
        }
        return result;
    }
}
