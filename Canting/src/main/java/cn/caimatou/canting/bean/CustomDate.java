package cn.caimatou.canting.bean;

import java.io.Serializable;

import cn.caimatou.canting.utils.GLDateUtil;

/**
 * Description：
 * <br/><br/>Created by Fausgoal on 15/9/4.
 * <br/><br/>
 */
public class CustomDate implements Serializable {

    private static final long serialVersionUID = 1L;
    public int year;
    public int month;
    public int day;
    public int week;

    public CustomDate(int year, int month, int day) {
        if (month > 12) {
            month = 1;
            year++;
        } else if (month < 1) {
            month = 12;
            year--;
        }
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public CustomDate() {
        this.year = GLDateUtil.getYear();
        this.month = GLDateUtil.getMonth();
        this.day = GLDateUtil.getCurrentMonthDay();
    }

    public static CustomDate modifiDayForObject(CustomDate date, int day) {
        return new CustomDate(date.year, date.month, day);
    }
}
