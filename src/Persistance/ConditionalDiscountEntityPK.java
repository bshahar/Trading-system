package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ConditionalDiscountEntityPK implements Serializable {
    private int discountId;
    private int conditionId;

    @Column(name = "discountId")
    @Id
    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    @Column(name = "conditionId")
    @Id
    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConditionalDiscountEntityPK that = (ConditionalDiscountEntityPK) o;
        return discountId == that.discountId && conditionId == that.conditionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountId, conditionId);
    }
}
