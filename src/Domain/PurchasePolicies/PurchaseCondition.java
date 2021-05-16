package Domain.PurchasePolicies;


import Domain.*;
import Domain.DiscountPolicies.PolicyCondition;
import Domain.Operators.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PurchaseCondition {
    //todo id
    private List<Policy> purchases;
    private LogicOperator operator;

    public PurchaseCondition(){
        this.purchases = new LinkedList<>();
        this.operator = new NoneOperator();
    }

    public PurchaseCondition(List<Policy> purchases, LogicOperator operator) {
        this.purchases = purchases;
        this.operator = operator;
    }

    public List<Policy> getPurchases() {
        return purchases;
    }

    public boolean validateCondition(User user, Date time, Bag bag) {
        if(this.operator instanceof NoneOperator)
            return purchases.get(0).validateCondition(user, time, bag);
        return operator.validateCondition(purchases,user, time, bag);
    }

    public String getOperator() {
        if(this.operator instanceof OrOperator)
            return "Or";
        else if(this.operator instanceof AndOperator)
            return "And";
        else if(this.operator instanceof XorOperator)
            return "Xor";
        else
            return "";
    }

    public void setOperator(LogicOperator operator) { this.operator = operator; }

    public void addPurchasePolicy(PolicyCondition policy) {
        PurchasePolicy pp;
        if (this.purchases == null)
            this.purchases = new LinkedList<>();

        switch (policy.getPolicyName()) {
            case "Age Limit":
                pp = new AgeLimitPolicy(Integer.parseInt(policy.getPolicyParams().get(0)));
                this.purchases.add(pp);
                break;
            case "Time Limit":
                pp = new TimeLimitPolicy(Integer.parseInt(policy.getPolicyParams().get(0)));
                this.purchases.add(pp);
                break;
            case "Min Amount":
                pp = new MinAmountPolicy(Integer.parseInt(policy.getPolicyParams().get(0)), Integer.parseInt(policy.getPolicyParams().get(1)));
                this.purchases.add(pp);
                break;
            case "Max Amount":
                pp = new MaxAmountPolicy(Integer.parseInt(policy.getPolicyParams().get(0)), Integer.parseInt(policy.getPolicyParams().get(1)));
                this.purchases.add(pp);
                break;
            default:
                break;
        }
    }
}
