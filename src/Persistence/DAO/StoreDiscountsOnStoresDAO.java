package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "StoreDiscountsOnStores")
public class StoreDiscountsOnStoresDAO {

    @DatabaseField(id = true)
    private int storeId;
    @DatabaseField
    private int discountId;

    public StoreDiscountsOnStoresDAO() {}

    public StoreDiscountsOnStoresDAO(int storeId, int discountId) {
        this.storeId = storeId;
        this.discountId = discountId;
    }

    public int getStoreId() {
        return storeId;
    }
    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getDiscountId() {
        return discountId;
    }
    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

}
