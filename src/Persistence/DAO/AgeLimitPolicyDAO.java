package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "AgeLimitPolicy")
public class AgeLimitPolicyDAO {

    @DatabaseField(uniqueCombo = true)
    private int conditionId;
    @DatabaseField(uniqueCombo = true)
    private int ageLimit;

    public AgeLimitPolicyDAO() {
        // ORMLite needs a no-arg constructor
    }

    public AgeLimitPolicyDAO(int conditionId, int ageLimit) {
        this.conditionId = conditionId;
        this.ageLimit = ageLimit;
    }

    public int getConditionId() {
        return conditionId;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

}



