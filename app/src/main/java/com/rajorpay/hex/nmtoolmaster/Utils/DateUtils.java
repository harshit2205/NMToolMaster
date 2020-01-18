package com.rajorpay.hex.nmtoolmaster.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateUtils {

    static List<String> monthList = new ArrayList<>();

    public static String getLatestMonthandYear(String lastMonthandYear){
        String[] split = lastMonthandYear.split(", ",2);
        String month = split[0];
        String year = split[1];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH,getMonthInt(month));
        c.set(Calendar.YEAR, Integer.parseInt(year));
        c.add(Calendar.MONTH, 1);
        month = getMonth(c.get(Calendar.MONTH));
        year = Integer.toString(c.get(Calendar.YEAR));
        return month+", "+year;
    }

    public static int getMonthInt(String month){
        switch (month){
            case "Jan": return  0;
            case "Feb": return  1;
            case "Mar": return  2;
            case "Apr": return  3;
            case "May": return  4;
            case "Jun": return  5;
            case "Jul": return  6;
            case "Aug": return  7;
            case "Sep": return  8;
            case "Oct": return  9;
            case "Nov": return  10;
            case "Dec": return  11;
            default: return 1000;
        }
    }

    public static String getMonth(int monthInt){
        switch (monthInt){
            case 0: return "Jan";
            case 1: return "Feb";
            case 2: return "Mar";
            case 3: return "Apr";
            case 4: return "May";
            case 5: return "Jun";
            case 6: return "Jul";
            case 7: return "Aug";
            case 8: return "Sep";
            case 9: return "Oct";
            case 10: return "Nov";
            case 11: return "Dec";
            default: return null;
        }
    }

    public static String getCurrentMonthYear(){
        Calendar calendar = Calendar.getInstance();
        int monthInt = calendar.get(Calendar.MONTH);
        String month = getMonth(monthInt);
        return month+", "+calendar.get(Calendar.YEAR);
    }

    public static boolean isMoreThanCurrent(String paidTill){
        String[] paidTillSplit = paidTill.split(", ",2);
        String paidTillmonth = paidTillSplit[0];
        String paidTillYear = paidTillSplit[1];
        Calendar paidTillC = Calendar.getInstance();
        paidTillC.set(Calendar.MONTH,getMonthInt(paidTillmonth));
        paidTillC.add(Calendar.MONTH, 1);
        paidTillC.set(Calendar.YEAR, Integer.parseInt(paidTillYear));
        paidTillC.set(Calendar.DAY_OF_MONTH, 0);
        paidTillC.set(Calendar.HOUR, 0);
        paidTillC.set(Calendar.MINUTE, 0);
        paidTillC.set(Calendar.SECOND, 0);

        Calendar currentC = Calendar.getInstance();
        currentC.setTimeInMillis(System.currentTimeMillis());
        int value = paidTillC.compareTo(currentC);
        if(value >= 0){
            return true;
        }else{
            return false;
        }
    }
}
