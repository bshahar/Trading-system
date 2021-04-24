package Domain.DiscountPolicies;

import Domain.Bag;

import java.util.*;

public class DiscountByPurchaseTime extends DiscountPolicy {
    private boolean byDayInWeek;
    private boolean byDayInMonth;
    private boolean byHourInDay;

    private int dayInWeek;
    private int dayInMonth;
    private int beginHour;
    private int endHour;

    public DiscountByPurchaseTime(boolean byDayInWeek, boolean byDayInMonth, boolean byHourInDay, int dayInWeek, int dayInMonth, int beginHour, int endHour) {
        this.byDayInWeek = byDayInWeek;
        this.byDayInMonth = byDayInMonth;
        this.byHourInDay = byHourInDay;

        if(byDayInWeek)
            this.dayInWeek = dayInWeek;

        if(byDayInMonth)
            this.dayInMonth = dayInMonth;

        if(byHourInDay) {
            if(endHour != -1)
                this.endHour = endHour;
            else
                this.endHour = 0; //00:00
            if(beginHour != -1)
                this.beginHour = beginHour;
            else
                this.beginHour = 6;
        }
    }


    @Override
    public boolean validateCondition(int userId, Date time, Bag bag) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);

        if(byDayInWeek)
            return this.dayInWeek == calendar.get(Calendar.DAY_OF_WEEK);

        else if(byDayInMonth)
            return this.dayInMonth == calendar.get(Calendar.DAY_OF_MONTH);

        else
            return this.beginHour >= calendar.get(Calendar.HOUR) && this.endHour > calendar.get(Calendar.HOUR);
    }

}
