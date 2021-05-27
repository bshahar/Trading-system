package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "StoreDiscountsOnProducts")
public class StoreDiscountsOnProductsDAO {

    @DatabaseField(uniqueCombo = true)
    private int storeId;
    @DatabaseField(uniqueCombo = true)
    private int productId;
    @DatabaseField
    private int discountId;

    public StoreDiscountsOnProductsDAO() {}

    public StoreDiscountsOnProductsDAO(int storeId, int productId, int discountId) {
        this.storeId = storeId;
        this.productId = productId;
        this.discountId = discountId;
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

    public int getDiscountId() {
        return discountId;
    }
    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }
}
