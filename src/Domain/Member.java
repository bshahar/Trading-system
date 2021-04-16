package Domain;

import Permissions.Permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Member {
    Map<Store, Permission> permissions;

    public Member ()
    {
        this.permissions = new ConcurrentHashMap<>();
    }
    public void openStore(Store store)
    {
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
        p.allowViewPurchaseHistor();
        permissions.put(store,p);
    }


    public boolean addStoreOwner(User user, Store store) {
        return permissions.get(store).appointOwner(user);

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
            p.allowViewPurchaseHistor();
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
            permissions.get(store).allowViewPurchaseHistor();
        }


    }

    public boolean addStoreManager(User user, Store store) {
        return permissions.get(store).appointManager(user);
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

    public boolean addProductToStore(int productId,Store store, String name, List<Product.Category> categories, double price, String description, int quantity) {
        if(permissions.containsKey(store)){
            Permission permission= permissions.get(store);
            return permission.addProduct(productId,name, categories, price, description, quantity);

        }else{
            return false;
        }
    }

    public boolean removeProductFromStore(Store store, int productId) {
        if(!permissions.containsKey(store)){
            Permission permission= permissions.get(store);
            return permission.removeProduct(productId);
        }else{
            return false;
        }
    }

    public boolean removeMangerFromStore(User owner,User manager, Store store) {
        if(!permissions.containsKey(store)){
            Permission permission= permissions.get(store);
            return permission.removeManagerAppointment(owner, manager);
        }else{
            return false;
        }
    }
}
