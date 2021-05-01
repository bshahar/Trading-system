package Permissions;

import Domain.Member;
import Domain.Result;
import Domain.Store;

public class EditPurchasePolicy {

    final private Member member;
    final private Store store;

    public EditPurchasePolicy(Member member, Store store) {
        this.store = store;
        this.member = member;
    }

    public Result action() {
        this.store.removePurchasePolicy();
        return new Result(true, "Purchase policy was removed successfully.");
    }
}
