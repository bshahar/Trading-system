package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class AgeLimitPolicyEntityPK implements Serializable {
    private int conditionId;
    private int ageLimit;

    @Column(name = "conditionId")
    @Id
    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    @Column(name = "ageLimit")
    @Id
    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgeLimitPolicyEntityPK that = (AgeLimitPolicyEntityPK) o;
        return conditionId == that.conditionId && ageLimit == that.ageLimit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionId, ageLimit);
    }
}
