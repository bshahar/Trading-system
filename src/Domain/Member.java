package Domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Member {
    Map<Store, Permission> permissions;
    List<Store> myStores;

    public Member ()
    {
        this.permissions = new ConcurrentHashMap<>();
        this.myStores = new LinkedList<>();
    }
    public void openStore(User user, Store store)
    {
        store.addOwner(user);
        myStores.add(store);
        Permission p = new Permission(this, store);
        p.allowOpenStore();
        p.openStore();
        p.allowAppointOwner();
        p.allowAddProduct();
        p.allowRemoveProduct();
        p.allowEditProduct();
        p.allowAppointManager();
        p.allowRemoveManagerAppointment();
        p.allowAppointOwner();
        p.allowRemoveOwnerAppointment();
        p.allowDefinePurchasePolicy();
        p.allowEditPurchasePolicy();
        p.allowDefinePurchaseFormat();
        p.allowEditPurchaseFormat();
        p.allowDefineDiscountPolicy();
        p.allowEditDiscountPolicy();
        p.allowDefineDiscountFormat();
        p.allowEditDiscountFormat();
        p.allowReopenStore();
        p.allowGetWorkersInfo();
        p.allowViewMessages();
        p.allowReplayMessages();
        p.allowViewPurchaseHistory();
        p.allowAddPermissions();
        p.allowRemovePermission();
        permissions.put(store,p);
    }


    public Result addStoreOwner(User owner, User user, Store store) {
        if(permissions.get(store)!=null)
            return permissions.get(store).appointOwner(owner,user);
        return new Result(false, "User has not permissions");

    }

    public void updateOwnerPermission(Store store) {
        if(!permissions.containsKey(store))
        {
            Permission p = new Permission(this, store);
            p.allowAppointOwner();
            p.allowAddProduct();
            p.allowRemoveProduct();
            p.allowEditProduct();
            p.allowAppointManager();
            p.allowRemoveManagerAppointment();
            p.allowAppointOwner();
            p.allowRemoveOwnerAppointment();
            p.allowDefinePurchasePolicy();
            p.allowEditPurchasePolicy();
            p.allowDefinePurchaseFormat();
            p.allowEditPurchaseFormat();
            p.allowDefineDiscountPolicy();
            p.allowEditDiscountPolicy();
            p.allowDefineDiscountFormat();
            p.allowEditDiscountFormat();
            p.allowReopenStore();
            p.allowGetWorkersInfo();
            p.allowViewMessages();
            p.allowReplayMessages();
            p.allowViewPurchaseHistory();
            p.allowAddPermissions();
            p.allowRemovePermission();
            permissions.put(store,p);
        }
        else {
            permissions.get(store).allowAppointOwner();
            permissions.get(store).allowAddProduct();
            permissions.get(store).allowRemoveProduct();
            permissions.get(store).allowEditProduct();
            permissions.get(store).allowAppointManager();
            permissions.get(store).allowRemoveManagerAppointment();
            permissions.get(store).allowAppointOwner();
            permissions.get(store).allowRemoveOwnerAppointment();
            permissions.get(store).allowDefinePurchasePolicy();
            permissions.get(store).allowEditPurchasePolicy();
            permissions.get(store).allowDefinePurchaseFormat();
            permissions.get(store).allowEditPurchaseFormat();
            permissions.get(store).allowDefineDiscountPolicy();
            permissions.get(store).allowEditDiscountPolicy();
            permissions.get(store).allowDefineDiscountFormat();
            permissions.get(store).allowEditDiscountFormat();
            permissions.get(store).allowReopenStore();
            permissions.get(store).allowGetWorkersInfo();
            permissions.get(store).allowViewMessages();
            permissions.get(store).allowReplayMessages();
            permissions.get(store).allowViewPurchaseHistory();
            permissions.get(store).allowAddPermissions();
            permissions.get(store).allowRemovePermission();
        }


    }

    public Result addStoreManager(User owner,User user, Store store) {
        return permissions.get(store).appointManager(owner,user);
    }

    public void updateManagerPermission(Store store) {
        if(!permissions.containsKey(store))
        {
            Permission p = new Permission(this, store);
            p.allowGetWorkersInfo();
            p.allowViewMessages();
            p.allowReplayMessages();
            permissions.put(store,p);
        }
        else {
            permissions.get(store).allowGetWorkersInfo();
            permissions.get(store).allowViewMessages();
            permissions.get(store).allowReplayMessages();
        }

    }

    public Result addPermissions(User user, Store store, List<Integer> opIndexes) {
        if(permissions.get(store)!=null && store.isManager(user) && permissions.get(store).addPermissions()) {
            user.updateMyPermissions(store, opIndexes);
            return new Result(true,"permissions added successfully");
        }
        return new Result(false,"permissions didn't added ");
    }
    public Result removePermissions(User user, Store store, List<Integer> opIndexes) {
        if(permissions.get(store)!=null && store.isManager(user) &&permissions.get(store).removePermission()) {
            user.disableMyPermissions(store, opIndexes);
            return new Result(true,"permissions remove successfully");
        }
        return new Result(false,"permissions didn't remove ");
    }



    public void disableMyPermissions(Store store, List<Integer> opIndexes) {
        Permission p = permissions.get(store);
        if(p!= null) {
            for (int permission : opIndexes) {
                switch (permission) {
                    case 1: {
                        p.disableAddProduct();
                        break;
                    }
                    case 2: {
                        p.disableAppointManager();
                        break;
                    }
                    case 3: {
                        p.disableAppointOwner();
                        break;
                    }
                    case 4: {
                        p.disableCloseStore();
                        break;
                    }
                    case 5: {
                        p.disableDefineDiscountFormat();
                        break;
                    }
                    case 6: {
                        p.disableDefineDiscountPolicy();
                        break;
                    }
                    case 7: {
                        p.disableDefinePurchaseFormat();
                        break;
                    }
                    case 8: {
                        p.disableDefinePurchasePolicy();
                        break;

                    }
                    case 9: {
                        p.disableEditDiscountFormat();
                        break;
                    }
                    case 10: {
                        p.disableEditDiscountPolicy();
                        break;
                    }
                    case 11: {
                        p.disableEditProduct();
                        break;
                    }
                    case 12: {
                        p.disableEditPurchaseFormat();
                        break;
                    }
                    case 13: {
                        p.disableEditPurchasePolicy();
                        break;
                    }
                    case 14: {
                        p.disableGetWorkersInfo();
                        break;
                    }
                    case 15: {
                        p.disableOpenStore();
                        break;
                    }
                    case 16: {
                        p.disableRemoveManagerAppointment();
                        break;
                    }
                    case 17: {
                        p.disableRemoveOwnerAppointment();
                        break;
                    }
                    case 19: {
                        p.disableRemoveProduct();
                        break;
                    }
                    case 20: {
                        p.disableReopenStore();
                        break;
                    }
                    case 21: {
                        p.disableReplayMessages();
                        break;
                    }
                    case 22: {
                        p.disableViewMessages();
                        break;
                    }
                    case 23: {
                        p.disableViewPurchaseHistory();
                        break;
                    }
                }
            }
        }
    }



    public void updateMyPermissions(Store store, List<Integer> opIndexes) {
        Permission p = permissions.get(store);
        if(p!= null) {
            for (int permission : opIndexes) {
                switch (permission) {
                    case 1: {
                        p.allowAddProduct();
                        break;
                    }
                    case 2: {
                        p.allowAppointManager();
                        break;
                    }
                    case 3: {
                        p.allowAppointOwner();
                        break;
                    }
                    case 4: {
                        p.allowCloseStore();
                        break;
                    }
                    case 5: {
                        p.allowDefineDiscountFormat();
                        break;
                    }
                    case 6: {
                        p.allowDefineDiscountPolicy();
                        break;
                    }
                    case 7: {
                        p.allowDefinePurchaseFormat();
                        break;
                    }
                    case 8: {
                        p.allowDefinePurchasePolicy();
                        break;

                    }
                    case 9: {
                        p.allowEditDiscountFormat();
                        break;
                    }
                    case 10: {
                        p.allowEditDiscountPolicy();
                        break;
                    }
                    case 11: {
                        p.allowEditProduct();
                        break;
                    }
                    case 12: {
                        p.allowEditPurchaseFormat();
                        break;
                    }
                    case 13: {
                        p.allowEditPurchasePolicy();
                        break;
                    }
                    case 14: {
                        p.allowGetWorkersInfo();
                        break;
                    }
                    case 15: {
                        p.allowOpenStore();
                        break;
                    }
                    case 16: {
                        p.allowRemoveManagerAppointment();
                        break;
                    }
                    case 17: {
                        p.allowRemoveOwnerAppointment();
                        break;
                    }
                    case 19: {
                        p.allowRemoveProduct();
                        break;
                    }
                    case 20: {
                        p.allowReopenStore();
                        break;
                    }
                    case 21: {
                        p.allowReplayMessages();
                        break;
                    }
                    case 22: {
                        p.allowViewMessages();
                        break;
                    }
                    case 23: {
                        p.allowViewPurchaseHistory();
                        break;
                    }
                }
            }
        }
    }

    public Result getWorkersInformation(Store store) {
        if(!permissions.containsKey(store)) return new Result(false,"User has no permissions");
        return permissions.get(store).getWorkersInfo();
    }

    public Result getStorePurchaseHistory(Store store) {
        if(!permissions.containsKey(store)) return new Result(false,"User has not permissions");
        return permissions.get(store).viewPurchaseHistory();
    }

    public boolean addProductToStore(int productId,Store store, String name, List<Product.Category> categories, double price, String description, int quantity) {
        if(permissions.containsKey(store)){
            Permission permission= permissions.get(store);
            return permission.addProduct(productId,name, categories, price, description, quantity);

        }else{
            return false;
        }
    }

    public Result removeProductFromStore(Store store, int productId) {
        if(permissions.containsKey(store)){
            Permission permission= permissions.get(store);
            return permission.removeProduct(productId);
        }else{
            return new Result(false,"User have no permissions");
        }
    }

    public Result removeMangerFromStore(User owner,User manager, Store store) {
        if(permissions.containsKey(store)){
            Permission permission= permissions.get(store);
            return (permission.removeManagerAppointment(owner, manager));
        }else{
            return new Result(false,"User has no permissions");
        }
    }

    public List<Store> getMyStores() {
        return this.myStores;
    }
    public void addToMyStores(Store store)
    {
        myStores.add(store);
    }

    public List<Permission> getPermissionsOfStore(int storeId) {
        return null;
    }

    public boolean checkPermissions(Store store ,int permissionId) {
        Permission p = permissions.get(store);
        if(p!= null) {
                switch (permissionId) {
                    case 1: {
                        return p.getAddProduct()!=null;
                    }
                    case 2: {
                        return p.getAppointManager()!=null;
                    }
                    case 3: {
                        return p.getAppointOwner()!=null;
                    }
                    case 4: {
                        return p.getCloseStore()!=null;
                    }
                    case 5: {
                        return p.getDefineDiscountFormat()!=null;
                    }
                    case 6: {
                        return p.getDefineDiscountPolicy()!=null;
                    }
                    case 7: {
                        return p.getDefinePurchaseFormat()!=null;
                    }
                    case 8: {
                        return p.getDefinePurchasePolicy()!=null;
                    }
                    case 9: {
                        return p.getEditDiscountFormat()!=null;
                    }
                    case 10: {
                        return p.getEditDiscountPolicy()!=null;
                    }
                    case 11: {
                        return p.getEditProduct()!=null;
                    }
                    case 12: {
                        return p.getEditPurchaseFormat()!=null;
                    }
                    case 13: {
                        return p.getEditPurchasePolicy()!=null;
                    }
                    case 14: {
                        return p.getGetWorkersInfo()!=null;
                    }
                    case 15: {
                        return p.getOpenStore()!=null;
                    }
                    case 16: {
                        return p.getRemoveManagerAppointment()!=null;
                    }
                    case 17: {
                        return p.getRemoveOwnerAppointment()!=null;
                    }
                    case 19: {
                        return p.getRemoveProduct()!=null;
                    }
                    case 20: {
                        return p.getReopenStore()!=null;
                    }
                    case 21: {
                        return p.getReplayMessages()!=null;
                    }
                    case 22: {
                        return p.getViewMessages()!=null;
                    }
                    case 23: {
                        return p.getViewPurchaseHistory()!=null;
                    }
                }
            }
        return false;
        }
    }

