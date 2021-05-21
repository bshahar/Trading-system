package Permissions;

import Domain.*;
import Domain.User;


public class AppointOwner {
    final private Member member;
    final private Store store;

    public AppointOwner(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public Result action(User owner, User user, Store store) {
        if(store.addOwner(user)) {
            store.addEmployee(owner,user);
            store.addOwnerToAppointments(user);
            user.updateOwnerPermission(store);
            user.addToMyStores(store);

            return new Result(true,true);
        }
        return new Result(false,"Cant appoint the User" );
    }
}
