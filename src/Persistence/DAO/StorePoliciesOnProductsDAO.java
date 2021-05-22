package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "StorePoliciesOnProducts")
public class StorePoliciesOnProductsDAO {

    @DatabaseField(id = true)
    private int storeId;
    @DatabaseField(id = true)
    private int productId;
    @DatabaseField
    private int immediatePurchaseId;

    public StorePoliciesOnProductsDAO() {
        // ORMLite needs a no-arg constructor
    }
    public StorePoliciesOnProductsDAO(int storeId, int productId, int immediatePurchaseId) {
        this.storeId = storeId;
        this.productId = productId;
        this.immediatePurchaseId = immediatePurchaseId;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getProductId() {
        return productId;
    }

    public int getImmediatePurchaseId() {
        return immediatePurchaseId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setImmediatePurchaseId(int immediatePurchaseId) {
        this.immediatePurchaseId = immediatePurchaseId;
    }

}


