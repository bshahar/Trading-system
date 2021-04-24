package Domain.DiscountPolicies;

public class DiscountByStore {
    private int prodId;

    public DiscountByStore(int prodId) {
        this.prodId = prodId;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }


}
