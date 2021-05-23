package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "DiscountByMinimalCost")
public class DiscountByMinimalCostDAO {

    @DatabaseField(uniqueCombo = true)
    private int conditionId;
    @DatabaseField(uniqueCombo = true)
    private double minCost;

    public DiscountByMinimalCostDAO() {}

    public DiscountByMinimalCostDAO(int conditionId, double minCost) {
        this.conditionId = conditionId;
        this.minCost = minCost;
    }

    public int getConditionId() {
        return conditionId;
    }
    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    public double getMinCost() {
        return minCost;
    }
    public void setMinCost(double minCost) {
        this.minCost = minCost;
    }

}
