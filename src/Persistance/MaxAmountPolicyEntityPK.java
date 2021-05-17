package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class MaxAmountPolicyEntityPK implements Serializable {
    private int conditionId;
    private int productId;
    private int maxAmount;

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

    @Column(name = "maxAmount")
    @Id
    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaxAmountPolicyEntityPK that = (MaxAmountPolicyEntityPK) o;
        return conditionId == that.conditionId && productId == that.productId && maxAmount == that.maxAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionId, productId, maxAmount);
    }
}
