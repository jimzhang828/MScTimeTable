package cs.hku.zwj.timetable_test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Hugo
 * @date 2020/5/6
 */
public class Caltest {
    public static void main(String[] args) throws ParseException {
//        Date date = new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar calendar = Calendar.getInstance();
//        date = simpleDateFormat.parse("2020-05-06");
//        calendar.setTime(date);
//        int weekNum = calendar.get(Calendar.WEEK_OF_YEAR);
//        System.out.println("WEEK_OF_YEAR: "+weekNum);
//        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
//        System.out.println("DAY_OF_WEEK_IN_MONTH: "+dayOfWeek);
//        int dayMonth = calendar.get(Calendar.DAY_OF_MONTH);
//        System.out.println("DAY_OF_MONTH: " + dayMonth);
//        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
//        System.out.println("DAY_OF_WEEK: " + dayWeek);
//        int month = calendar.get(Calendar.MONTH);
//        System.out.println("MONTH: " + month);
//        int dayYear = calendar.get(Calendar.DAY_OF_YEAR);
//        System.out.println("DAY_OF_YEAR: " + dayYear);

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.WEEK_OF_YEAR, 18);
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);

        String months[] = {
                "Jan", "Feb", "Mar", "Apr",
                "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"};
        String month = months[calendar.get(Calendar.MONTH)];
        System.out.println("Month: " + month);
        System.out.println("Day: " + calendar.get(Calendar.DAY_OF_MONTH));

        calendar.add(Calendar.DAY_OF_MONTH, 6);
        System.out.println("Day: " + calendar.get(Calendar.DAY_OF_MONTH));
//        String show = simpleDateFormat.format(date);
//        System.out.println(show);

    }
}
