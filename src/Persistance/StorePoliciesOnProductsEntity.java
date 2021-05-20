package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "StorePoliciesOnProducts", schema = "zw9P3SlfWt", catalog = "")
@IdClass(StorePoliciesOnProductsEntityPK.class)
public class StorePoliciesOnProductsEntity {
    private int storeId;
    private int productId;
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
    @Column(name = "productId")
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
        StorePoliciesOnProductsEntity that = (StorePoliciesOnProductsEntity) o;
        return storeId == that.storeId && productId == that.productId && immediatePurchaseId == that.immediatePurchaseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, productId, immediatePurchaseId);
    }
}
