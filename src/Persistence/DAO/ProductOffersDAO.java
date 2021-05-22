package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ProductOffers")
public class ProductOffersDAO {

    @DatabaseField(id = true)
    private int storeId;
    @DatabaseField(id = true)
    private int offerId;
    @DatabaseField(id = true)
    private int productId;

    public ProductOffersDAO() {
        // ORMLite needs a no-arg constructor
    }
    public ProductOffersDAO(int storeId, int offerId, int productId) {
        this.storeId = storeId;
        this.offerId = offerId;
        this.productId = productId;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getOfferId() {
        return offerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }



}
