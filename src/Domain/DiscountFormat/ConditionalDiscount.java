package Domain.DiscountFormat;

import Domain.DiscountPolicies.*;
import java.util.*;

public class ConditionalDiscount extends Discount {
    private List<DiscountCondition> conditions;
    private int percentage;

    public ConditionalDiscount(int id, Date begin, Date end, List<DiscountCondition> conditions, int percentage) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.conditions = conditions;
        this.percentage = percentage;
    }



}
