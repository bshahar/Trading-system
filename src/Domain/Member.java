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

    public Permission getPermissions(Store store){
        return memberStorePermissionsWrapper.getPermission(this,store);
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
        Permission p = memberStorePermissionsWrapper.getPermission(this,store);
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
        Permission p = memberStorePermissionsWrapper.getPermission(this,store);
        return p.appointManager(owner,user);
        //return permissions.get(store.getStoreId()).appointManager(owner,user);
    }

    public void updateManagerPermission(Store store) {
        Permission p = memberStorePermissionsWrapper.getPermission(this,store);
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
        Permission p = memberStorePermissionsWrapper.getPermission(this,store);
        if(p!=null && store.isManager(user) && p.addPermissions()) {
            user.updateMyPermissions(store, opIndexes);
            return new Result(true,"permissions added successfully");
        }
        return new Result(false,"permissions didn't added ");
    }
    public Result removePermissions(User user, Store store, List<Integer> opIndexes) {
        Permission p = memberStorePermissionsWrapper.getPermission(this,store);
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
                        break;
                    }
                    case 2: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"appointManager");
                        break;
                    }
                    case 3: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"appointOwner");
                        break;
                    }
                    case 4: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"closeStore");
                        break;
                    }
                    case 5: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"defineDiscountFormat");
                        break;
                    }
                    case 6: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"defineDiscountPolicy");
                        break;
                    }
                    case 7: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"definePurchaseFormat");
                        break;
                    }
                    case 8: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"definePurchasePolicy");
                        break;

                    }
                    case 9: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"editDiscountFormat");
                        break;
                    }
                    case 10: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"editDiscountPolicy");
                        break;
                    }
                    case 11: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"editProduct");
                        break;
                    }
                    case 12: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"editPurchaseFormat");
                        break;
                    }
                    case 13: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"disableEeditPurchasePolicy");
                        break;
                    }
                    case 14: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"getWorkersInfo");
                        break;
                    }
                    case 15: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"openStore");
                        break;
                    }
                    case 16: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"removeManagerAppointment");
                        break;
                    }
                    case 17: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"removeOwnerAppointment");
                        break;

                    }
                    case 19: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"removeProduct");
                        break;
                    }
                    case 20: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"reopenStore");
                        break;
                    }
                    case 21: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"replayMessages");
                        break;
                    }
                    case 22: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"viewMessages");
                        break;
                    }
                    case 23: {
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"viewPurchaseHistory");
                        break;
                    }
                    case 24:{
                        memberStorePermissionsWrapper.disablePermissionOnUser(id,store.getStoreId(),"responedToOffer");
                        break;
                    }
                }
            }
    }

    public void updateMyPermissions(Store store, List<Integer> opIndexes) {
        for (int permission : opIndexes) {
            switch (permission) {
                case 1: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"addProduct");
                    break;
                }
                case 2: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"appointManager");
                    break;
                }
                case 3: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"appointOwner");
                    break;
                }
                case 4: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"closeStore");
                    break;
                }
                case 5: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"defineDiscountFormat");
                    break;
                }
                case 6: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"defineDiscountPolicy");
                    break;
                }
                case 7: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"definePurchaseFormat");
                    break;
                }
                case 8: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"definePurchasePolicy");
                    break;

                }
                case 9: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"editDiscountFormat");
                    break;
                }
                case 10: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"editDiscountPolicy");
                    break;
                }
                case 11: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"editProduct");
                    break;
                }
                case 12: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"editPurchaseFormat");
                    break;
                }
                case 13: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"disableEeditPurchasePolicy");
                    break;
                }
                case 14: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"getWorkersInfo");
                    break;
                }
                case 15: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"openStore");
                    break;
                }
                case 16: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"removeManagerAppointment");
                    break;
                }
                case 17: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"removeOwnerAppointment");
                    break;

                }
                case 19: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"removeProduct");
                    break;
                }
                case 20: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"reopenStore");
                    break;
                }
                case 21: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"replayMessages");
                    break;
                }
                case 22: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"viewMessages");
                    break;
                }
                case 23: {
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"viewPurchaseHistory");
                    break;
                }
                case 24:{
                    memberStorePermissionsWrapper.enablePermissionOnUser(id,store.getStoreId(),"responedToOffer");
                    break;
                }
            }
        }
    }


    public Result getWorkersInformation(Store store) {
        Permission p = memberStorePermissionsWrapper.getPermission(this,store);
        if(p==null) return new Result(false,"User has no permissions");
        return p.getWorkersInfo();
    }

    public Result getStorePurchaseHistory(Store store) {
        Permission p = memberStorePermissionsWrapper.getPermission(this,store);
        if(p==null) return new Result(false,"User has not permissions");
        return p.viewPurchaseHistory();
    }

    public boolean addProductToStore(int productId,Store store, String name, List<String> categories, double price, String description, int quantity) {
        Permission p = memberStorePermissionsWrapper.getPermission(this,store);
        if(p!=null){
            return p.addProduct(productId,name, categories, price, description, quantity);

        }else{
            return false;
        }
    }

    public Result removeProductFromStore(Store store, int productId) {
        Permission p = memberStorePermissionsWrapper.getPermission(this,store);
        if(p!=null){
            return p.removeProduct(productId);
        }else{
            return new Result(false,"User have no permissions");
        }
    }

    public Result addDiscountPolicy(Store store, String condition, String param, String category, int prodId, Date begin, Date end, DiscountCondition conditions, int percentage, Discount.MathOp op) {
        Permission p = memberStorePermissionsWrapper.getPermission(this,store);
        if(p!=null) {
            return p.defineDiscountPolicy(param, condition, category, prodId, begin, end, conditions, percentage, op);
        }
        else
            return new Result(false,"User has no permission for this action.");
    }


    public Result removeDiscountPolicy(Store store, int prodId, String category) {
        Permission p = memberStorePermissionsWrapper.getPermission(this,store);
        if(p!=null) {
            return p.defineEditDiscountPolicy(prodId, category);
        }
        else
            return new Result(false,"User has no permission for this action.");
    }

    public Result responedToOffer(Store store, int prodId, int offerId, String responed, double counterOffer, String option) {
        Permission permissions = memberStorePermissionsWrapper.getPermission(this,store);
        if(permissions!=null) {
            return permissions.responedToOffer(prodId,offerId, responed, counterOffer, option);
        }
        else
            return new Result(false,"User has no permission for this action.");
    }


    public Result removeMangerFromStore(User owner,User manager, Store store) {
        Permission permissions = memberStorePermissionsWrapper.getPermission(this,store);
        if(permissions!=null) {
            return (permissions.removeManagerAppointment(owner, manager));
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
        Permission permissions = memberStorePermissionsWrapper.getPermission(this,store);
        if(permissions!=null) {
            return permissions.editPurchasePolicy(prodId, category);
        }
        else
            return new Result(false,"User has no permission for this action.");
    }

    public Result getDiscountPolicies(Store store,int prodId, String category) {
        Permission permissions = memberStorePermissionsWrapper.getPermission(this,store);
        if(permissions!=null) {
            return permissions.defineViewDiscountPolicies(prodId, category);
        }
        else
            return new Result(false,"User has no permission for this action.");
    }

    public Result getPurchasePolicy(Store store, int prodId, String category) {
        Permission permissions = memberStorePermissionsWrapper.getPermission(this,store);
        if(permissions!=null) {
            return permissions.defineViewPurchasePolicies(prodId, category);
        }
        else
            return new Result(false,"User has no permission for this action.");
    }



    public Result removeOwnerFromStore(User owner, User ownerToRemove, Store store) {
        Permission permissions = memberStorePermissionsWrapper.getPermission(this,store);
        if(permissions!=null) {
            return (permissions.removeOwnerAppointment(owner, ownerToRemove));
        }else{
            return new Result(false,"User has no permissions");
        }
    }

    public Result editProduct(Store store,Product product, int price,int amount) {
        Permission permissions = memberStorePermissionsWrapper.getPermission(this, store);
        if (permissions != null) {
            return permissions.editProduct(product, price, amount);
        }
        else{
            return new Result(false,"User has no permissions");
        }
    }

    public Result addPurchasePolicy(Store store,String param, String category, int prodId, PurchaseCondition conditions) {
            Permission permissions = memberStorePermissionsWrapper.getPermission(this,store);
            if(permissions!=null) {
            return permissions.definePurchasePolicy(param, category, prodId, conditions);
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
        memberStorePermissionsWrapper.add(p, id, store.getStoreId());
        //permissions.put(store.getStoreId(),p);
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
            memberStorePermissionsWrapper.add(p, id, s.getStoreId());
        }
    }


}

