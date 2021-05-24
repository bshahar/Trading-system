package Domain;

import Domain.DiscountFormat.Discount;
import Domain.DiscountPolicies.DiscountCondition;
import Domain.PurchasePolicies.PurchaseCondition;
import Persistence.MemberStorePermissionsWrapper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Member {
    List<Integer> myStores;
    MemberStorePermissionsWrapper memberStorePermissionsWrapper;
    int id;

    public Member (int userId)
    {
        //this.permissions = new ConcurrentHashMap<>();
        this.myStores = new LinkedList<>();
        this.memberStorePermissionsWrapper = new MemberStorePermissionsWrapper();
        this.id = userId;
    }

    public int getId() {
        return id;
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
        memberStorePermissionsWrapper.add(p, user.getId(), store.getStoreId());
        //permissions.put(store.getStoreId(),p);
    }


    public Result addStoreOwner(User owner, User user, Store store) {
        Permission p = memberStorePermissionsWrapper.getPermission(owner,store);
        if(p!=null)
            return p.appointOwner(owner,user);
        return new Result(false, "User has not permissions");
//        if(permissions.get(store.getStoreId())!=null)
//            return permissions.get(store.getStoreId()).appointOwner(owner,user);
//        return new Result(false, "User has not permissions");

    }

    public void updateOwnerPermission(Store store) {
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
            memberStorePermissionsWrapper.add(p, id, store.getStoreId());
    }

    public Result addStoreManager(User owner,User user, Store store) {
        Permission p = memberStorePermissionsWrapper.getPermission(owner,store);
        return p.appointManager(owner,user);
        //return permissions.get(store.getStoreId()).appointManager(owner,user);
    }

    public void updateManagerPermission(Store store,User user) {
        Permission p = memberStorePermissionsWrapper.getPermission(user,store);
        if(p== null)
        {
            Permission newP = new Permission(this, store);
            newP.allowGetWorkersInfo();
            newP.allowViewMessages();
            newP.allowReplayMessages();
            memberStorePermissionsWrapper.add(newP, id, store.getStoreId());
        }
        else {
            memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"getWorkersInfo");
            memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"viewMessages");
            memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"replayMessages");
        }

//        if(!permissions.containsKey(store.getStoreId()))
//        {
//            Permission p = new Permission(this, store);
//            p.allowGetWorkersInfo();
//            p.allowViewMessages();
//            p.allowReplayMessages();
//            permissions.put(store.getStoreId(),p);
//        }
//        else {
//            permissions.get(store.getStoreId()).allowGetWorkersInfo();
//            permissions.get(store.getStoreId()).allowViewMessages();
//            permissions.get(store.getStoreId()).allowReplayMessages();
//        }

    }

    public Result addPermissions(User user, Store store, List<Integer> opIndexes) {
        Permission p = memberStorePermissionsWrapper.getPermission(user,store);
        if(p!=null && store.isManager(user) && p.addPermissions()) {
            user.updateMyPermissions(store, opIndexes);
            return new Result(true,"permissions added successfully");
        }
        return new Result(false,"permissions didn't added ");
    }
    public Result removePermissions(User user, Store store, List<Integer> opIndexes) {
        Permission p = memberStorePermissionsWrapper.getPermission(user,store);
        if(p!=null && store.isManager(user) && p.removePermission()) {
            user.disableMyPermissions(store, opIndexes);
            return new Result(true,"permissions remove successfully");
        }
        return new Result(false,"permissions didn't remove ");
    }

    public void disableMyPermissions(Store store, List<Integer> opIndexes) {
            for (int permission : opIndexes) {
                switch (permission) {
                    case 1: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                       // p.disableAddProduct();
                        break;
                    }
                    case 2: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"appointManager");
                        p.disableAppointManager();
                        break;
                    }
                    case 3: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"appointOwner");
                        p.disableAppointOwner();
                        break;
                    }
                    case 4: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"closeStore");
                        p.disableCloseStore();
                        break;
                    }
                    case 5: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"defineDiscountFormat");
                        p.disableDefineDiscountFormat();
                        break;
                    }
                    case 6: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"defineDiscountPolicy");
                        p.disableDefineDiscountPolicy();
                        break;
                    }
                    case 7: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"definePurchaseFormat");
                        p.disableDefinePurchaseFormat();
                        break;
                    }
                    case 8: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"definePurchasePolicy");
                        p.disableDefinePurchasePolicy();
                        break;

                    }
                    case 9: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"editDiscountFormat");
                        p.disableEditDiscountFormat();
                        break;
                    }
                    case 10: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                        p.disableEditDiscountPolicy();
                        break;
                    }
                    case 11: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                        p.disableEditProduct();
                        break;
                    }
                    case 12: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                        p.disableEditPurchaseFormat();
                        break;
                    }
                    case 13: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                        p.disableEditPurchasePolicy();
                        break;
                    }
                    case 14: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                        p.disableGetWorkersInfo();
                        break;
                    }
                    case 15: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                        p.disableOpenStore();
                        break;
                    }
                    case 16: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                        p.disableRemoveManagerAppointment();
                        break;
                    }
                    case 17: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                        p.disableRemoveOwnerAppointment();
                        break;

                    }
                    case 19: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                        p.disableRemoveProduct();
                        break;
                    }
                    case 20: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                        p.disableReopenStore();
                        break;
                    }
                    case 21: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                        p.disableReplayMessages();
                        break;
                    }
                    case 22: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                        p.disableViewMessages();
                        break;
                    }
                    case 23: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                        p.disableViewPurchaseHistory();
                        break;
                    }
                    case 24:{
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"addProduct");
                        p.disableResponedToOffer();
                        break;
                    }
                }
            }
    }

    public void updateMyPermissions(Store store, List<Integer> opIndexes,int userId) {
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
        //Permission p = permissions.get(store.getStoreId());
        //Permission p = memberStorePermissionsWrapper.getPermission(store.getStoreId(),id);
                switch (permissionId) {
                    case 1: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"addProduct");
                        //return p.getAddProduct()!=null;
                    }
                    case 2: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"appointManager");
                    }
                    case 3: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"appointOwner");
                    }
                    case 4: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"closeStore");
                    }
                    case 5: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"defineDiscountFormat");
                    }
                    case 6: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"defineDiscountPolicy");
                    }
                    case 7: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"definePurchaseFormat");
                    }
                    case 8: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"definePurchasePolicy");
                    }
                    case 9: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"editDiscountFormat");
                    }
                    case 10: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"editDiscountPolicy");

                    }
                    case 11: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"editProduct");
                    }
                    case 12: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"editPurchaseFormat");
                    }
                    case 13: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"editPurchasePolicy");
                    }
                    case 14: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"getWorkersInfo");
                    }
                    case 15: {

                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"openStore");
                    }
                    case 16: {

                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"removeManagerAppointment");

                    }
                    case 17: {

                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"removeOwnerAppointment");
                    }
                    case 19: {

                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"removeProduct");
                    }
                    case 20: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"reopenStore");
                    }
                    case 21: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"replayMessages");
                    }
                    case 22: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"viewMessages");
                    }
                    case 23: {
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"viewPurchaseHistory");
                    }
                    case 26:{
                        return memberStorePermissionsWrapper.checkPermission(store.getStoreId(),id,"responedToOffer");
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

