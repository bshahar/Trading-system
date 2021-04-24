package Domain.DiscountPolicies;

import Domain.*;

public class DiscountByUser {

    private int prodId;
    private User user;

    public DiscountByUser(int prodId, User user) {
        this.prodId = prodId;
        this.user = user;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
