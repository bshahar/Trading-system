package Domain.PurchasePolicies;

import Domain.Bag;
import Domain.User;

import java.util.*;

public class TimeLimitPolicy extends PurchasePolicy{

    private int hourOfDay;

    public TimeLimitPolicy(int hourOfDay){
        this.hourOfDay = hourOfDay;
    }
    @Override
    public boolean validateCondition(User user, Date time, Bag bag) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(time);   // assigns calendar to given date
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= hourOfDay )
            return false;
        return true;
    }

    @Override
    public String getPolicyName() {
        return "Time Limit";
    }

    @Override
    public List<String> getPolicyParams() {
        List<String> params = new LinkedList<>();
        params.add(String.valueOf(hourOfDay));
        return params;
    }
}
