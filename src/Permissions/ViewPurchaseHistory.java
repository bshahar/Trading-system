package Permissions;

import Domain.Member;
import Domain.Receipt;
import Domain.Result;
import Domain.Store;

import java.util.List;

public class
ViewPurchaseHistory {
    final private Member member;
    final private Store store;

    public ViewPurchaseHistory(Member member, Store store) {
        this.member = member;
        this.store = store;
    }
    public Result action() {
        return this.store.getPurchaseHistory();
    }
}
