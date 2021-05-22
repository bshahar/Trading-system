package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ProductCategories")
public class ProductCategoriesDAO {

    @DatabaseField(id = true)
    private int productId;
    @DatabaseField(id = true)
    private String category;

    public ProductCategoriesDAO(int productId, String category) {
        this.productId = productId;
        this.category = category;
    }

    public ProductCategoriesDAO() {
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
