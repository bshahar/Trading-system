package Persistence.DAO;

import Domain.ReceiptLine;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.OneToMany;
import java.util.List;

@DatabaseTable(tableName = "Receipts")
public class ReceiptDAO {

    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private int storeId;
    @DatabaseField
    private int userId;
    @DatabaseField
    private String userName;
    @DatabaseField
    private double totalCost;
    @DatabaseField
    private int paymentTransactionId;
    @DatabaseField
    private int supplementTransactionId;



    public ReceiptDAO() {
        // ORMLite needs a no-arg constructor
    }

    public ReceiptDAO(int id, int storeId, int userId, String userName, double totalCost, int paymentTransactionId, int supplementTransactionId) {
        this.id = id;
        this.storeId = storeId;
        this.userId = userId;
        this.userName = userName;
        this.totalCost = totalCost;
        this.paymentTransactionId = paymentTransactionId;
        this.supplementTransactionId = supplementTransactionId;
    }

    public int getId() {
        return id;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public int getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public int getSupplementTransactionId() {
        return supplementTransactionId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setPaymentTransactionId(int paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public void setSupplementTransactionId(int supplementTransactionId) {
        this.supplementTransactionId = supplementTransactionId;
    }


}
