package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "MemberStorePermissions", schema = "zw9P3SlfWt", catalog = "")
@IdClass(MemberStorePermissionsEntityPK.class)
public class MemberStorePermissionsEntity {
    private int userId;
    private int storeId;
    private Byte addProduct;
    private Byte removeProduct;
    private Byte editProduct;
    private Byte appointManager;
    private Byte removeManagerAppointment;
    private Byte appointOwner;
    private Byte removeOwnerAppointment;
    private Byte definePurchasePolicy;
    private Byte editPurchasePolicy;
    private Byte definePurchaseFormat;
    private Byte editPurchaseFormat;
    private Byte defineDiscountPolicy;
    private Byte editDiscountPolicy;
    private Byte defineDiscountFormat;
    private Byte editDiscountFormat;
    private Byte closeStore;
    private Byte reopenStore;
    private Byte getWorkersInfo;
    private Byte viewMessages;
    private Byte replayMessages;
    private Byte viewPurchaseHistory;
    private Byte openStore;
    private Byte addPermissions;
    private Byte removePermission;
    private Byte viewDiscountPolicies;
    private Byte viewPurchasePolicies;

    @Id
    @Column(name = "userId")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "storeId")
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Basic
    @Column(name = "addProduct")
    public Byte getAddProduct() {
        return addProduct;
    }

    public void setAddProduct(Byte addProduct) {
        this.addProduct = addProduct;
    }

    @Basic
    @Column(name = "removeProduct")
    public Byte getRemoveProduct() {
        return removeProduct;
    }

    public void setRemoveProduct(Byte removeProduct) {
        this.removeProduct = removeProduct;
    }

    @Basic
    @Column(name = "editProduct")
    public Byte getEditProduct() {
        return editProduct;
    }

    public void setEditProduct(Byte editProduct) {
        this.editProduct = editProduct;
    }

    @Basic
    @Column(name = "appointManager")
    public Byte getAppointManager() {
        return appointManager;
    }

    public void setAppointManager(Byte appointManager) {
        this.appointManager = appointManager;
    }

    @Basic
    @Column(name = "removeManagerAppointment")
    public Byte getRemoveManagerAppointment() {
        return removeManagerAppointment;
    }

    public void setRemoveManagerAppointment(Byte removeManagerAppointment) {
        this.removeManagerAppointment = removeManagerAppointment;
    }

    @Basic
    @Column(name = "appointOwner")
    public Byte getAppointOwner() {
        return appointOwner;
    }

    public void setAppointOwner(Byte appointOwner) {
        this.appointOwner = appointOwner;
    }

    @Basic
    @Column(name = "removeOwnerAppointment")
    public Byte getRemoveOwnerAppointment() {
        return removeOwnerAppointment;
    }

    public void setRemoveOwnerAppointment(Byte removeOwnerAppointment) {
        this.removeOwnerAppointment = removeOwnerAppointment;
    }

    @Basic
    @Column(name = "definePurchasePolicy")
    public Byte getDefinePurchasePolicy() {
        return definePurchasePolicy;
    }

    public void setDefinePurchasePolicy(Byte definePurchasePolicy) {
        this.definePurchasePolicy = definePurchasePolicy;
    }

    @Basic
    @Column(name = "editPurchasePolicy")
    public Byte getEditPurchasePolicy() {
        return editPurchasePolicy;
    }

    public void setEditPurchasePolicy(Byte editPurchasePolicy) {
        this.editPurchasePolicy = editPurchasePolicy;
    }

    @Basic
    @Column(name = "definePurchaseFormat")
    public Byte getDefinePurchaseFormat() {
        return definePurchaseFormat;
    }

    public void setDefinePurchaseFormat(Byte definePurchaseFormat) {
        this.definePurchaseFormat = definePurchaseFormat;
    }

    @Basic
    @Column(name = "editPurchaseFormat")
    public Byte getEditPurchaseFormat() {
        return editPurchaseFormat;
    }

    public void setEditPurchaseFormat(Byte editPurchaseFormat) {
        this.editPurchaseFormat = editPurchaseFormat;
    }

    @Basic
    @Column(name = "defineDiscountPolicy")
    public Byte getDefineDiscountPolicy() {
        return defineDiscountPolicy;
    }

    public void setDefineDiscountPolicy(Byte defineDiscountPolicy) {
        this.defineDiscountPolicy = defineDiscountPolicy;
    }

    @Basic
    @Column(name = "editDiscountPolicy")
    public Byte getEditDiscountPolicy() {
        return editDiscountPolicy;
    }

    public void setEditDiscountPolicy(Byte editDiscountPolicy) {
        this.editDiscountPolicy = editDiscountPolicy;
    }

    @Basic
    @Column(name = "defineDiscountFormat")
    public Byte getDefineDiscountFormat() {
        return defineDiscountFormat;
    }

    public void setDefineDiscountFormat(Byte defineDiscountFormat) {
        this.defineDiscountFormat = defineDiscountFormat;
    }

    @Basic
    @Column(name = "editDiscountFormat")
    public Byte getEditDiscountFormat() {
        return editDiscountFormat;
    }

    public void setEditDiscountFormat(Byte editDiscountFormat) {
        this.editDiscountFormat = editDiscountFormat;
    }

    @Basic
    @Column(name = "closeStore")
    public Byte getCloseStore() {
        return closeStore;
    }

    public void setCloseStore(Byte closeStore) {
        this.closeStore = closeStore;
    }

    @Basic
    @Column(name = "reopenStore")
    public Byte getReopenStore() {
        return reopenStore;
    }

    public void setReopenStore(Byte reopenStore) {
        this.reopenStore = reopenStore;
    }

    @Basic
    @Column(name = "getWorkersInfo")
    public Byte getGetWorkersInfo() {
        return getWorkersInfo;
    }

    public void setGetWorkersInfo(Byte getWorkersInfo) {
        this.getWorkersInfo = getWorkersInfo;
    }

    @Basic
    @Column(name = "viewMessages")
    public Byte getViewMessages() {
        return viewMessages;
    }

    public void setViewMessages(Byte viewMessages) {
        this.viewMessages = viewMessages;
    }

    @Basic
    @Column(name = "replayMessages")
    public Byte getReplayMessages() {
        return replayMessages;
    }

    public void setReplayMessages(Byte replayMessages) {
        this.replayMessages = replayMessages;
    }

    @Basic
    @Column(name = "viewPurchaseHistory")
    public Byte getViewPurchaseHistory() {
        return viewPurchaseHistory;
    }

    public void setViewPurchaseHistory(Byte viewPurchaseHistory) {
        this.viewPurchaseHistory = viewPurchaseHistory;
    }

    @Basic
    @Column(name = "openStore")
    public Byte getOpenStore() {
        return openStore;
    }

    public void setOpenStore(Byte openStore) {
        this.openStore = openStore;
    }

    @Basic
    @Column(name = "addPermissions")
    public Byte getAddPermissions() {
        return addPermissions;
    }

    public void setAddPermissions(Byte addPermissions) {
        this.addPermissions = addPermissions;
    }

    @Basic
    @Column(name = "removePermission")
    public Byte getRemovePermission() {
        return removePermission;
    }

    public void setRemovePermission(Byte removePermission) {
        this.removePermission = removePermission;
    }

    @Basic
    @Column(name = "viewDiscountPolicies")
    public Byte getViewDiscountPolicies() {
        return viewDiscountPolicies;
    }

    public void setViewDiscountPolicies(Byte viewDiscountPolicies) {
        this.viewDiscountPolicies = viewDiscountPolicies;
    }

    @Basic
    @Column(name = "viewPurchasePolicies")
    public Byte getViewPurchasePolicies() {
        return viewPurchasePolicies;
    }

    public void setViewPurchasePolicies(Byte viewPurchasePolicies) {
        this.viewPurchasePolicies = viewPurchasePolicies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberStorePermissionsEntity that = (MemberStorePermissionsEntity) o;
        return userId == that.userId && storeId == that.storeId && Objects.equals(addProduct, that.addProduct) && Objects.equals(removeProduct, that.removeProduct) && Objects.equals(editProduct, that.editProduct) && Objects.equals(appointManager, that.appointManager) && Objects.equals(removeManagerAppointment, that.removeManagerAppointment) && Objects.equals(appointOwner, that.appointOwner) && Objects.equals(removeOwnerAppointment, that.removeOwnerAppointment) && Objects.equals(definePurchasePolicy, that.definePurchasePolicy) && Objects.equals(editPurchasePolicy, that.editPurchasePolicy) && Objects.equals(definePurchaseFormat, that.definePurchaseFormat) && Objects.equals(editPurchaseFormat, that.editPurchaseFormat) && Objects.equals(defineDiscountPolicy, that.defineDiscountPolicy) && Objects.equals(editDiscountPolicy, that.editDiscountPolicy) && Objects.equals(defineDiscountFormat, that.defineDiscountFormat) && Objects.equals(editDiscountFormat, that.editDiscountFormat) && Objects.equals(closeStore, that.closeStore) && Objects.equals(reopenStore, that.reopenStore) && Objects.equals(getWorkersInfo, that.getWorkersInfo) && Objects.equals(viewMessages, that.viewMessages) && Objects.equals(replayMessages, that.replayMessages) && Objects.equals(viewPurchaseHistory, that.viewPurchaseHistory) && Objects.equals(openStore, that.openStore) && Objects.equals(addPermissions, that.addPermissions) && Objects.equals(removePermission, that.removePermission) && Objects.equals(viewDiscountPolicies, that.viewDiscountPolicies) && Objects.equals(viewPurchasePolicies, that.viewPurchasePolicies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, storeId, addProduct, removeProduct, editProduct, appointManager, removeManagerAppointment, appointOwner, removeOwnerAppointment, definePurchasePolicy, editPurchasePolicy, definePurchaseFormat, editPurchaseFormat, defineDiscountPolicy, editDiscountPolicy, defineDiscountFormat, editDiscountFormat, closeStore, reopenStore, getWorkersInfo, viewMessages, replayMessages, viewPurchaseHistory, openStore, addPermissions, removePermission, viewDiscountPolicies, viewPurchasePolicies);
    }
}
