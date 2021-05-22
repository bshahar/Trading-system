package Persistence.DAO;

import Domain.PurchasePolicies.TimeLimitPolicy;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "TimeLimitPolicy")
public class TimeLimitPolicyDAO {

    @DatabaseField(id = true)
    private int conditionId;
    @DatabaseField(id = true)
    private int HourInDay;

    public TimeLimitPolicyDAO() {
        // ORMLite needs a no-arg constructor
    }

    public TimeLimitPolicyDAO(int conditionId, int HourInDay) {
        this.conditionId = conditionId;
        this.HourInDay = HourInDay;
    }

    public int getConditionId() {
        return conditionId;
    }

    public int getHourInDay() {
        return HourInDay;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    public void setHourInDay(int HourInDay) {
        this.HourInDay = HourInDay;
    }

}



