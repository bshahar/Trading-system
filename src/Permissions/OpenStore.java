package Permissions;

import Domain.Member;
import Domain.Store;

public class OpenStore {
    final private Member member;
    final private Store store;

    public OpenStore(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public void action() {

    }
}
