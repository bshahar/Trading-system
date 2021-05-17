package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "AgeLimitPolicy", schema = "zw9P3SlfWt", catalog = "")
@IdClass(AgeLimitPolicyEntityPK.class)
public class AgeLimitPolicyEntity {
    private int conditionId;
    private int ageLimit;

    @Id
    @Column(name = "conditionId")
    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    @Id
    @Column(name = "ageLimit")
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
        AgeLimitPolicyEntity that = (AgeLimitPolicyEntity) o;
        return conditionId == that.conditionId && ageLimit == that.ageLimit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionId, ageLimit);
    }
}
