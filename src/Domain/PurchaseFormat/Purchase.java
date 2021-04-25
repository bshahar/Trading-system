package Domain.PurchaseFormat;

import java.util.List;

public abstract class Purchase {
    public abstract boolean validatePurchase (List<PurchasePolicy> policies);

}
