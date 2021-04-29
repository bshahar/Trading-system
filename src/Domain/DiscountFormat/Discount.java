package Domain.DiscountFormat;

import Domain.Bag;
import Domain.Product;
import Domain.User;

import java.util.Date;


public abstract class Discount {

    public enum MathOp {
        MAX,
        SUM
    }

    protected int id;
    protected Date begin;
    protected Date end;
    protected MathOp mathOp;
    protected int percentage;

    public abstract double calculateDiscount(Product prod, User user, Date time, Bag bag);

    public MathOp getMathOp() {
        return mathOp;
    }
}
