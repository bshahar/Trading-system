package Domain.DiscountPolicies;

public class DiscountByMinimalAmount {
    private int prodId;
    private int minAmount;


    public DiscountByMinimalAmount(int prodId, int minAmount) {
        this.prodId = prodId;
        this.minAmount = minAmount;
    }

}
