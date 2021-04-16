package Permissions;

import Domain.Member;
import Domain.Store;

public class RemoveProduct {

    final private Member member;
    final private Store store;

    public RemoveProduct(Member member, Store store) {
        this.member =member;
        this.store=store;
    }

    public boolean action(int productId) {

        return store.removeProductFromStore(productId);
    }
}
