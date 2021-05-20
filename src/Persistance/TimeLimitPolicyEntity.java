package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TimeLimitPolicy", schema = "zw9P3SlfWt", catalog = "")
@IdClass(TimeLimitPolicyEntityPK.class)
public class TimeLimitPolicyEntity {
    private int conditionId;
    private int hourInDay;

    @Id
    @Column(name = "conditionId")
    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    @Id
    @Column(name = "HourInDay")
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
        TimeLimitPolicyEntity that = (TimeLimitPolicyEntity) o;
        return conditionId == that.conditionId && hourInDay == that.hourInDay;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionId, hourInDay);
    }
}
