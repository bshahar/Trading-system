package Permissions;

import Domain.Member;
import Domain.Store;
import Domain.User;

public class AppointManager {
    public AppointManager(Member member, Store store) {
    }

    public boolean action(User user, Store store) {
        if(store.addManager(user)) {
            store.addEmployee(user);
            user.updateManagerPermission(store);
            return true;
        }
        return false;
    }
}
