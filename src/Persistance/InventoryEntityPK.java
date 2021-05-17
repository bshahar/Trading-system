package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class InventoryEntityPK implements Serializable {
    private int storeId;
    private int productId;

    @Column(name = "storeId")
    @Id
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Column(name = "productId")
    @Id
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryEntityPK that = (InventoryEntityPK) o;
        return storeId == that.storeId && productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, productId);
    }
}
