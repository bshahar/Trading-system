package Permissions;

import Domain.Member;
import Domain.Store;
import Domain.User;

public class AppointManager {
    public AppointManager(Member member, Store store) {
    }

    public boolean action(User owner,User user, Store store) {
        if(store.addManager(user)) {
            store.addEmployee(owner,user);
            user.updateManagerPermission(store);
            user.addToMyStores(store);
            return true;
        }
        return false;
    }
}
