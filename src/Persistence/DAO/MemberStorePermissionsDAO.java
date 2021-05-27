package Persistence.DAO;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MemberStorePermissions")
public class MemberStorePermissionsDAO {


    @DatabaseField(uniqueCombo = true)
    private int userId;
    @DatabaseField(uniqueCombo = true)
    private int storeId;
    @DatabaseField
    private boolean addProduct;
    @DatabaseField
    private boolean removeProduct;
    @DatabaseField
    private boolean editProduct;
    @DatabaseField
    private boolean appointManager;
    @DatabaseField
    private boolean removeManagerAppointment;
    @DatabaseField
    private boolean appointOwner;
    @DatabaseField
    private boolean removeOwnerAppointment;
    @DatabaseField
    private boolean definePurchasePolicy;
    @DatabaseField
    private boolean editPurchasePolicy;
    @DatabaseField
    private boolean definePurchaseFormat;
    @DatabaseField
    private boolean editPurchaseFormat;
    @DatabaseField
    private boolean defineDiscountPolicy;
    @DatabaseField
    private boolean editDiscountPolicy;
    @DatabaseField
    private boolean defineDiscountFormat;
    @DatabaseField
    private boolean editDiscountFormat;
    @DatabaseField
    private boolean closeStore;
    @DatabaseField
    private boolean reopenStore;
    @DatabaseField
    private boolean getWorkersInfo;
    @DatabaseField
    private boolean viewMessages;
    @DatabaseField
    private boolean replayMessages;
    @DatabaseField
    private boolean viewPurchaseHistory;
    @DatabaseField
    private boolean openStore;
    @DatabaseField
    private boolean addPermissions;
    @DatabaseField
    private boolean removePermission;
    @DatabaseField
    private boolean viewDiscountPolicies;
    @DatabaseField
    private boolean viewPurchasePolicies;
    @DatabaseField
    private boolean responedToOffer;


    public MemberStorePermissionsDAO() {
        // ORMLite needs a no-arg constructor
    }

    public MemberStorePermissionsDAO(int userId, int storeId) {
        this.userId = userId;
        this.storeId = storeId;
    }

    public int getUserId() {
        return userId;
    }

    public int getStoreId() {
        return storeId;
    }

    public boolean isAddProduct() {
        return addProduct;
    }

    public boolean isRemoveProduct() {
        return removeProduct;
    }

    public boolean isEditProduct() {
        return editProduct;
    }

    public boolean isAppointManager() {
        return appointManager;
    }

    public boolean isRemoveManagerAppointment() {
        return removeManagerAppointment;
    }

    public boolean isAppointOwner() {
        return appointOwner;
    }

    public boolean isRemoveOwnerAppointment() {
        return removeOwnerAppointment;
    }

    public boolean isDefinePurchasePolicy() {
        return definePurchasePolicy;
    }

    public boolean isEditPurchasePolicy() {
        return editPurchasePolicy;
    }

    public boolean isDefinePurchaseFormat() {
        return definePurchaseFormat;
    }

    public boolean isEditPurchaseFormat() {
        return editPurchaseFormat;
    }

    public boolean isDefineDiscountPolicy() {
        return defineDiscountPolicy;
    }

    public boolean isEditDiscountPolicy() {
        return editDiscountPolicy;
    }

    public boolean isDefineDiscountFormat() {
        return defineDiscountFormat;
    }

    public boolean isEditDiscountFormat() {
        return editDiscountFormat;
    }

    public boolean isCloseStore() {
        return closeStore;
    }

    public boolean isReopenStore() {
        return reopenStore;
    }

    public boolean isGetWorkersInfo() {
        return getWorkersInfo;
    }

    public boolean isViewMessages() {
        return viewMessages;
    }

    public boolean isReplayMessages() {
        return replayMessages;
    }

    public boolean isViewPurchaseHistory() {
        return viewPurchaseHistory;
    }

    public boolean isOpenStore() {
        return openStore;
    }

    public boolean isAddPermissions() {
        return addPermissions;
    }

    public boolean isRemovePermission() {
        return removePermission;
    }

    public boolean isViewDiscountPolicies() {
        return viewDiscountPolicies;
    }

    public boolean isViewPurchasePolicies() {
        return viewPurchasePolicies;
    }

    public boolean isResponedToOffer() {
        return responedToOffer;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setAddProduct(boolean addProduct) {
        this.addProduct = addProduct;
    }

    public void setRemoveProduct(boolean removeProduct) {
        this.removeProduct = removeProduct;
    }

    public void setEditProduct(boolean editProduct) {
        this.editProduct = editProduct;
    }

    public void setAppointManager(boolean appointManager) {
        this.appointManager = appointManager;
    }

    public void setRemoveManagerAppointment(boolean removeManagerAppointment) {
        this.removeManagerAppointment = removeManagerAppointment;
    }

    public void setAppointOwner(boolean appointOwner) {
        this.appointOwner = appointOwner;
    }

    public void setRemoveOwnerAppointment(boolean removeOwnerAppointment) {
        this.removeOwnerAppointment = removeOwnerAppointment;
    }

    public void setDefinePurchasePolicy(boolean definePurchasePolicy) {
        this.definePurchasePolicy = definePurchasePolicy;
    }

    public void setEditPurchasePolicy(boolean editPurchasePolicy) {
        this.editPurchasePolicy = editPurchasePolicy;
    }

    public void setDefinePurchaseFormat(boolean definePurchaseFormat) {
        this.definePurchaseFormat = definePurchaseFormat;
    }

    public void setEditPurchaseFormat(boolean editPurchaseFormat) {
        this.editPurchaseFormat = editPurchaseFormat;
    }

    public void setDefineDiscountPolicy(boolean defineDiscountPolicy) {
        this.defineDiscountPolicy = defineDiscountPolicy;
    }

    public void setEditDiscountPolicy(boolean editDiscountPolicy) {
        this.editDiscountPolicy = editDiscountPolicy;
    }

    public void setDefineDiscountFormat(boolean defineDiscountFormat) {
        this.defineDiscountFormat = defineDiscountFormat;
    }

    public void setEditDiscountFormat(boolean editDiscountFormat) {
        this.editDiscountFormat = editDiscountFormat;
    }

    public void setCloseStore(boolean closeStore) {
        this.closeStore = closeStore;
    }

    public void setReopenStore(boolean reopenStore) {
        this.reopenStore = reopenStore;
    }

    public void setGetWorkersInfo(boolean getWorkersInfo) {
        this.getWorkersInfo = getWorkersInfo;
    }

    public void setViewMessages(boolean viewMessages) {
        this.viewMessages = viewMessages;
    }

    public void setReplayMessages(boolean replayMessages) {
        this.replayMessages = replayMessages;
    }

    public void setViewPurchaseHistory(boolean viewPurchaseHistory) {
        this.viewPurchaseHistory = viewPurchaseHistory;
    }

    public void setOpenStore(boolean openStore) {
        this.openStore = openStore;
    }

    public void setAddPermissions(boolean addPermissions) {
        this.addPermissions = addPermissions;
    }

    public void setRemovePermission(boolean removePermission) {
        this.removePermission = removePermission;
    }

    public void setViewDiscountPolicies(boolean viewDiscountPolicies) {
        this.viewDiscountPolicies = viewDiscountPolicies;
    }

    public void setViewPurchasePolicies(boolean viewPurchasePolicies) {
        this.viewPurchasePolicies = viewPurchasePolicies;
    }

    public void setResponedToOffer(boolean responedToOffer) {
        this.responedToOffer = responedToOffer;
    }
}