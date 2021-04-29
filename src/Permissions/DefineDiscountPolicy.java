package Permissions;

import Domain.DiscountFormat.Discount;
import Domain.DiscountPolicies.DiscountCondition;
import Domain.Member;
import Domain.Product;
import Domain.Result;
import Domain.Store;

import java.util.Date;

public class DefineDiscountPolicy {
    final private Member member;
    final private Store store;

    public DefineDiscountPolicy(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public Result action(String param, String condition, Product.Category category, int prodId, Date begin, Date end, DiscountCondition conditions, int percentage, Discount.MathOp op) {
        switch (param) {
            case "PRODUCT":
                if (condition.equals("simple"))
                    this.store.addSimpleDiscountOnProduct(prodId, begin, end, percentage, op);
                else //condition == complex
                    this.store.addDiscountOnProduct(prodId, begin, end, conditions, percentage, op);
                break;
            case "CATEGORY":
                if (condition.equals("simple"))
                    this.store.addSimpleDiscountOnCategory(category, begin, end, percentage, op);
                else //condition == complex
                    this.store.addDiscountOnCategory(category, begin, end, conditions, percentage, op);
                break;
            case "STORE":
                if (condition.equals("simple"))
                    this.store.addSimpleDiscountOnStore(begin, end, percentage, op);
                else //condition == complex
                    this.store.addDiscountOnStore(begin, end, conditions, percentage, op);
                break;
        }
        return new Result(true, "Discount policy added successfully.");
    }
}
