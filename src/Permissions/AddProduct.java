package Permissions;

import Domain.Member;
import Domain.Store;

public class AddProduct {
    final private Member member;
    final private Store store;

    public AddProduct(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public void action() {
    }
}
