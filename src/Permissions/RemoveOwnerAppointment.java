package Permissions;

import Domain.Member;
import Domain.Result;
import Domain.Store;
import Persistance.User;

public class RemoveOwnerAppointment {
    final private Member member;
    final private Store store;

    public RemoveOwnerAppointment(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public Result action(User owner, User manager) {
        return store.removeOwner(owner, manager);
    }
}
