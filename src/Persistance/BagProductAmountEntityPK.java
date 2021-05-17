package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class BagProductAmountEntityPK implements Serializable {
    private int userId;
    private int storeId;
    private int productId;

    @Column(name = "userId")
    @Id
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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
        BagProductAmountEntityPK that = (BagProductAmountEntityPK) o;
        return userId == that.userId && storeId == that.storeId && productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, storeId, productId);
    }
}
