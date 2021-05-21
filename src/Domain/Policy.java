package Domain;

import java.util.Date;
import java.util.List;

public interface Policy {
    boolean validateCondition(User user, Date time, Bag bag);

    String getPolicyName();

    List<String> getPolicyParams();
}
