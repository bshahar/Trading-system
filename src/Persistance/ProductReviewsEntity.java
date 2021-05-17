package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ProductReviews", schema = "zw9P3SlfWt", catalog = "")
public class ProductReviewsEntity {
    private int productId;
    private String review;
    private int id;

    @Basic
    @Column(name = "productId")
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "review")
    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductReviewsEntity that = (ProductReviewsEntity) o;
        return productId == that.productId && id == that.id && Objects.equals(review, that.review);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, review, id);
    }
}
