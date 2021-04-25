package Domain.DiscountFormat;

import Domain.Bag;
import Domain.User;

import java.util.Date;

public class SimpleDiscount extends Discount{

    private int percentage;

    public SimpleDiscount(int id, Date begin, Date end,  int percentage) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.percentage = percentage;
    }

    @Override
    public double calculateDiscount(double totalCost, User user, Date time, Bag bag) {
        return (this.percentage * totalCost) / 100;
    }
}
