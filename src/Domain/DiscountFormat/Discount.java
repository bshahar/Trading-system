package Domain.DiscountFormat;

import Domain.Bag;
import Domain.Operators.AndOperator;
import Domain.Operators.OrOperator;
import Domain.Operators.XorOperator;
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

    public Date getBegin() { return this.begin; }

    public Date getEnd() { return this.end; }

    public int getPercentage() { return this.percentage; }

    public MathOp getMathOp() {
        return this.mathOp;
    }

    public String getMathOpStr() {
        if(String.valueOf(this.mathOp).equals("MAX"))
            return "Max";
        else
            return "Sum";
    }
}
