package Permissions;

import Domain.Member;
import Domain.Store;

public class RemovePermission {
    final private Member member;
    final private Store store;

    public RemovePermission(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public boolean action() {
        return true;
    }
}
