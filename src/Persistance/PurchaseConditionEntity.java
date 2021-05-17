package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PurchaseCondition", schema = "zw9P3SlfWt", catalog = "")
public class PurchaseConditionEntity {
    private int id;
    private String logicOperator;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "logicOperator")
    public String getLogicOperator() {
        return logicOperator;
    }

    public void setLogicOperator(String logicOperator) {
        this.logicOperator = logicOperator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseConditionEntity that = (PurchaseConditionEntity) o;
        return id == that.id && Objects.equals(logicOperator, that.logicOperator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, logicOperator);
    }
}
