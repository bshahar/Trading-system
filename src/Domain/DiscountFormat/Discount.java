package Domain.DiscountFormat;

import Domain.Bag;
import Domain.User;

import java.util.Date;

public abstract class Discount {
    protected int id;
    protected Date begin;
    protected Date end;

    public abstract double calculateDiscount(double totalCost, User user, Date time, Bag bag);
}
