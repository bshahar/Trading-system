package Domain.Operators;

import Domain.Bag;
import Domain.Policy;
import Persistance.User;

import java.util.*;

public class NoneOperator implements LogicOperator {
    @Override
    public boolean validateCondition(List<Policy> policies, User user, Date time, Bag bag) {
        return true;
    }
}
