package Domain.DiscountPolicies;

public class DiscountByMinimalCost {
    private int prodId;
    private double minCost;

    public DiscountByMinimalCost(int prodId, double minCost) {
        this.prodId = prodId;
        this.minCost = minCost;
    }
}
