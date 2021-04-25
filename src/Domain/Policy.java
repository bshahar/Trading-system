package Domain;

import java.util.Date;

public interface Policy {
    boolean validateCondition(User user, Date time, Bag bag);
}
