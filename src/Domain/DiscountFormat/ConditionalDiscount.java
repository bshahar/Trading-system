package Domain.DiscountFormat;

import Domain.Bag;
import Domain.DiscountPolicies.*;
import Domain.Product;
import Persistance.User;

import java.util.*;

public class ConditionalDiscount extends Discount {

    private DiscountCondition conditions;

    public ConditionalDiscount(int id, Date begin, Date end, DiscountCondition conditions, int percentage, MathOp op) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.conditions = conditions;
        this.percentage = percentage;
        this.mathOp = op;
    }

    //Returns the amount of money to reduce from the original cost of the bag
    @Override
    public double calculateDiscount(Product prod, User user, Date time, Bag bag) {
        double discount = 0;
        if (time.after(this.begin) && time.before(this.end)) {
            if (conditions.validateCondition(user, time, bag)) {
                discount = (this.percentage * prod.getPrice()) / 100;
            }
        }
        return discount;
    }

    public DiscountCondition getConditions() { return this.conditions; }



}
