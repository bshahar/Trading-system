package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable(tableName = "Counters")
public class CounterDAO {
    @DatabaseField(id = true)
    private int id;
         @DatabaseField
        private int storeCounter;
        @DatabaseField
        private int userCounter;
        @DatabaseField
        private int productCounter;
        @DatabaseField
        private int receiptCounter;
        @DatabaseField
        private int observableCounter;
        @DatabaseField
        private int immediatePurchaseCounter;
        @DatabaseField
        private int discountConditionCounter;
        @DatabaseField
        private int notificationCounter;
    @DatabaseField
    private int messageCounter;

        public CounterDAO() {
            // ORMLite needs a no-arg constructor
        }

    public CounterDAO(int messageCounter ,int id ,int storeCounter, int userCounter, int productCounter, int receiptCounter, int observableCounter, int immediatePurchaseCounter, int discountConditionCounter, int notificationCounter) {
            this.messageCounter = messageCounter;
            this.id = id;
            this.storeCounter = storeCounter;
        this.userCounter = userCounter;
        this.productCounter = productCounter;
        this.receiptCounter = receiptCounter;
        this.observableCounter = observableCounter;
        this.immediatePurchaseCounter = immediatePurchaseCounter;
        this.discountConditionCounter = discountConditionCounter;
        this.notificationCounter = notificationCounter;
    }

    public int getId() {
        return id;
    }

    public int getMessageCounter() {
        return messageCounter;
    }

    public void setMessageCounter(int messageCounter) {
        this.messageCounter = messageCounter;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoreCounter() {
        return storeCounter;
    }

    public void setStoreCounter(int storeCounter) {
        this.storeCounter = storeCounter;
    }

    public int getUserCounter() {
        return userCounter;
    }

    public void setUserCounter(int userCounter) {
        this.userCounter = userCounter;
    }

    public int getProductCounter() {
        return productCounter;
    }

    public void setProductCounter(int productCounter) {
        this.productCounter = productCounter;
    }

    public int getReceiptCounter() {
        return receiptCounter;
    }

    public void setReceiptCounter(int receiptCounter) {
        this.receiptCounter = receiptCounter;
    }

    public int getObservableCounter() {
        return observableCounter;
    }

    public void setObservableCounter(int observableCounter) {
        this.observableCounter = observableCounter;
    }

    public int getImmediatePurchaseCounter() {
        return immediatePurchaseCounter;
    }

    public void setImmediatePurchaseCounter(int immediatePurchaseCounter) {
        this.immediatePurchaseCounter = immediatePurchaseCounter;
    }

    public int getDiscountConditionCounter() {
        return discountConditionCounter;
    }

    public void setDiscountConditionCounter(int discountConditionCounter) {
        this.discountConditionCounter = discountConditionCounter;
    }

    public int getNotificationCounter() {
        return notificationCounter;
    }

    public void setNotificationCounter(int notificationCounter) {
        this.notificationCounter = notificationCounter;
    }
}
