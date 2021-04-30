package Domain;

import Domain.DiscountFormat.Discount;
import Domain.DiscountPolicies.DiscountCondition;
import Domain.Member;
import Domain.PurchasePolicies.PurchaseCondition;
import Domain.PurchasePolicies.PurchasePolicy;
import Domain.Receipt;
import Domain.Product;
import Domain.Store;
import Domain.User;
import Permissions.*;

import java.util.Date;
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
    private ViewDiscountPolicies viewDiscountPolicies;
    private ViewPurchasePolicies viewPurchasePolicies;

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
    public boolean addProduct(int productId,String name, List<String> categories, double price, String description, int quantity)
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
    public Result removeProduct(int productId)
    {
        if(this.removeProduct!= null)
            return this.removeProduct.action(productId);
        return new Result(false,"User has not permissions");
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
    public Result editProduct( Product product, int price, int amount)
    {
        if(this.editProduct!= null)
            return this.editProduct.action(product,price,amount);
        return new Result(false,"user has no permissions");
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
    public Result appointManager(User owner,User user) {
        if (this.appointManager != null)
            return this.appointManager.action(owner, user, this.store);
        return new Result(false, "User has no permissions");
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

    public Result removeManagerAppointment(User ownerId,User managerId)
    {
        if(this.removeManagerAppointment!= null)
            return this.removeManagerAppointment.action(ownerId, managerId);
        return new Result(false,"User has no permissions");
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
    public Result appointOwner(User owner, User user)
    {
        if(this.appointOwner!= null)
            return this.appointOwner.action(owner,user,this.store);
        return new Result(false,"User has not permissions");
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
    public Result removeOwnerAppointment(User ownerId,User ownerToRemoveId)
    {
        if(this.removeOwnerAppointment!= null)
            return this.removeOwnerAppointment.action(ownerId, ownerToRemoveId);
        return new Result(false,"User has no permissions");
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
    public Result definePurchasePolicy(PurchaseCondition condition)
    {
        if(this.definePurchasePolicy!= null)
            return this.definePurchasePolicy.action(condition);
        return new Result(false,"User has no permission for this action.");
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
    public Result editPurchasePolicy() {
        if(this.editPurchasePolicy!= null)
            return this.editPurchasePolicy.action();
        return new Result(false,"User has no permission for this action.");
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
    public Result defineDiscountPolicy(String param, String condition, String category, int prodId, Date begin, Date end, DiscountCondition conditions, int percentage, Discount.MathOp op)
    {
        if(this.defineDiscountPolicy!= null)
            return this.defineDiscountPolicy.action(param, condition, category, prodId, begin, end, conditions, percentage, op);
        return new Result(false,"User has no permission for this action.");
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
    public Result defineEditDiscountPolicy(int prodId, String category)
    {
        if(this.editDiscountPolicy!= null)
            return this.editDiscountPolicy.action(prodId, category);
        return new Result(false,"User has no permission for this action.");
    }

    public void allowViewDiscountPolicies() {
        this.viewDiscountPolicies = new ViewDiscountPolicies(this.member,this.store);
    }

    public void disableViewDiscountPolicies() {
        if(this.viewDiscountPolicies == null) return;
        this.viewDiscountPolicies = null;
    }

    public Result defineViewDiscountPolicies(int prodId, String category) {
        if(this.viewDiscountPolicies!= null)
            return this.viewDiscountPolicies.action(prodId, category);
        return new Result(false,"User has no permission for this action.");
    }

    public void allowViewPurchasePolicies() {
        this.viewPurchasePolicies = new ViewPurchasePolicies(this.member, this.store);
    }

    public void disableViewPurchasePolicies() {
        if(this.viewPurchasePolicies == null) return;
        this.viewPurchasePolicies = null;
    }

    public Result defineViewPurchasePolicies() {
        if(this.viewPurchasePolicies!= null)
            return this.viewPurchasePolicies.action();
        return new Result(false,"User has no permission for this action.");
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
    public Result getWorkersInfo()
    {
        if(this.getWorkersInfo!= null)
            return this.getWorkersInfo.action();
        return new Result(false,"User has no permissions");
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
    public Result viewPurchaseHistory()
    {
        if(this.viewPurchaseHistory!= null)
            return this.viewPurchaseHistory.action();
        return new Result(false,"User has no permissions");
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


    public Member getMember() {
        return member;
    }

    public Store getStore() {
        return store;
    }

    public AddProduct getAddProduct() {
        return addProduct;
    }

    public RemoveProduct getRemoveProduct() {
        return removeProduct;
    }

    public EditProduct getEditProduct() {
        return editProduct;
    }

    public AppointManager getAppointManager() {
        return appointManager;
    }

    public RemoveManagerAppointment getRemoveManagerAppointment() {
        return removeManagerAppointment;
    }

    public AppointOwner getAppointOwner() {
        return appointOwner;
    }

    public RemoveOwnerAppointment getRemoveOwnerAppointment() {
        return removeOwnerAppointment;
    }

    public DefinePurchasePolicy getDefinePurchasePolicy() {
        return definePurchasePolicy;
    }

    public EditPurchasePolicy getEditPurchasePolicy() {
        return editPurchasePolicy;
    }

    public DefinePurchaseFormat getDefinePurchaseFormat() {
        return definePurchaseFormat;
    }

    public EditPurchaseFormat getEditPurchaseFormat() {
        return editPurchaseFormat;
    }

    public DefineDiscountPolicy getDefineDiscountPolicy() {
        return defineDiscountPolicy;
    }

    public EditDiscountPolicy getEditDiscountPolicy() {
        return editDiscountPolicy;
    }

    public DefineDiscountFormat getDefineDiscountFormat() {
        return defineDiscountFormat;
    }

    public EditDiscountFormat getEditDiscountFormat() {
        return editDiscountFormat;
    }

    public CloseStore getCloseStore() {
        return closeStore;
    }

    public ReopenStore getReopenStore() {
        return reopenStore;
    }

    public GetWorkersInfo getGetWorkersInfo() {
        return getWorkersInfo;
    }

    public ViewMessages getViewMessages() {
        return viewMessages;
    }

    public ReplayMessages getReplayMessages() {
        return replayMessages;
    }

    public ViewPurchaseHistory getViewPurchaseHistory() {
        return viewPurchaseHistory;
    }

    public OpenStore getOpenStore() {
        return openStore;
    }

    public AddPermissions getAddPermissions() {
        return addPermissions;
    }

    public RemovePermission getRemovePermission() {
        return removePermission;
    }
}
