package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class DiscountByMinimalAmountEntityPK implements Serializable {
    private int conditionId;
    private int productId;
    private int minAmount;

    @Column(name = "conditionId")
    @Id
    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    @Column(name = "productId")
    @Id
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Column(name = "minAmount")
    @Id
    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountByMinimalAmountEntityPK that = (DiscountByMinimalAmountEntityPK) o;
        return conditionId == that.conditionId && productId == that.productId && minAmount == that.minAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionId, productId, minAmount);
    }
}
