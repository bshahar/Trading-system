package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "StoreDiscountsOnStores", schema = "zw9P3SlfWt", catalog = "")
public class StoreDiscountsOnStoresEntity {
    private int storeId;
    private Integer discountId;

    @Id
    @Column(name = "storeId")
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
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
        StoreDiscountsOnStoresEntity that = (StoreDiscountsOnStoresEntity) o;
        return storeId == that.storeId && Objects.equals(discountId, that.discountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, discountId);
    }
}
