package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ProductCategories", schema = "zw9P3SlfWt", catalog = "")
public class ProductCategoriesEntity {
    private int productId;
    private int category;

    @Id
    @Column(name = "productId")
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "category")
    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategoriesEntity that = (ProductCategoriesEntity) o;
        return productId == that.productId && category == that.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, category);
    }
}
