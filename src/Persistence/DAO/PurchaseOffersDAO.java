package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "PurchaseOffers")
public class PurchaseOffersDAO {

    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private double priceOfOffer;
    @DatabaseField
    private int userId;
    @DatabaseField
    private int quantity;

    @DatabaseField
    private int counter;

    public PurchaseOffersDAO() {
        // ORMLite needs a no-arg constructor
    }

    public PurchaseOffersDAO(int id, double priceOfOffer, int userId, int quantity, int counter) {
        this.id = id;
        this.priceOfOffer = priceOfOffer;
        this.userId = userId;
        this.quantity = quantity;
        this.counter = counter;
    }


    public int getId() {
        return id;
    }

    public double getPriceOfOffer() {
        return priceOfOffer;
    }

    public int getUserId() {
        return userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPriceOfOffer(double priceOfOffer) {
        this.priceOfOffer = priceOfOffer;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }


}