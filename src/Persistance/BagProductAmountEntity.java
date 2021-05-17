package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "BagProductAmount", schema = "zw9P3SlfWt", catalog = "")
@IdClass(BagProductAmountEntityPK.class)
public class BagProductAmountEntity {
    private int userId;
    private int storeId;
    private int productId;
    private Integer amount;

    @Id
    @Column(name = "userId")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BagProductAmountEntity that = (BagProductAmountEntity) o;
        return userId == that.userId && storeId == that.storeId && productId == that.productId && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, storeId, productId, amount);
    }
}
