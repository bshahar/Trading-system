package Persistance;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "DiscountByPurchaseTime", schema = "zw9P3SlfWt", catalog = "")
@IdClass(DiscountByPurchaseTimeEntityPK.class)
public class DiscountByPurchaseTimeEntity {
    private int conditionId;
    private boolean byDayInWeek;
    private boolean byDayInMonth;
    private boolean byHourInDay;
    private int dayInWeek;
    private int dayInMonth;
    private int beginHour;
    private int endHour;

    @Id
    @Column(name = "conditionId")
    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    @Id
    @Column(name = "byDayInWeek")
    public boolean getByDayInWeek() {
        return byDayInWeek;
    }

    public void setByDayInWeek(boolean byDayInWeek) {
        this.byDayInWeek = byDayInWeek;
    }

    @Id
    @Column(name = "byDayInMonth")
    public boolean getByDayInMonth() {
        return byDayInMonth;
    }

    public void setByDayInMonth(boolean byDayInMonth) {
        this.byDayInMonth = byDayInMonth;
    }

    @Id
    @Column(name = "byHourInDay")
    public boolean getByHourInDay() {
        return byHourInDay;
    }

    public void setByHourInDay(boolean byHourInDay) {
        this.byHourInDay = byHourInDay;
    }

    @Id
    @Column(name = "dayInWeek")
    public int getDayInWeek() {
        return dayInWeek;
    }

    public void setDayInWeek(int dayInWeek) {
        this.dayInWeek = dayInWeek;
    }

    @Id
    @Column(name = "dayInMonth")
    public int getDayInMonth() {
        return dayInMonth;
    }

    public void setDayInMonth(int dayInMonth) {
        this.dayInMonth = dayInMonth;
    }

    @Id
    @Column(name = "beginHour")
    public int getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    @Id
    @Column(name = "endHour")
    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountByPurchaseTimeEntity that = (DiscountByPurchaseTimeEntity) o;
        return conditionId == that.conditionId && byDayInWeek == that.byDayInWeek && byDayInMonth == that.byDayInMonth && byHourInDay == that.byHourInDay && dayInWeek == that.dayInWeek && dayInMonth == that.dayInMonth && beginHour == that.beginHour && endHour == that.endHour;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionId, byDayInWeek, byDayInMonth, byHourInDay, dayInWeek, dayInMonth, beginHour, endHour);
    }
}
