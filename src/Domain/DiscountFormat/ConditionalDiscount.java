package Domain.DiscountFormat;

import Domain.Bag;
import Domain.DiscountPolicies.*;
import Domain.User;

import java.util.*;

public class ConditionalDiscount extends Discount {
    private DiscountCondition conditions;
    private int percentage;

    public ConditionalDiscount(int id, Date begin, Date end, DiscountCondition conditions, int percentage) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.conditions = conditions;
        this.percentage = percentage;
    }

    public double calculateDiscount(double totalCost, User user, Date time, Bag bag)
    {
        if(time.after(this.begin) && time.before(this.end)) {
            if (conditions.validateCondition(user, time, bag)) {
                //totalCost =
            }
        }
        return totalCost;
    }

}
