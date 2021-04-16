package Permissions;

import Domain.Member;
import Domain.Store;
import Domain.User;

public class AppointManager {
    public AppointManager(Member member, Store store) {
    }

    public boolean action(User user, Store store) {
        user.updateManagerPermission(store);
        return true;
    }
}
