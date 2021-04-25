package Domain.DiscountFormat;

import Domain.Bag;
import Domain.DiscountPolicies.*;
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

    //Returns the amount of money to reduce from the original cost of the bag
    public double calculateDiscount(double totalCost, int userId, Date time, Bag bag) {
        double discount = 0;
        if (time.after(this.begin) && time.before(this.end)) {
            if (conditions.validateCondition(userId, time, bag)) {
                discount = (this.percentage * totalCost) / 100;
            }
        }
        return discount;
    }

}