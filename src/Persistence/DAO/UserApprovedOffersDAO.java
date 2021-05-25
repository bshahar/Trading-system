package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "UserApprovedOffers")
public class UserApprovedOffersDAO {

    @DatabaseField(uniqueCombo = true)
    private int userId;
    @DatabaseField(uniqueCombo = true)
    private int storeId;
    @DatabaseField(uniqueCombo = true)
    private int productId;
    @DatabaseField
    private double offerPrice;

    public UserApprovedOffersDAO() {
        // ORMLite needs a no-arg constructor
    }

    public UserApprovedOffersDAO(int userId, int storeId, int productId, double offerPrice) {
        this.userId = userId;
        this.storeId = storeId;
        this.productId = productId;
        this.offerPrice = offerPrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(double offerPrice) {
        this.offerPrice = offerPrice;
    }


}


