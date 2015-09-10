package cn.caimatou.canting.utils;


import android.content.Context;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.caimatou.canting.R;
import cn.caimatou.canting.bean.CustomDate;

public class GLDateUtil {

    public static final long HOURS = 60 * 60 * 1000;
    public static final long DAY = 24 * 60 * 60 * 1000;
    public static final long FIVE_MINUTES = 5 * 60 * 1000;
    public static final String YMD_FORMAT = "yyyy年MM月dd日";
    public static final String YMDHM_FORMAT = "yy/MM/dd HH:mm";
    public static final String MDHM_FORMAT = "MM/dd HH:mm";
    public static final String DAY_FORMAT = "MM月dd日";
    public static final String DAY_FORMAT_SHORT = "M月d日";
    public static final String YEAR_AND_MONTH_FORMAT = "yyyy年M月";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DAY_TIME_FORMAT = "MM月dd日 HH:mm";
    public static final String YYYY_MM_DD_HHMMSS_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_P_MM_P_DD_HHMMSS_FORMAT = "yyyy.MM.dd HH:mm:ss";

    public static final String[] WEEK_NAME = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public static int getMonthDays(int year, int month) {
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int days = 0;

        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29; // 闰年2月29天
        }

        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.getStackTrace();
        }

        return days;
    }

    public static boolean isToday(long timestamp) {
        long t = new Date().getTime() - timestamp;
        if (t > 0 && t < DAY)
            return true;
        else
            return false;
    }

    public static boolean isTomorrow(long timestamp) {
        long t = new Date().getTime() - timestamp;
        if (t < 0 && t < -DAY)
            return true;
        else
            return false;
    }

    public static boolean isCurrentMonth(CustomDate date) {
        return (date.year == getYear() &&
                date.month == getMonth());
    }

    public static String format(String format, long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(timestamp);
    }

    public static StringBuffer getDetailTime(long startTime, long endTime, long serviceTime, boolean isDetail) {
        int day = (int) ((startTime - serviceTime) / DAY);
        StringBuffer timeBuffer = new StringBuffer();
        if (isDetail) {
            timeBuffer.append(format(DAY_FORMAT, startTime));
        } else {
            if (day > 2 || day < -2)
                timeBuffer.append(format(DAY_FORMAT, startTime));
            else if (day == 2)
                timeBuffer.append(GLResourcesUtil.getString(R.string.after_tomorrow));
            else if (day == 1)
                timeBuffer.append(GLResourcesUtil.getString(R.string.tomorrow));
            else if (day == 0)
                timeBuffer.append(GLResourcesUtil.getString(R.string.today));
            else if (day == -1)
                timeBuffer.append(GLResourcesUtil.getString(R.string.yesterday));
            else if (day == -2)
                timeBuffer.append(GLResourcesUtil.getString(R.string.before_yesterday));
        }

        timeBuffer.append(" ");
        timeBuffer.append(format(TIME_FORMAT, startTime));
        if (endTime > 0) {
            timeBuffer.append("-");
            timeBuffer.append(format(TIME_FORMAT, endTime));
        }
        return timeBuffer;
    }

    public static String getListTimeTab(Context context, long time, long serviceTime) {
        int day = (int) ((time - serviceTime) / DAY);
        int hours = new Time(time).getHours();
        String timeString = null;
        if (day > 2)
            timeString = context.getString(R.string.two_day_after);
        else if (day == 2)
            timeString = context.getString(R.string.after_tomorrow);
        else if (day == 1)
            timeString = context.getString(R.string.tomorrow);
        else if (day == 0)
            timeString = hours + ":00-" + (hours + 1) + ":00";
        else if (day == -1)
            timeString = context.getString(R.string.yesterday);
        else if (day == -2)
            timeString = context.getString(R.string.before_yesterday);
        else if (day < -2)
            timeString = context.getString(R.string.two_day_before);
        return timeString;
    }

    // 添加大小月月份并将其转换为list,方便之后的判断
    private static String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    private static String[] months_little = {"4", "6", "9", "11"};
    private static List<String> list_big = Arrays.asList(months_big);
    private static List<String> list_little = Arrays.asList(months_little);

    /**
     * 获得今天日期
     *
     * @return 今天
     */
    public static long getTodayData() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    public static boolean isToday(int year, int month, int day) {
        return (year == getYear()
                && month == getMonth()
                && day == getCurrentMonthDay());
    }

    /**
     * 获得明天日期
     *
     * @return
     */
    public static long getTomorrowData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        if (day == 30) {
            if (list_big.contains(String.valueOf(month))) {
                day = 31;
            }
            if (list_little.contains(String.valueOf(month))) {
                day = 1;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            }
        } else if (day == 31) {
            day = 1;
            if (month == 12) {
                year++;
                month = 1;
            } else {
                month++;
            }
        } else {
            day++;
        }
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    /**
     * 获得后天日期
     *
     * @return
     */
    public static long getAfterTomorrowData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        if (day == 30) {
            if (list_big.contains(String.valueOf(month))) {
                day = 1;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            }
            if (list_little.contains(String.valueOf(month))) {
                day = 2;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            }
        } else if (day == 31) {
            day = 2;
            if (month == 12) {
                year++;
                month = 1;
            } else {
                month++;
            }
        } else {
            day = day + 2;
        }
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    /**
     * 获得两天后日期
     *
     * @return
     */
    public static long getTwoDayAfterData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        if (day == 30) {
            if (list_big.contains(String.valueOf(month))) {
                day = 3;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            }
            if (list_little.contains(String.valueOf(month))) {
                day = 3;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            }
        } else if (day == 31) {
            day = 3;
            if (month == 12) {
                year++;
                month = 1;
            } else {
                month++;
            }
        } else {
            day = day + 3;
        }
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeekDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static CustomDate getNextSunday() {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 7 - getWeekDay() + 1);
        return new CustomDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
    }

    public static int[] getWeekSunday(int year, int month, int day, int pervious) {
        int[] time = new int[3];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(Calendar.DAY_OF_MONTH, pervious);
        time[0] = c.get(Calendar.YEAR);
        time[1] = c.get(Calendar.MONTH) + 1;
        time[2] = c.get(Calendar.DAY_OF_MONTH);
        return time;

    }

    public static int getWeekDayFromDate(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(year, month));
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return week_index;
    }

    public static Date getDateFromString(int year, int month) {
        String dateString = year + "-" + (month > 9 ? month : ("0" + month)) + "-01";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }

    public static String minsToString(int mins) {
        int hours = mins / 60;
        mins = mins - hours * 60;
        return String.format("%02d:%02d", hours, mins);
    }

    public static long getDay(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTimeInMillis();
    }

    /**
     * 显示 （今天/ 昨天) date + week
     *
     * @param time
     * @return
     */
    public static String getDayAndWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);

        int week_index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }

        String dayAndWeek;
        if (isToday(year, month, date)) {
            // 今天
            dayAndWeek = GLResourcesUtil.getString(R.string.today) + " ";
        } else {
            long day = (long) Math.ceil(System.currentTimeMillis() / DAY);// 天前

            int days = (int) (day - (calendar.getTimeInMillis() / DAY));
            days = Math.abs(days);
            if (days == 1) {
                dayAndWeek = GLResourcesUtil.getString(R.string.yesterday) + " ";
            } else {
                dayAndWeek = "";
            }
        }
        dayAndWeek += format(DAY_FORMAT_SHORT, time) + WEEK_NAME[week_index];
        return dayAndWeek;
    }
}
