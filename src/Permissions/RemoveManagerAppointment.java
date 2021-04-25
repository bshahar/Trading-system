package Permissions;

import Domain.Member;
import Domain.Result;
import Domain.Store;
import Domain.User;

public class RemoveManagerAppointment {

    final private Member member;
    final private Store store;

    public RemoveManagerAppointment(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public Result action(User owner, User manager) {
        return store.removeManager(owner, manager);
    }
}
