package Domain;

import Domain.DiscountFormat.Discount;
import Domain.DiscountPolicies.DiscountCondition;
import Domain.PurchasePolicies.PurchaseCondition;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Member {
    Map<Integer, Permission> permissions; //integer=storeId
    List<Integer> myStores;

    public Member ()
    {
        this.permissions = new ConcurrentHashMap<>();
        this.myStores = new LinkedList<>();
    }


    public Map<Integer, Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<Integer, Permission> permissions) {
        this.permissions = permissions;
    }

    public void setMyStores(List<Integer> myStores) {
        this.myStores = myStores;
    }

    public void openStore(User user, Store store)
    {
        store.addOwner(user);
        myStores.add(store.getStoreId());
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
        p.allowViewDiscountPolicies();
        p.allowViewPurchasePolicies();
        p.allowResponedToOffer();
        permissions.put(store.getStoreId(),p);
    }


    public Result addStoreOwner(User owner, User user, Store store) {
        if(permissions.get(store.getStoreId())!=null)
            return permissions.get(store.getStoreId()).appointOwner(owner,user);
        return new Result(false, "User has not permissions");

    }

    public void updateOwnerPermission(Store store) {
        if(!permissions.containsKey(store.getStoreId()))
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
            p.allowViewDiscountPolicies();
            p.allowViewPurchasePolicies();
            p.allowResponedToOffer();
            permissions.put(store.getStoreId(),p);
        }
        else {
            permissions.get(store.getStoreId()).allowAppointOwner();
            permissions.get(store.getStoreId()).allowAddProduct();
            permissions.get(store.getStoreId()).allowRemoveProduct();
            permissions.get(store.getStoreId()).allowEditProduct();
            permissions.get(store.getStoreId()).allowAppointManager();
            permissions.get(store.getStoreId()).allowRemoveManagerAppointment();
            permissions.get(store.getStoreId()).allowAppointOwner();
            permissions.get(store.getStoreId()).allowRemoveOwnerAppointment();
            permissions.get(store.getStoreId()).allowDefinePurchasePolicy();
            permissions.get(store.getStoreId()).allowEditPurchasePolicy();
            permissions.get(store.getStoreId()).allowDefinePurchaseFormat();
            permissions.get(store.getStoreId()).allowEditPurchaseFormat();
            permissions.get(store.getStoreId()).allowDefineDiscountPolicy();
            permissions.get(store.getStoreId()).allowEditDiscountPolicy();
            permissions.get(store.getStoreId()).allowDefineDiscountFormat();
            permissions.get(store.getStoreId()).allowEditDiscountFormat();
            permissions.get(store.getStoreId()).allowReopenStore();
            permissions.get(store.getStoreId()).allowGetWorkersInfo();
            permissions.get(store.getStoreId()).allowViewMessages();
            permissions.get(store.getStoreId()).allowReplayMessages();
            permissions.get(store.getStoreId()).allowViewPurchaseHistory();
            permissions.get(store.getStoreId()).allowAddPermissions();
            permissions.get(store.getStoreId()).allowRemovePermission();
            permissions.get(store.getStoreId()).allowViewDiscountPolicies();
            permissions.get(store.getStoreId()).allowViewPurchasePolicies();
            permissions.get(store.getStoreId()).allowResponedToOffer();
        }


    }

    public Result addStoreManager(User owner,User user, Store store) {
        return permissions.get(store.getStoreId()).appointManager(owner,user);
    }

    public void updateManagerPermission(Store store) {
        if(!permissions.containsKey(store.getStoreId()))
        {
            Permission p = new Permission(this, store);
            p.allowGetWorkersInfo();
            p.allowViewMessages();
            p.allowReplayMessages();
            permissions.put(store.getStoreId(),p);
        }
        else {
            permissions.get(store.getStoreId()).allowGetWorkersInfo();
            permissions.get(store.getStoreId()).allowViewMessages();
            permissions.get(store.getStoreId()).allowReplayMessages();
        }

    }

    public Result addPermissions(User user, Store store, List<Integer> opIndexes) {
        if(permissions.get(store.getStoreId())!=null && store.isManager(user) && permissions.get(store.getStoreId()).addPermissions()) {
            user.updateMyPermissions(store, opIndexes);
            return new Result(true,"permissions added successfully");
        }
        return new Result(false,"permissions didn't added ");
    }
    public Result removePermissions(User user, Store store, List<Integer> opIndexes) {
        if(permissions.get(store.getStoreId())!=null && store.isManager(user) &&permissions.get(store.getStoreId()).removePermission()) {
            user.disableMyPermissions(store, opIndexes);
            return new Result(true,"permissions remove successfully");
        }
        return new Result(false,"permissions didn't remove ");
    }

    public void disableMyPermissions(Store store, List<Integer> opIndexes) {
        Permission p = permissions.get(store.getStoreId());
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
                    case 24:{
                        p.disableResponedToOffer();
                        break;
                    }
                }
            }
        }
    }

    public void updateMyPermissions(Store store, List<Integer> opIndexes) {
        Permission p = permissions.get(store.getStoreId());
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
        if(!permissions.containsKey(store.getStoreId())) return new Result(false,"User has no permissions");
        return permissions.get(store.getStoreId()).getWorkersInfo();
    }

    public Result getStorePurchaseHistory(Store store) {
        if(!permissions.containsKey(store.getStoreId())) return new Result(false,"User has not permissions");
        return permissions.get(store.getStoreId()).viewPurchaseHistory();
    }

    public boolean addProductToStore(int productId,Store store, String name, List<String> categories, double price, String description, int quantity) {
        if(permissions.containsKey(store.getStoreId())){
            Permission permission= permissions.get(store.getStoreId());
            return permission.addProduct(productId,name, categories, price, description, quantity);

        }else{
            return false;
        }
    }

    public Result removeProductFromStore(Store store, int productId) {
        if(permissions.containsKey(store.getStoreId())){
            Permission permission= permissions.get(store.getStoreId());
            return permission.removeProduct(productId);
        }else{
            return new Result(false,"User have no permissions");
        }
    }

    public Result addDiscountPolicy(Store store, String condition, String param, String category, int prodId, Date begin, Date end, DiscountCondition conditions, int percentage, Discount.MathOp op) {
        if(permissions.containsKey(store.getStoreId())) {
            Permission permission = permissions.get(store.getStoreId());
            return permission.defineDiscountPolicy(param, condition, category, prodId, begin, end, conditions, percentage, op);
        }
        else
            return new Result(false,"User has no permission for this action.");
    }


    public Result removeDiscountPolicy(Store store, int prodId, String category) {
        if(permissions.containsKey(store.getStoreId())) {
            Permission permission = permissions.get(store.getStoreId());
            return permission.defineEditDiscountPolicy(prodId, category);
        }
        else
            return new Result(false,"User has no permission for this action.");
    }

    public Result responedToOffer(Store store, int prodId, int offerId, String responed, double counterOffer, String option) {
        if(permissions.containsKey(store.getStoreId())) {
            Permission permission = permissions.get(store.getStoreId());
            return permission.responedToOffer(prodId,offerId, responed, counterOffer, option);
        }
        else
            return new Result(false,"User has no permission for this action.");
    }


    public Result removeMangerFromStore(User owner,User manager, Store store) {
        if(permissions.containsKey(store.getStoreId())){
            Permission permission= permissions.get(store.getStoreId());
            return (permission.removeManagerAppointment(owner, manager));
        }else{
            return new Result(false,"User has no permissions");
        }
    }

    public List<Integer> getMyStores() {
        return this.myStores;
    }

    public void addToMyStores(Store store)
    {
        myStores.add(store.getStoreId());
    }

    public void removeFromMyStores(Store store)
    {
        for(Integer id: myStores){
            if(id==store.getStoreId()){
                myStores.remove(id);
            }
        }
    }

    public List<Permission> getPermissionsOfStore(int storeId) {
        return null;
    }

    public boolean checkPermissions(Store store ,int permissionId) {
        Permission p = permissions.get(store.getStoreId());
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
                    case 26:{
                        return p.getResponedToOffer()!=null;
                    }
                }
            }
        return false;
        }

    public Result removePurchasePolicy(Store store, int prodId, String category) {
        if(permissions.containsKey(store.getStoreId())) {
            Permission permission = permissions.get(store.getStoreId());
            return permission.editPurchasePolicy(prodId, category);
        }
        else
            return new Result(false,"User has no permission for this action.");
    }

    public Result getDiscountPolicies(Store store,int prodId, String category) {
        if(permissions.containsKey(store.getStoreId())){
            Permission permission = permissions.get(store.getStoreId());
            return permission.defineViewDiscountPolicies(prodId, category);
        }
        else
            return new Result(false,"User has no permission for this action.");
    }

    public Result getPurchasePolicy(Store store, int prodId, String category) {
        if(permissions.containsKey(store.getStoreId())){
            Permission permission = permissions.get(store.getStoreId());
            return permission.defineViewPurchasePolicies(prodId, category);
        }
        else
            return new Result(false,"User has no permission for this action.");
    }



    public Result removeOwnerFromStore(User owner, User ownerToRemove, Store store) {
        if(permissions.containsKey(store.getStoreId())){
            Permission permission= permissions.get(store.getStoreId());
            return (permission.removeOwnerAppointment(owner, ownerToRemove));
        }else{
            return new Result(false,"User has no permissions");
        }
    }

    public Result editProduct(Store store,Product product, int price,int amount) {
        if(permissions.get(store.getStoreId())!=null)
            return permissions.get(store.getStoreId()).editProduct(product,price,amount);
        return new Result(false,"user has no permissions");
    }

    public Result addPurchasePolicy(Store store,String param, String category, int prodId, PurchaseCondition conditions) {
        if(permissions.containsKey(store.getStoreId())) {
            Permission permission = permissions.get(store.getStoreId());
            return permission.definePurchasePolicy(param, category, prodId, conditions);
        }
        else
            return new Result(false,"User has no permission for this action.");
    }

    public void addStoreToSystemManager(Store store){
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
        permissions.put(store.getStoreId(),p);
    }

    public void removeStoreToSystemManager(Store store){
    }

    public void setSystemManagerPermission(List<Store> stores) {
        for(Store s: stores){
            Permission p = new Permission(this, s);
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
            permissions.put(s.getStoreId(),p);
        }
    }


}

