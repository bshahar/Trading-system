import java.util.*;

public class Permissions {
    enum Operations {
        AddProduct,
        RemoveProduct,
        EditProduct,
        AppointManager,
        RemoveManagerAppointment,
        AppointOwner,
        RemoveOwnerAppointment,
        DefinePurchasePolicy,
        DefinePurchaseFormat,
        DefineDiscountPolicy,
        DefineDiscountFormat,
        CloseStore, //only for store creator
        ReopenStore,
        GetBossesInfo
    }

    //private Map<User, User> appointments; //Appointed, appointee
    private List<Operations> operations;

    private Map<Integer, Integer> appointments; //Appointed, appointee (ids)
    private Map<User, List<Operations>> usersPermissions;

    public boolean addPermissions(int currUserId, int editUserId, List<Integer> opIndexes) {
        for (User u: usersPermissions.keySet()) {


        }
        Operations[] ops = Operations.values();
        for (int i: opIndexes) {
            if(!this.operations.contains(ops[i-1]))
                this.operations.add(ops[i-1]);
        }
        return true;
    }

    public void removePermissions(int currUserId, int editUserId, List<Integer> opIndexes) {
    }

}
