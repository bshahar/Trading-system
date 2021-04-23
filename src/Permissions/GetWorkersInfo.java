package Permissions;

import Domain.Member;
import Domain.Result;
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
    public Result action() {
        return this.store.getEmployees();
    }
}
