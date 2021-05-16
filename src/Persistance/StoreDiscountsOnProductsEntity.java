package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "StoreDiscountsOnProducts", schema = "zw9P3SlfWt", catalog = "")
@IdClass(StoreDiscountsOnProductsEntityPK.class)
public class StoreDiscountsOnProductsEntity {
    private int storeId;
    private int productId;
    private int discountId;

    @Id
    @Column(name = "storeId")
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Id
    @Column(name = "productId")
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "discountId")
    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreDiscountsOnProductsEntity that = (StoreDiscountsOnProductsEntity) o;
        return storeId == that.storeId && productId == that.productId && discountId == that.discountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, productId, discountId);
    }
}
