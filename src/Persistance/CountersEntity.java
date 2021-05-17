package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Counters", schema = "zw9P3SlfWt", catalog = "")
public class CountersEntity {
    private int storeCounter;
    private Integer userCounter;
    private Integer productCounter;
    private Integer receiptCounter;
    private Integer observableCounter;
    private Integer immediatePurchaseCounter;
    private Integer discountConditionCounter;
    private Integer notificationCounter;

    @Id
    @Column(name = "storeCounter")
    public int getStoreCounter() {
        return storeCounter;
    }

    public void setStoreCounter(int storeCounter) {
        this.storeCounter = storeCounter;
    }

    @Basic
    @Column(name = "userCounter")
    public Integer getUserCounter() {
        return userCounter;
    }

    public void setUserCounter(Integer userCounter) {
        this.userCounter = userCounter;
    }

    @Basic
    @Column(name = "productCounter")
    public Integer getProductCounter() {
        return productCounter;
    }

    public void setProductCounter(Integer productCounter) {
        this.productCounter = productCounter;
    }

    @Basic
    @Column(name = "receiptCounter")
    public Integer getReceiptCounter() {
        return receiptCounter;
    }

    public void setReceiptCounter(Integer receiptCounter) {
        this.receiptCounter = receiptCounter;
    }

    @Basic
    @Column(name = "observableCounter")
    public Integer getObservableCounter() {
        return observableCounter;
    }

    public void setObservableCounter(Integer observableCounter) {
        this.observableCounter = observableCounter;
    }

    @Basic
    @Column(name = "immediatePurchaseCounter")
    public Integer getImmediatePurchaseCounter() {
        return immediatePurchaseCounter;
    }

    public void setImmediatePurchaseCounter(Integer immediatePurchaseCounter) {
        this.immediatePurchaseCounter = immediatePurchaseCounter;
    }

    @Basic
    @Column(name = "discountConditionCounter")
    public Integer getDiscountConditionCounter() {
        return discountConditionCounter;
    }

    public void setDiscountConditionCounter(Integer discountConditionCounter) {
        this.discountConditionCounter = discountConditionCounter;
    }

    @Basic
    @Column(name = "notificationCounter")
    public Integer getNotificationCounter() {
        return notificationCounter;
    }

    public void setNotificationCounter(Integer notificationCounter) {
        this.notificationCounter = notificationCounter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountersEntity that = (CountersEntity) o;
        return storeCounter == that.storeCounter && Objects.equals(userCounter, that.userCounter) && Objects.equals(productCounter, that.productCounter) && Objects.equals(receiptCounter, that.receiptCounter) && Objects.equals(observableCounter, that.observableCounter) && Objects.equals(immediatePurchaseCounter, that.immediatePurchaseCounter) && Objects.equals(discountConditionCounter, that.discountConditionCounter) && Objects.equals(notificationCounter, that.notificationCounter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeCounter, userCounter, productCounter, receiptCounter, observableCounter, immediatePurchaseCounter, discountConditionCounter, notificationCounter);
    }
}
