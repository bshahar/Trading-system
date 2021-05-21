package Permissions;

import Domain.Member;
import Domain.Result;
import Domain.Store;
import Domain.User;

public class AppointManager {
    public AppointManager(Member member, Store store) {
    }

    public Result action(User owner, User user, Store store) {
        if(store.addManager(user)) {
            store.addEmployee(owner,user);
            user.updateManagerPermission(store);
            user.addToMyStores(store);

            return new Result(true,true);
        }
        return new Result(false,"User has no permissions");
    }
}
