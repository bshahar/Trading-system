package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "MaxAmountPolicy", schema = "zw9P3SlfWt", catalog = "")
@IdClass(MaxAmountPolicyEntityPK.class)
public class MaxAmountPolicyEntity {
    private int conditionId;
    private int productId;
    private int maxAmount;

    @Id
    @Column(name = "conditionId")
    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    @Id
    @Column(name = "productId")
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Id
    @Column(name = "maxAmount")
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
        MaxAmountPolicyEntity that = (MaxAmountPolicyEntity) o;
        return conditionId == that.conditionId && productId == that.productId && maxAmount == that.maxAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionId, productId, maxAmount);
    }
}
