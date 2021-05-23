package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ProductReviews")
public class ProductReviewsDAO {

    @DatabaseField(uniqueCombo = true)
    private int productId;
    @DatabaseField(uniqueCombo = true)
    private String review;

    public ProductReviewsDAO(int productId, String review) {
        this.productId = productId;
        this.review = review;
    }

    public ProductReviewsDAO() {
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
