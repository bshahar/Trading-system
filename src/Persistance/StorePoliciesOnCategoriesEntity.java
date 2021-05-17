package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "StorePoliciesOnCategories", schema = "zw9P3SlfWt", catalog = "")
@IdClass(StorePoliciesOnCategoriesEntityPK.class)
public class StorePoliciesOnCategoriesEntity {
    private int storeId;
    private String category;
    private int immediatePurchaseId;

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
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Basic
    @Column(name = "immediatePurchaseId")
    public int getImmediatePurchaseId() {
        return immediatePurchaseId;
    }

    public void setImmediatePurchaseId(int immediatePurchaseId) {
        this.immediatePurchaseId = immediatePurchaseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorePoliciesOnCategoriesEntity that = (StorePoliciesOnCategoriesEntity) o;
        return storeId == that.storeId && immediatePurchaseId == that.immediatePurchaseId && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, category, immediatePurchaseId);
    }
}
