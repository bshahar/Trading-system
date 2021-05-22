package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "PurchaseCondition")
public class PurchaseConditionDAO {

    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String logicOperator;

    public PurchaseConditionDAO() {
        // ORMLite needs a no-arg constructor
    }

    public PurchaseConditionDAO(int id, String logicOperator) {
        this.id = id;
        this.logicOperator = logicOperator;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogicOperator(String logicOperator) {
        this.logicOperator = logicOperator;
    }

    public int getId() {
        return id;
    }

    public String getLogicOperator() {
        return logicOperator;
    }

}


