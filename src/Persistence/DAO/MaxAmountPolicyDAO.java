package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MaxAmountPolicy")
public class MaxAmountPolicyDAO {

    @DatabaseField(id = true)
    private int conditionId;
    @DatabaseField(id = true)
    private int productId;
    @DatabaseField(id = true)
    private int maxAmount;

    public MaxAmountPolicyDAO() {
        // ORMLite needs a no-arg constructor
    }

    public MaxAmountPolicyDAO(int conditionId, int productId, int maxAmount) {
        this.conditionId = conditionId;
        this.productId = productId;
        this.maxAmount = maxAmount;
    }


    public int getConditionId() {
        return conditionId;
    }

    public int getProductId() {
        return productId;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

}

