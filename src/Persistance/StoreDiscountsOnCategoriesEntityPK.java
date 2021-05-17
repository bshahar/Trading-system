package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class StoreDiscountsOnCategoriesEntityPK implements Serializable {
    private int storeId;
    private int category;

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
        StoreDiscountsOnCategoriesEntityPK that = (StoreDiscountsOnCategoriesEntityPK) o;
        return storeId == that.storeId && category == that.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, category);
    }
}
