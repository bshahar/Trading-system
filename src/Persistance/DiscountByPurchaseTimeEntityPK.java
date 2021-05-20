package Persistance;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class DiscountByPurchaseTimeEntityPK implements Serializable {
    private int conditionId;
    private byte byDayInWeek;
    private int byDayInMonth;
    private byte byHourInDay;
    private int dayInWeek;
    private int dayInMonth;
    private int beginHour;
    private int endHour;

    @Column(name = "conditionId")
    @Id
    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    @Column(name = "byDayInWeek")
    @Id
    public byte getByDayInWeek() {
        return byDayInWeek;
    }

    public void setByDayInWeek(byte byDayInWeek) {
        this.byDayInWeek = byDayInWeek;
    }

    @Column(name = "byDayInMonth")
    @Id
    public int getByDayInMonth() {
        return byDayInMonth;
    }

    public void setByDayInMonth(int byDayInMonth) {
        this.byDayInMonth = byDayInMonth;
    }

    @Column(name = "byHourInDay")
    @Id
    public byte getByHourInDay() {
        return byHourInDay;
    }

    public void setByHourInDay(byte byHourInDay) {
        this.byHourInDay = byHourInDay;
    }

    @Column(name = "dayInWeek")
    @Id
    public int getDayInWeek() {
        return dayInWeek;
    }

    public void setDayInWeek(int dayInWeek) {
        this.dayInWeek = dayInWeek;
    }

    @Column(name = "dayInMonth")
    @Id
    public int getDayInMonth() {
        return dayInMonth;
    }

    public void setDayInMonth(int dayInMonth) {
        this.dayInMonth = dayInMonth;
    }

    @Column(name = "beginHour")
    @Id
    public int getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    @Column(name = "endHour")
    @Id
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
        DiscountByPurchaseTimeEntityPK that = (DiscountByPurchaseTimeEntityPK) o;
        return conditionId == that.conditionId && byDayInWeek == that.byDayInWeek && byDayInMonth == that.byDayInMonth && byHourInDay == that.byHourInDay && dayInWeek == that.dayInWeek && dayInMonth == that.dayInMonth && beginHour == that.beginHour && endHour == that.endHour;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionId, byDayInWeek, byDayInMonth, byHourInDay, dayInWeek, dayInMonth, beginHour, endHour);
    }
}
