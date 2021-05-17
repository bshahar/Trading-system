package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "StoreDiscountsOnCategories", schema = "zw9P3SlfWt", catalog = "")
@IdClass(StoreDiscountsOnCategoriesEntityPK.class)
public class StoreDiscountsOnCategoriesEntity {
    private int storeId;
    private int category;
    private Integer discountId;

    @Id
    @Column(name = "storeId")
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Id
    @Column(name = "category")
    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Basic
    @Column(name = "discountId")
    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreDiscountsOnCategoriesEntity that = (StoreDiscountsOnCategoriesEntity) o;
        return storeId == that.storeId && category == that.category && Objects.equals(discountId, that.discountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, category, discountId);
    }
}
