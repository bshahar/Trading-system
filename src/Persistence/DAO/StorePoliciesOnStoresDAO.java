package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "StorePoliciesOnStores")
public class StorePoliciesOnStoresDAO {

    @DatabaseField(id = true)
    private int storeId;
    @DatabaseField
    private int immediatePurchaseId;

    public StorePoliciesOnStoresDAO() {
        // ORMLite needs a no-arg constructor
    }
    public StorePoliciesOnStoresDAO(int storeId, int immediatePurchaseId) {
        this.storeId = storeId;
        this.immediatePurchaseId = immediatePurchaseId;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getImmediatePurchaseId() {
        return immediatePurchaseId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setImmediatePurchaseId(int immediatePurchaseId) {
        this.immediatePurchaseId = immediatePurchaseId;
    }

}


