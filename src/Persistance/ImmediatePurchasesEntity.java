package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ImmediatePurchases", schema = "zw9P3SlfWt", catalog = "")
public class ImmediatePurchasesEntity {
    private int id;
    private Integer conditionId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "conditionId")
    public Integer getConditionId() {
        return conditionId;
    }

    public void setConditionId(Integer conditionId) {
        this.conditionId = conditionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmediatePurchasesEntity that = (ImmediatePurchasesEntity) o;
        return id == that.id && Objects.equals(conditionId, that.conditionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, conditionId);
    }
}
