package com.misis.brs;

import java.util.Calendar;

/**
 * Класс помощник для перевода unix времени в строковое представление
 */
public class TimeHelper {

    /**
     * функция конвертации времени
     * @param sec unix время в секундах
     * @return время в формате дд.мм.гггг
     */
    public static String getTime(long sec){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sec*1000);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH)+1; //так как месяцы считаются с 0
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        return mDay+"."+mMonth+"."+mYear;
    }

    public static String getNotifTime(long sec){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sec*1000);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH)+1; //так как месяцы считаются с 0
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR);
        int mMin = calendar.get(Calendar.MINUTE);
        return mHour+":"+mMin+" "+mDay+"."+mMonth+"."+mYear;
    }
}
