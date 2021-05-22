package Persistence.DAO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "DiscountByPurchaseTime")
public class DiscountByPurchaseTimeDAO {

    @DatabaseField(id = true)
    private int conditionId;
    @DatabaseField(id = true)
    private boolean byDayInWeek;
    @DatabaseField(id = true)
    private boolean byDayInMonth;
    @DatabaseField(id = true)
    private boolean byHourInDay;
    @DatabaseField(id = true)
    private int dayInWeek;
    @DatabaseField(id = true)
    private int dayInMonth;
    @DatabaseField(id = true)
    private int beginHour;
    @DatabaseField(id = true)
    private int endHour;

    public DiscountByPurchaseTimeDAO() {}

    public DiscountByPurchaseTimeDAO(int conditionId, boolean byDayInWeek, boolean byDayInMonth, boolean byHourInDay,
                                     int dayInWeek, int dayInMonth, int beginHour, int endHour) {
        this.conditionId = conditionId;
        this.byDayInWeek = byDayInWeek;
        this.byDayInMonth = byDayInMonth;
        this.dayInWeek = dayInWeek;
        this.dayInMonth = dayInMonth;
        this.beginHour = beginHour;
        this.endHour = endHour;
    }

    public int getConditionId() {
        return conditionId;
    }
    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    public boolean isByDayInWeek() {
        return byDayInWeek;
    }
    public void setByDayInWeek(boolean byDayInWeek) {
        this.byDayInWeek = byDayInWeek;
    }

    public boolean isByDayInMonth() {
        return byDayInMonth;
    }
    public void setByDayInMonth(boolean byDayInMonth) {
        this.byDayInMonth = byDayInMonth;
    }

    public boolean isByHourInDay() {
        return byHourInDay;
    }
    public void setByHourInDay(boolean byHourInDay) {
        this.byHourInDay = byHourInDay;
    }

    public int getDayInWeek() {
        return dayInWeek;
    }
    public void setDayInWeek(int dayInWeek) {
        this.dayInWeek = dayInWeek;
    }

    public int getDayInMonth() {
        return dayInMonth;
    }
    public void setDayInMonth(int dayInMonth) {
        this.dayInMonth = dayInMonth;
    }

    public int getBeginHour() {
        return beginHour;
    }
    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    public int getEndHour() {
        return endHour;
    }
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

}
