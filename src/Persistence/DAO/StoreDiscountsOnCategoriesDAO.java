package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "StoreDiscountsOnCategories")
public class StoreDiscountsOnCategoriesDAO {

    @DatabaseField(uniqueCombo = true)
    private int storeId;
    @DatabaseField(uniqueCombo = true)
    private String category;
    @DatabaseField
    private int discountId;

    public StoreDiscountsOnCategoriesDAO() {}

    public StoreDiscountsOnCategoriesDAO(int storeId, String category, int discountId) {
        this.storeId = storeId;
        this.category = category;
        this.discountId = discountId;
    }

    public int getStoreId() {
        return storeId;
    }
    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public int getDiscountId() {
        return discountId;
    }
    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }
}
