package Permissions;

import Domain.Member;
import Domain.Store;

public class AddPermissions {
    final private Member member;
    final private Store store;

    public AddPermissions(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public boolean action() {
        return true;
    }
}
