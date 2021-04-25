package Domain.PurchasePolicies;


import Domain.*;
import Domain.Operators.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PurchaseCondition {
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

    public boolean validateCondition(User user, Date time, Bag bag) {
        if(this.operator instanceof NoneOperator)
            return purchases.get(0).validateCondition(user, time, bag);
        return operator.validateCondition(purchases, user, time, bag);
    }

    public void setOperator(LogicOperator operator) { this.operator = operator; }

    public void addDiscount(String policy, List<String> params) {
        PurchasePolicy pp;
        if (this.purchases == null)
            this.purchases = new LinkedList<>();

        switch (policy) {
            case "Age Limit":
                pp = new AgeLimitPolicy(Integer.parseInt(params.get(0)));
                this.purchases.add(pp);
                break;
            case "Time Limit":
                pp = new TimeLimitPolicy(Integer.parseInt(params.get(0)));
                this.purchases.add(pp);
                break;
            default:
                break;
        }
    }
}
