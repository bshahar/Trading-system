package Domain.PurchaseFormat;

import java.util.List;

public class ImmediatePurchase extends Purchase {

    @Override
    public boolean validatePurchase(List<PurchasePolicy> policies) {
        return false;
    }
}
