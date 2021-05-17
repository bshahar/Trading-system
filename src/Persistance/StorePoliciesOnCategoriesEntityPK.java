package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class StorePoliciesOnCategoriesEntityPK implements Serializable {
    private int storeId;
    private String category;

    @Column(name = "storeId")
    @Id
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Column(name = "category")
    @Id
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorePoliciesOnCategoriesEntityPK that = (StorePoliciesOnCategoriesEntityPK) o;
        return storeId == that.storeId && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, category);
    }
}
