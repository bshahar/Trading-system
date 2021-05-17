package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ConditionalDiscount", schema = "zw9P3SlfWt", catalog = "")
@IdClass(ConditionalDiscountEntityPK.class)
public class ConditionalDiscountEntity {
    private int discountId;
    private int conditionId;

    @Id
    @Column(name = "discountId")
    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    @Id
    @Column(name = "conditionId")
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
        ConditionalDiscountEntity that = (ConditionalDiscountEntity) o;
        return discountId == that.discountId && conditionId == that.conditionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountId, conditionId);
    }
}
