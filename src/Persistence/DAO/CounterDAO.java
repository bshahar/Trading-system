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
    private int conditionCounter;
    @DatabaseField
    private int offerCounter;
    @DatabaseField
    private int policyCounter;
    @DatabaseField
    private int notificationCounter;
    @DatabaseField
    private int messageCounter;

    public CounterDAO() {
        // ORMLite needs a no-arg constructor
    }

    public CounterDAO(int id, int storeCounter, int userCounter, int productCounter, int receiptCounter, int observableCounter, int conditionCounter, int offerCounter, int policyCounter, int notificationCounter, int messageCounter) {
        this.id = id;
        this.storeCounter = storeCounter;
        this.userCounter = userCounter;
        this.productCounter = productCounter;
        this.receiptCounter = receiptCounter;
        this.observableCounter = observableCounter;
        this.conditionCounter = conditionCounter;
        this.offerCounter = offerCounter;
        this.policyCounter = policyCounter;
        this.notificationCounter = notificationCounter;
        this.messageCounter = messageCounter;
    }

    public int getId() {
        return id;
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

    public int getConditionCounter() {
        return conditionCounter;
    }

    public void setConditionCounter(int conditionCounter) {
        this.conditionCounter = conditionCounter;
    }

    public int getOfferCounter() {
        return offerCounter;
    }

    public void setOfferCounter(int offerCounter) {
        this.offerCounter = offerCounter;
    }

    public int getPolicyCounter() {
        return policyCounter;
    }

    public void setPolicyCounter(int policyCounter) {
        this.policyCounter = policyCounter;
    }

    public int getNotificationCounter() {
        return notificationCounter;
    }

    public void setNotificationCounter(int notificationCounter) {
        this.notificationCounter = notificationCounter;
    }

    public int getMessageCounter() {
        return messageCounter;
    }

    public void setMessageCounter(int messageCounter) {
        this.messageCounter = messageCounter;
    }
}
