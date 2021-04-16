package Permissions;

import Domain.Member;
import Domain.Store;
import Domain.User;

import java.util.List;

public class GetWorkersInfo {
    final private Member member;
    final private Store store;

    public GetWorkersInfo(Member member, Store store) {
        this.member = member;
        this.store = store;
    }
    public List<User> action() {
        return this.store.getEmployees();
    }
}
