package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class TimeLimitPolicyEntityPK implements Serializable {
    private int conditionId;
    private int hourInDay;

    @Column(name = "conditionId")
    @Id
    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    @Column(name = "HourInDay")
    @Id
    public int getHourInDay() {
        return hourInDay;
    }

    public void setHourInDay(int hourInDay) {
        this.hourInDay = hourInDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeLimitPolicyEntityPK that = (TimeLimitPolicyEntityPK) o;
        return conditionId == that.conditionId && hourInDay == that.hourInDay;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionId, hourInDay);
    }
}
