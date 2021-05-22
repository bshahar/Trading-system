package Persistence.DAO;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "DiscountByMinimalCost")
public class ConditionalDiscountDAO {

    @DatabaseField(id = true)
    private int conditionId;
    @DatabaseField(id = true)
    private double discountId;

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

    public double getDiscountId() {
        return discountId;
    }
    public void setDiscountId(double discountId) {
        this.discountId = discountId;
    }

}
