package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Inventory", schema = "zw9P3SlfWt", catalog = "")
@IdClass(InventoryEntityPK.class)
public class InventoryEntity {
    private int storeId;
    private int productId;
    private int amount;

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
    @Column(name = "amount")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryEntity that = (InventoryEntity) o;
        return storeId == that.storeId && productId == that.productId && amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, productId, amount);
    }
}
