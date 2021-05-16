package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class DiscountByMinimalCostEntityPK implements Serializable {
    private int conditionId;
    private double minCost;

    @Column(name = "conditionId")
    @Id
    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    @Column(name = "minCost")
    @Id
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
        DiscountByMinimalCostEntityPK that = (DiscountByMinimalCostEntityPK) o;
        return conditionId == that.conditionId && Double.compare(that.minCost, minCost) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionId, minCost);
    }
}
