package Domain.PurchasePolicies;

import Domain.*;

import java.util.*;

public class MinAmountPolicy extends PurchasePolicy {
    private int prodId;
    private int minAmount;


    public MinAmountPolicy(int minAmount, int prodId) {
        this.prodId = prodId;
        this.minAmount = minAmount;
    }

    @Override
    public boolean validateCondition(User user, Date time, Bag bag) {
        int amount = 0;
        for (Product p: bag.getProducts()) {
            if(p.getId() == this.prodId)
                amount = bag.getProductsAmounts().get(p);
        }
        return amount >= this.minAmount;
    }

    public void setMinimalAmount(int newMinAmount) { this.minAmount = newMinAmount; }

    @Override
    public String getPolicyName() {
        return "Min Amount";
    }

    @Override
    public List<String> getPolicyParams() {
        List<String> params = new LinkedList<>();
        params.add(String.valueOf(minAmount));
        params.add(String.valueOf(prodId));
        return params;
    }


}
