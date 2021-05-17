package Domain.Operators;

import Domain.Bag;
import Domain.Policy;
import Persistance.User;

import java.util.*;

public class XorOperator implements LogicOperator {

    @Override
    public boolean validateCondition(List<Policy> policies, User user, Date time, Bag bag) {
        int count = 0;
        for (Policy p: policies) {
            if (p.validateCondition(user, time, bag))
                count++;
            if(count > 1)
                return false;
        }
        return count == 1;
    }
}
