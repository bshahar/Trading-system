package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "DiscountCondition", schema = "zw9P3SlfWt", catalog = "")
public class DiscountConditionEntity {
    private String logicOperator;
    private int id;

    @Basic
    @Column(name = "logicOperator")
    public String getLogicOperator() {
        return logicOperator;
    }

    public void setLogicOperator(String logicOperator) {
        this.logicOperator = logicOperator;
    }

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountConditionEntity that = (DiscountConditionEntity) o;
        return id == that.id && Objects.equals(logicOperator, that.logicOperator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logicOperator, id);
    }
}
