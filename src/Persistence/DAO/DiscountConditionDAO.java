package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "DiscountCondition")
public class DiscountConditionDAO {

    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String logicOperator;

    public DiscountConditionDAO() {}

    public DiscountConditionDAO(int id, String logicOperator) {
        this.id = id;
        this.logicOperator = logicOperator;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getLogicOperator() {
        return logicOperator;
    }
    public void setLogicOperator(String logicOperator) {
        this.logicOperator = logicOperator;
    }
}
