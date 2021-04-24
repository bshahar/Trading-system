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

    public boolean action(User owner, User user, Store store) {
        if(store.addOwner(user)) {
            store.addEmployee(owner,user);
            store.addOwnerToAppointments(user);
            user.updateOwnerPermission(store);
            user.addToMyStores(store);
            return true;
        }
        return false;
    }
}
