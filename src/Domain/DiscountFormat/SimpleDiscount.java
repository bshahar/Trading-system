package Domain.DiscountFormat;

import Domain.Bag;
import Domain.Product;
import Domain.User;

import java.util.Date;

public class SimpleDiscount extends Discount{


    public SimpleDiscount(int id, Date begin, Date end,  int percentage, MathOp op) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.percentage = percentage;
        this.mathOp = op;
    }

    @Override
    public double calculateDiscount(Product prod, User user, Date time, Bag bag) {
        return (this.percentage * prod.getPrice()) / 100;
    }
}
