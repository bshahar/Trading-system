package Persistence.DAO;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ConditionalDiscount")
public class ConditionalDiscountDAO {

    @DatabaseField(uniqueCombo = true)
    private int conditionId;
    @DatabaseField(uniqueCombo = true)
    private int discountId;

    public ConditionalDiscountDAO() {}

    public ConditionalDiscountDAO(int conditionId, int discountId) {
        this.conditionId = conditionId;
        this.discountId = discountId;
    }

    public int getConditionId() {
        return conditionId;
    }
    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    public int getDiscountId() {
        return discountId;
    }
    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

}
