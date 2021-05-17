package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "DiscountByMinimalCost", schema = "zw9P3SlfWt", catalog = "")
@IdClass(DiscountByMinimalCostEntityPK.class)
public class DiscountByMinimalCostEntity {
    private int conditionId;
    private double minCost;

    @Id
    @Column(name = "conditionId")
    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    @Id
    @Column(name = "minCost")
    public double getMinCost() {
        return minCost;
    }

    public void setMinCost(double minCost) {
        this.minCost = minCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountByMinimalCostEntity that = (DiscountByMinimalCostEntity) o;
        return conditionId == that.conditionId && Double.compare(that.minCost, minCost) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionId, minCost);
    }
}
