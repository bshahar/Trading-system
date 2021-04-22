package Domain.DiscountPolicies;

import Domain.*;

public class DiscountByUser implements DiscountPolicy{

    private int prodId;
    private int percentage;
    private User user;

    public DiscountByUser(int prodId, int percentage, User user) {
        this.prodId = prodId;
        this.percentage = percentage;
        this.user = user;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
