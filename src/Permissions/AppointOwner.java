package Permissions;

import Domain.Member;
import Domain.Store;
import Domain.User;


public class AppointOwner {
    final private Member member;
    final private Store store;

    public AppointOwner(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public boolean action(User user ,Store store) {
        store.addEmployee(user);
        user.updateOwnerPermission(store);
        return true;
    }
}
