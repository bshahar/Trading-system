package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "DiscountByMinimalAmount")

public class DiscountByMinimalAmountDAO {

    @DatabaseField(uniqueCombo = true)
    private int conditionId;
    @DatabaseField(uniqueCombo = true)
    private int productId;
    @DatabaseField(uniqueCombo = true)
    private int minAmount;

    public DiscountByMinimalAmountDAO() {}

    public DiscountByMinimalAmountDAO(int conditionId, int productId, int minAmount) {
        this.conditionId = conditionId;
        this.productId = productId;
        this.minAmount = minAmount;
    }


    public int getConditionId() {
        return conditionId;
    }
    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getMinAmount() {
        return minAmount;
    }
    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

}
