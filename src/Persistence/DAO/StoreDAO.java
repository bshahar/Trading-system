package Persistence.DAO;

import Domain.*;
import Domain.DiscountFormat.Discount;
import Domain.PurchaseFormat.ImmediatePurchase;
import Domain.PurchaseFormat.PurchaseOffer;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@DatabaseTable(tableName = "Users")
public class StoreDAO {

    @DatabaseField(id = true)
    private int storeId;
    @DatabaseField
    private int notificationId;
    @DatabaseField
    private String name;
    @DatabaseField
    private double rate;
    @DatabaseField
    private int ratesCount;

    public StoreDAO() {
    }

    public StoreDAO(int storeId, int notificationId, String name, double rate, int ratesCount) {
        this.storeId = storeId;
        this.notificationId = notificationId;
        this.name = name;
        this.rate = rate;
        this.ratesCount = ratesCount;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getRatesCount() {
        return ratesCount;
    }

    public void setRatesCount(int ratesCount) {
        this.ratesCount = ratesCount;
    }
}
