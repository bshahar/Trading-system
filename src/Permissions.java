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
        EditPurchasePolicy,
        DefinePurchaseFormat,
        EditPurchaseFormat,
        DefineDiscountPolicy,
        EditDiscountPolicy,
        DefineDiscountFormat,
        EditDiscountFormat,
        CloseStore, //only for store creator
        ReopenStore,
        GetWorkersInfo,
        ViewMessages,
        ReplayMessages,
        ViewPurchaseHistory
    }

    private final int OWNER = 1;
    private final int MANAGER = 2;

    public Permissions(User founder) {
        this.founder = founder;
        this.roles = new HashMap<>();
        this.appointments = new HashMap<>();
        this.usersPermissions = new HashMap<>();
        this.roles.put(founder, OWNER);
        addOwnerPermissions(founder);
    }


    private User founder; //TODO change to Registered after implementation of inheritance
    private Map<User, Integer> roles;
    private Map<Integer, Integer> appointments; //Appointed, appointee (ids)
    private Map<User, List<Operations>> usersPermissions;

    /**
     * Add permissions to a store manager.
     * Can be done only by a store owner.
     * @param currUserId store owner's id
     * @param editUserId store manager's id
     * @param opIndexes indexes of operations to add
     * @return true in case of success, false otherwise.
     */
    public boolean addPermissions(int currUserId, int editUserId, List<Integer> opIndexes) {
        for (User u1: this.roles.keySet()) {
            if(u1.getId() == currUserId && roles.get(u1) == OWNER) { //if current user is an owner of the store
                for (User u2: this.usersPermissions.keySet()) {
                    if(u2.getId() == editUserId) { //find the user we want to edit his permissions
                        //TODO need to check if u1 appointed u2?
                        Operations[] ops = Operations.values();
                        for (int i: opIndexes) {
                            if(!this.usersPermissions.get(u2).contains(ops[i-1]))
                                this.usersPermissions.get(u2).add(ops[i-1]);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Remove permissions from a store manager.
     * Can be done only by a store owner.
     * @param currUserId store owner's id
     * @param editUserId store manager's id
     * @param opIndexes indexes of operations to add
     * @return true in case of success, false otherwise.
     */
    public boolean removePermissions(int currUserId, int editUserId, List<Integer> opIndexes) {
        for (User u1: this.roles.keySet()) {
            if(u1.getId() == currUserId && roles.get(u1) == OWNER) { //if current user is an owner of the store
                for (User u2: this.usersPermissions.keySet()) {
                    if(u2.getId() == editUserId
                            && this.appointments.get(u1.getId()) == u2.getId()
                            && this.roles.get(u2) == MANAGER) {
                        //find the user we want to edit his permissions
                        //check if user1 appointed user2 and that user2 is manager NOT owner
                        Operations[] ops = Operations.values();
                        for (int i: opIndexes) {
                            if(this.usersPermissions.get(u2).contains(ops[i-1]))
                                this.usersPermissions.get(u2).remove(ops[i-1]);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Appoint a new store owner.
     * @param currUserId owner who makes the appointment
     * @param newUser new owner of the store
     * @return true true in case of success, false otherwise.
     */
    public boolean appointOwner(int currUserId, User newUser) {
        for (User u1: this.roles.keySet()) {
            if (u1.getId() == currUserId && roles.get(u1) == OWNER) { //if current user is an owner of the store
                for (User u2 : this.roles.keySet()) {
                    if (u2.getId() == newUser.getId()) {
                        if (this.roles.get(u2) == MANAGER) { //if new user was already a store manager
                            this.roles.replace(u2, MANAGER, OWNER);
                            addOwnerPermissions(u2);
                            for (int id1 : this.appointments.keySet()) { //if new user was already appointed
                                if (this.appointments.get(id1) == u2.getId())
                                    this.appointments.remove(id1, u2.getId());
                            }
                            this.appointments.put(currUserId, u2.getId());
                            return true;
                        }
                    }
                }
                if (newUser.isRegistered() && !this.roles.containsKey(newUser)) {
                    this.roles.put(newUser, OWNER);
                    this.appointments.put(currUserId, newUser.getId());
                    addOwnerPermissions(newUser);
                    return true;
                }
            }
        }
        return false;
    }

    private void addOwnerPermissions(User user) {
        List<Operations> p = new LinkedList<>();
        p.add(Operations.AddProduct);
        p.add(Operations.RemoveProduct);
        p.add(Operations.EditProduct);
        p.add(Operations.AppointManager);
        p.add(Operations.RemoveManagerAppointment);
        p.add(Operations.AppointOwner);
        p.add(Operations.RemoveOwnerAppointment);
        p.add(Operations.DefinePurchasePolicy);
        p.add(Operations.EditPurchasePolicy);
        p.add(Operations.DefinePurchaseFormat);
        p.add(Operations.EditPurchaseFormat);
        p.add(Operations.DefineDiscountPolicy);
        p.add(Operations.EditDiscountPolicy);
        p.add(Operations.DefineDiscountFormat);
        p.add(Operations.EditDiscountFormat);
        p.add(Operations.ReopenStore);
        p.add(Operations.GetWorkersInfo);
        p.add(Operations.ViewMessages);
        p.add(Operations.ReplayMessages);
        p.add(Operations.ViewPurchaseHistory);

        if(user.getId() == this.founder.getId())
            p.add(Operations.CloseStore);
        if(this.usersPermissions.replace(user, p) == null) //if the user did not exist in the list before
            this.usersPermissions.put(user, p);
    }


    /**
     * Appoint a new store manager.
     * @param currUserId owner who makes the appointment
     * @param newUser new manager of the store
     * @return true true in case of success, false otherwise.
     */
    public boolean appointManager(int currUserId, User newUser) {
        if(this.roles.containsKey(newUser)) //user already had a role in this store
            return false;
        for (User u1: this.roles.keySet()) {
            if (u1.getId() == currUserId && roles.get(u1) == OWNER) { //if current user is an owner of the store
                for (User u2: this.roles.keySet()) {
                    if (u2.getId() == newUser.getId())
                        return false;
                }
                if (newUser.isRegistered() && !this.roles.containsKey(newUser)) {
                    this.roles.put(newUser, MANAGER);
                    addManagerPermissions(newUser);
                    return true;
                }
            }
        }
        return false;
    }

    private void addManagerPermissions(User user) {
        List<Operations> p = new LinkedList<>();
        p.add(Operations.GetWorkersInfo);
        p.add(Operations.ViewMessages);
        p.add(Operations.ReplayMessages);
        this.usersPermissions.put(user, p);
    }

    /**
     * Remove owner/ manager.
     * @param currUserId owner who removes the appointment
     * @param toRemoveId owner/ manager to remove
     * @return true true in case of success, false otherwise.
     */
    public boolean removeAppointment(int currUserId, int toRemoveId) {
        for (int id: this.appointments.keySet()) {
            if(id == currUserId && this.appointments.get(currUserId) == toRemoveId) {
                this.roles.remove(getUserById(toRemoveId));
                this.usersPermissions.remove(getUserById(toRemoveId));
                if(this.roles.get(getUserById(currUserId)) == OWNER) {
                    removeSubAppointments(toRemoveId);
                }
            }
        }
        return false;
    }

    private void removeSubAppointments(int userId) {
        User user = getUserById(userId);
        this.roles.remove(user);
        this.usersPermissions.remove(user);
        for (int id: this.appointments.keySet()) {
            if(id == userId) {
                int subId = this.appointments.get(user);
                this.appointments.remove(user);
                removeSubAppointments(subId);
            }
        }
    }

    public boolean validatePermission(User user, Operations op) {
        for (User u: this.usersPermissions.keySet()) {
            if(user.getId() == u.getId()) {
                return this.usersPermissions.get(u).contains(op);
            }
        }
        return false; //user is not an owner/ manager of this store
    }
    private User getUserById(int id) {
        for(User user : usersPermissions.keySet())
        {
            if(user.getId() == id)
                return user;
        }
        return null;
    }

    public Map<User, List<Operations>> getUserPermissions() {
        return this.usersPermissions;
    }

    public String getWorkersInformation(int userId) {
        StringBuilder usersInfo = new StringBuilder();
        if(validatePermission(getUserById(userId),Operations.GetWorkersInfo))
        {
            for(User user: usersPermissions.keySet())
            {
                usersInfo.append(user.toString());
            }
        }
        return usersInfo.toString();
    }

    public boolean getStorePurchaseHistory(int userId) {
        return validatePermission(getUserById(userId),Operations.ViewPurchaseHistory);
    }



}
