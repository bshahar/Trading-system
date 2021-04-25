package Domain.DiscountPolicies;

import Domain.Bag;

import java.util.Date;

public class DiscountByMinimalAmount extends DiscountPolicy {
    private int prodId;
    private int minAmount;


    public DiscountByMinimalAmount(int minAmount, int prodId) {
        this.prodId = prodId;
        this.minAmount = minAmount;
    }

    @Override
    public boolean validateCondition(int userId, Date time, Bag bag) {
        return false;
        //todo
    }
}
