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
        CloseStore,
        ReopenStore,
        GetBossesInfo
    }

    private Map<User, User> appointments; //Appointed, appointee
    private List<Operations> operations;

    public boolean addPermissions(int currUserId, int editUserId, List<Integer> operations) {
        return true;
    }

    public boolean removePermissions(int currUserId, int editUserId, List<Integer> operations) {
        return true;
    }

}
