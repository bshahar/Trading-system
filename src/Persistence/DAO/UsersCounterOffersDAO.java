package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "UsersCounterOffers")
public class UsersCounterOffersDAO {

    @DatabaseField(id = true)
    private int storeId;
    @DatabaseField(id = true)
    private int userId;
    @DatabaseField(id = true)
    private int productId;
    @DatabaseField
    private int offerId;

    public UsersCounterOffersDAO() {
        // ORMLite needs a no-arg constructor
    }

    public UsersCounterOffersDAO(int storeId, int userId, int productId, int offerId) {
        this.storeId = storeId;
        this.userId = userId;
        this.productId = productId;
        this.offerId = offerId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

}