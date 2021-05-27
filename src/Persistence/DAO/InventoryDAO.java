package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Inventory")
public class InventoryDAO {


    @DatabaseField(uniqueCombo = true)
    private int storeId;
    @DatabaseField(uniqueCombo = true)
    private int productId;
    @DatabaseField
    private int amount;

    public InventoryDAO() {
    }

    public InventoryDAO(int storeId, int productId, int amount) {
        this.storeId = storeId;
        this.productId = productId;
        this.amount = amount;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
