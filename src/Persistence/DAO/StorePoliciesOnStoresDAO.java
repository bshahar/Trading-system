package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "StorePoliciesOnStores")
public class StorePoliciesOnStoresDAO {

    @DatabaseField(id = true)
    private int storeId;
    @DatabaseField
    private int immediateId;

    public StorePoliciesOnStoresDAO() {
        // ORMLite needs a no-arg constructor
    }
    public StorePoliciesOnStoresDAO(int storeId, int immediateId) {
        this.storeId = storeId;
        this.immediateId = immediateId;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getImmediatePurchaseId() {
        return immediateId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setImmediatePurchaseId(int immediateId) {
        this.immediateId = immediateId;
    }

}


