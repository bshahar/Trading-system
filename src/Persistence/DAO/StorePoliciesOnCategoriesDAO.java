package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "StorePoliciesOnCategories")
public class StorePoliciesOnCategoriesDAO {

    @DatabaseField(id = true)
    private int storeId;
    @DatabaseField(id = true)
    private String category;
    @DatabaseField
    private int immediatePurchaseId;

    public StorePoliciesOnCategoriesDAO() {
        // ORMLite needs a no-arg constructor
    }
    public StorePoliciesOnCategoriesDAO(int storeId, String category, int immediatePurchaseId) {
        this.storeId = storeId;
        this.category = category;
        this.immediatePurchaseId = immediatePurchaseId;
    }

    public int getStoreId() {
        return storeId;
    }

    public String getCategory() {
        return category;
    }

    public int getImmediatePurchaseId() {
        return immediatePurchaseId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImmediatePurchaseId(int immediatePurchaseId) {
        this.immediatePurchaseId = immediatePurchaseId;
    }

}


