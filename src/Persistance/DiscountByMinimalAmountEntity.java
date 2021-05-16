package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "DiscountByMinimalAmount", schema = "zw9P3SlfWt", catalog = "")
@IdClass(DiscountByMinimalAmountEntityPK.class)
public class DiscountByMinimalAmountEntity {
    private int conditionId;
    private int productId;
    private int minAmount;

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
    @Column(name = "minAmount")
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
        DiscountByMinimalAmountEntity that = (DiscountByMinimalAmountEntity) o;
        return conditionId == that.conditionId && productId == that.productId && minAmount == that.minAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionId, productId, minAmount);
    }
}
