package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Discounts", schema = "zw9P3SlfWt", catalog = "")
public class DiscountsEntity {
    private String mathOperator;
    private String beginDate;
    private String endDate;
    private Integer percentage;
    private int id;

    @Basic
    @Column(name = "mathOperator")
    public String getMathOperator() {
        return mathOperator;
    }

    public void setMathOperator(String mathOperator) {
        this.mathOperator = mathOperator;
    }

    @Basic
    @Column(name = "beginDate")
    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    @Basic
    @Column(name = "endDate")
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Basic
    @Column(name = "percentage")
    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
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
        DiscountsEntity that = (DiscountsEntity) o;
        return id == that.id && Objects.equals(mathOperator, that.mathOperator) && Objects.equals(beginDate, that.beginDate) && Objects.equals(endDate, that.endDate) && Objects.equals(percentage, that.percentage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mathOperator, beginDate, endDate, percentage, id);
    }
}
