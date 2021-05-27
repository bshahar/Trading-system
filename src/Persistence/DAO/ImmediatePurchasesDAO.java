package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ImmediatePurchases")
public class ImmediatePurchasesDAO {

    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private int conditionId;

    public ImmediatePurchasesDAO() {
        // ORMLite needs a no-arg constructor
    }

    public ImmediatePurchasesDAO(int id, int conditionId) {
        this.id = id;
        this.conditionId = conditionId;
    }

    public int getId() {
        return id;
    }

    public int getConditionId() {
        return conditionId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }
}

