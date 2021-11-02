package com.huawei.courselearningdemo;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.huawei.courselearningdemo.utils.DateUtil;

import static org.junit.Assert.assertEquals;

public class DateUtilUnitTest {

    @Test
    public void testGetDateString1() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2021);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 18);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 30);
        Date date = calendar.getTime();
        assertEquals("2021-01-18 20:45:30", DateUtil.getDateString(date));
    }

    @Test
    public void testGetDateString2() {
        long time = new Long("1610973930000");
        Date date = new Date(time);
        assertEquals("2021-01-18 20:45:30", DateUtil.getDateString(date));
    }

    @Test
    public void testToDate() throws ParseException {
        String dateString = "2021-01-18 20:45:30";
        Date date = DateUtil.toDate(dateString);
        long expected = new Long("1610973930000");
        assertEquals(expected, date.getTime());
    }
}
