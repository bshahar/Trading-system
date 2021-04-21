package Domain;

import Domain.Member;
import Domain.Receipt;
import Domain.Product;
import Domain.Store;
import Domain.User;
import Permissions.*;

import java.util.LinkedList;
import java.util.List;

import java.util.List;

public class Permission {
    final private Member member;
    final private Store store;


    private AddProduct addProduct;
    private RemoveProduct removeProduct;
    private EditProduct editProduct;
    private AppointManager appointManager;
    private RemoveManagerAppointment removeManagerAppointment;
    private AppointOwner appointOwner;
    private RemoveOwnerAppointment removeOwnerAppointment;
    private DefinePurchasePolicy definePurchasePolicy;
    private EditPurchasePolicy editPurchasePolicy;
    private DefinePurchaseFormat definePurchaseFormat;
    private EditPurchaseFormat editPurchaseFormat;
    private DefineDiscountPolicy defineDiscountPolicy;
    private EditDiscountPolicy editDiscountPolicy;
    private DefineDiscountFormat defineDiscountFormat;
    private EditDiscountFormat editDiscountFormat;
    private CloseStore closeStore;
    private ReopenStore reopenStore;
    private GetWorkersInfo getWorkersInfo;
    private ViewMessages viewMessages;
    private ReplayMessages replayMessages;
    private ViewPurchaseHistory viewPurchaseHistory;
    private OpenStore openStore;
    private AddPermissions addPermissions;
    private RemovePermission removePermission;

    public Permission(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public void allowAddProduct()
    {
        this.addProduct = new AddProduct(this.member,this.store);
    }
    public void disableAddProduct()
    {
        if(this.addProduct == null) return;
        this.addProduct = null;
    }
    public boolean addProduct(int productId,String name, List<Product.Category> categories, double price, String description, int quantity)
    {
        if(this.addProduct!= null) {
            return this.addProduct.action(productId, name, categories, price, description, quantity);
        }
        else{
            return false;
        }
    }

    public void allowRemoveProduct()
    {
        this.removeProduct = new RemoveProduct(this.member,this.store);
    }
    public void disableRemoveProduct()
    {
        if(this.removeProduct == null) return;
        this.removeProduct = null;
    }
    public boolean removeProduct(int productId)
    {
        if(this.removeProduct!= null)
            return this.removeProduct.action(productId);
        return false;
    }

    public void allowEditProduct()
    {
        this.editProduct = new EditProduct(this.member,this.store);
    }
    public void disableEditProduct()
    {
        if(this.editProduct == null) return;
        this.editProduct = null;
    }
    public void editProduct()
    {
        if(this.editProduct!= null)
            this.editProduct.action();
    }

    public void allowAppointManager()
    {
        this.appointManager = new AppointManager(this.member,this.store);
    }
    public void disableAppointManager()
    {
        if(this.appointManager == null) return;
        this.appointManager = null;
    }
    public boolean appointManager(User owner,User user)
    {
        if(this.appointManager!= null)
            return this.appointManager.action(owner,user,this.store);
        return false;
    }

    public void allowRemoveManagerAppointment()
    {
        this.removeManagerAppointment = new RemoveManagerAppointment(this.member,this.store);
    }

    public void disableRemoveManagerAppointment()
    {
        if(this.removeManagerAppointment == null) return;
        this.removeManagerAppointment = null;
    }

    public boolean removeManagerAppointment(User ownerId,User managerId)
    {
        if(this.removeManagerAppointment!= null)
            return this.removeManagerAppointment.action(ownerId, managerId);
        return false;
    }


    public void allowAppointOwner()
    {
        this.appointOwner = new AppointOwner(this.member,this.store);
    }
    public void disableAppointOwner()
    {
        if(this.appointOwner == null) return;
        this.appointOwner = null;
    }
    public boolean appointOwner(User owner, User user)
    {
        if(this.appointOwner!= null)
            return this.appointOwner.action(owner,user,this.store);
        return false;
    }

    public void allowRemoveOwnerAppointment()
    {
        this.removeOwnerAppointment = new RemoveOwnerAppointment(this.member,this.store);
    }
    public void disableRemoveOwnerAppointment()
    {
        if(this.removeOwnerAppointment == null) return;
        this.removeOwnerAppointment = null;
    }
    public void removeOwnerAppointment()
    {
        if(this.removeOwnerAppointment!= null)
            this.removeOwnerAppointment.action();
    }


    public void allowDefinePurchasePolicy()
    {
        this.definePurchasePolicy = new DefinePurchasePolicy(this.member,this.store);
    }
    public void disableDefinePurchasePolicy()
    {
        if(this.definePurchasePolicy == null) return;
        this.definePurchasePolicy = null;
    }
    public void definePurchasePolicy()
    {
        if(this.definePurchasePolicy!= null)
            this.definePurchasePolicy.action();
    }

    public void allowEditPurchasePolicy()
    {
        this.editPurchasePolicy = new EditPurchasePolicy(this.member,this.store);
    }
    public void disableEditPurchasePolicy()
    {
        if(this.editPurchasePolicy == null) return;
        this.editPurchasePolicy = null;
    }
    public void editPurchasePolicy()
    {
        if(this.editPurchasePolicy!= null)
            this.editPurchasePolicy.action();
    }

    public void allowDefinePurchaseFormat()
    {
        this.definePurchaseFormat = new DefinePurchaseFormat(this.member,this.store);
    }
    public void disableDefinePurchaseFormat()
    {
        if(this.definePurchaseFormat == null) return;
        this.definePurchaseFormat = null;
    }
    public void definePurchaseFormat()
    {
        if(this.definePurchaseFormat!= null)
            this.definePurchaseFormat.action();
    }

    public void allowEditPurchaseFormat()
    {
        this.editPurchaseFormat = new EditPurchaseFormat(this.member,this.store);
    }
    public void disableEditPurchaseFormat()
    {
        if(this.editPurchaseFormat == null) return;
        this.editPurchaseFormat = null;
    }
    public void editPurchaseFormat()
    {
        if(this.editPurchaseFormat!= null)
            this.editPurchaseFormat.action();
    }

    public void allowDefineDiscountPolicy()
    {
        this.defineDiscountPolicy = new DefineDiscountPolicy(this.member,this.store);
    }
    public void disableDefineDiscountPolicy()
    {
        if(this.defineDiscountPolicy == null) return;
        this.defineDiscountPolicy = null;
    }
    public void defineDiscountPolicy()
    {
        if(this.defineDiscountPolicy!= null)
            this.defineDiscountPolicy.action();
    }

    public void allowEditDiscountPolicy()
    {
        this.editDiscountPolicy = new EditDiscountPolicy(this.member,this.store);
    }
    public void disableEditDiscountPolicy()
    {
        if(this.editDiscountPolicy == null) return;
        this.editDiscountPolicy = null;
    }
    public void defineEditDiscountPolicy()
    {
        if(this.editDiscountPolicy!= null)
            this.editDiscountPolicy.action();
    }

    public void allowDefineDiscountFormat()
    {
        this.defineDiscountFormat = new DefineDiscountFormat(this.member,this.store);
    }
    public void disableDefineDiscountFormat()
    {
        if(this.defineDiscountFormat == null) return;
        this.defineDiscountFormat = null;
    }
    public void defineDiscountFormat()
    {
        if(this.defineDiscountFormat!= null)
            this.defineDiscountFormat.action();
    }

    public void allowEditDiscountFormat()
    {
        this.editDiscountFormat = new EditDiscountFormat(this.member,this.store);
    }
    public void disableEditDiscountFormat()
    {
        if(this.editDiscountFormat == null) return;
        this.editDiscountFormat = null;
    }
    public void editDiscountFormat()
    {
        if(this.editDiscountFormat!= null)
            this.editDiscountFormat.action();
    }

    public void allowCloseStore()
    {
        this.closeStore = new CloseStore(this.member,this.store);
    }
    public void disableCloseStore()
    {
        if(this.closeStore == null) return;
        this.closeStore = null;
    }
    public void closeStore()
    {
        if(this.closeStore!= null)
            this.closeStore.action();
    }

    public void allowReopenStore()
    {
        this.reopenStore = new ReopenStore(this.member,this.store);
    }
    public void disableReopenStore()
    {
        if(this.reopenStore == null) return;
        this.reopenStore = null;
    }
    public void reopenStore()
    {
        if(this.reopenStore!= null)
            this.reopenStore.action();
    }

    public void allowGetWorkersInfo()
    {
        this.getWorkersInfo = new GetWorkersInfo(this.member,this.store);
    }
    public void disableGetWorkersInfo()
    {
        if(this.getWorkersInfo == null) return;
        this.getWorkersInfo = null;
    }
    public List<User> getWorkersInfo()
    {
        if(this.getWorkersInfo!= null)
            return this.getWorkersInfo.action();
        return null;
    }

    public void allowViewMessages()
    {
        this.viewMessages = new ViewMessages(this.member,this.store);
    }
    public void disableViewMessages()
    {
        if(this.viewMessages == null) return;
        this.viewMessages = null;
    }
    public void viewMessages()
    {
        if(this.viewMessages!= null)
            this.viewMessages.action();
    }

    public void allowReplayMessages()
    {
        this.replayMessages = new ReplayMessages(this.member,this.store);
    }
    public void disableReplayMessages()
    {
        if(this.replayMessages == null) return;
        this.replayMessages = null;
    }
    public void replayMessages()
    {
        if(this.replayMessages!= null)
            this.replayMessages.action();
    }

    public void allowViewPurchaseHistory()
    {
        this.viewPurchaseHistory = new ViewPurchaseHistory(this.member,this.store);
    }
    public void disableViewPurchaseHistory()
    {
        if(this.viewPurchaseHistory == null) return;
        this.viewPurchaseHistory = null;
    }
    public List<Receipt> viewPurchaseHistory()
    {
        if(this.viewPurchaseHistory!= null)
            return this.viewPurchaseHistory.action();
        return new LinkedList<>();
    }

    public void allowOpenStore()
    {
        this.openStore = new OpenStore(this.member,this.store);
    }
    public void disableOpenStore()
    {
        if(this.openStore == null) return;
        this.openStore = null;
    }
    public void openStore()
    {
        if(this.openStore!= null)
            this.openStore.action();
    }

    public void allowAddPermissions()
    {
        this.addPermissions = new AddPermissions(this.member,this.store);
    }
    public void disableAddPermissions()
    {
        if(this.addPermissions == null) return;
        this.addPermissions = null;
    }
    public boolean addPermissions()
    {
        if(this.addPermissions!= null)
            return this.addPermissions.action();
        return false;
    }

    public void allowRemovePermission()
    {
        this.removePermission = new RemovePermission(this.member,this.store);
    }
    public void disableRemovePermission()
    {
        if(this.removePermission == null) return;
        this.removePermission = null;
    }
    public boolean removePermission()
    {
        if(this.removePermission!= null)
            return this.removePermission.action();
        return false;
    }












}
