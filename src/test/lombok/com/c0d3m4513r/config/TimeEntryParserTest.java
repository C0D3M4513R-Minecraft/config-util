package com.c0d3m4513r.config;

import com.c0d3m4513r.LoggerUtils;
import lombok.val;
import lombok.var;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TimeEntryParserTest {
    @BeforeAll
    public static void init() {
        LoggerUtils.initLogger("TimeUnitValueTest");
    }

    public static ArrayList<TimeUnitValue> getTimeUnitValues() {
        final ArrayList<TimeUnitValue> list = new ArrayList<>(TimeUnitValueTest.timeUnitValues.size());
        for (val entry : TimeUnitValueTest.timeUnitValues) {
            list.add(new TimeUnitValue(entry.getRight(), entry.getMiddle()));
        }
        return list;
    }

    @DisplayName("Test TimeEntry#new()")
    @Test
    public void testNew() {
        TimeEntry te = new TimeEntry();
        assertNotNull(te);
        assertEquals(0, te.getDays());
        assertEquals(0, te.getHours());
        assertEquals(0, te.getMinutes());
        assertEquals(0, te.getSeconds());
        assertEquals(0, te.getMs());
        assertEquals(0, te.getUs());
        assertEquals(0, te.getNs());
    }

    @DisplayName("Test TimeEntry#new(long, long, long, long, long, long, long)")
    @Test
    public void testNewAllArgs(){
        final long days = 1;
        final long hours = 2;
        final long minutes = 3;
        final long seconds = 4;
        final long ms = 5;
        final long us = 6;
        final long ns = 7;
        TimeEntry te = new TimeEntry(days, hours, minutes, seconds, ms, us, ns);
        assertNotNull(te);
        assertEquals(days, te.getDays());
        assertEquals(hours, te.getHours());
        assertEquals(minutes, te.getMinutes());
        assertEquals(seconds, te.getSeconds());
        assertEquals(ms, te.getMs());
        assertEquals(us, te.getUs());
        assertEquals(ns, te.getNs());
    }

    @DisplayName("Test TimeEntry#new(TimeEntry)")
    @Test
    public void testNewCopy(){
        final long days = 1;
        final long hours = 2;
        final long minutes = 3;
        final long seconds = 4;
        final long ms = 5;
        final long us = 6;
        final long ns = 7;
        TimeEntry te = new TimeEntry(days, hours, minutes, seconds, ms, us, ns);
        assertNotNull(te);
        assertEquals(days, te.getDays());
        assertEquals(hours, te.getHours());
        assertEquals(minutes, te.getMinutes());
        assertEquals(seconds, te.getSeconds());
        assertEquals(ms, te.getMs());
        assertEquals(us, te.getUs());
        assertEquals(ns, te.getNs());
        TimeEntry te2 = new TimeEntry(te);
        assertNotNull(te2);
        assertNotSame(te, te2);
        assertEquals(days, te2.getDays());
        assertEquals(hours, te2.getHours());
        assertEquals(minutes, te2.getMinutes());
        assertEquals(seconds, te2.getSeconds());
        assertEquals(ms, te2.getMs());
        assertEquals(us, te2.getUs());
        assertEquals(ns, te2.getNs());
    }
    @DisplayName("Test TimeEntry#new(TimeUnit, long, TimeEntry)")
    @Test
    public void testNewCopyAndSet(){
        final long days = 1;
        final long hours = 2;
        final long minutes = 3;
        final long seconds = 4;
        final long ms = 5;
        final long us = 6;
        final long ns = 7;
        TimeEntry te = new TimeEntry(days, hours, minutes, seconds, ms, us, ns);
        assertNotNull(te);
        assertEquals(days, te.getDays());
        assertEquals(hours, te.getHours());
        assertEquals(minutes, te.getMinutes());
        assertEquals(seconds, te.getSeconds());
        assertEquals(ms, te.getMs());
        assertEquals(us, te.getUs());
        assertEquals(ns, te.getNs());
        final long newDays = 10;
        TimeEntry te2 = new TimeEntry(TimeUnit.DAYS, newDays, te);
        assertNotNull(te2);
        assertNotSame(te, te2);
        assertNotEquals(te, te2);
        assertEquals(newDays, te2.getDays());
        assertEquals(hours, te2.getHours());
        assertEquals(minutes, te2.getMinutes());
        assertEquals(seconds, te2.getSeconds());
        assertEquals(ms, te2.getMs());
        assertEquals(us, te2.getUs());
        assertEquals(ns, te2.getNs());
    }
    @DisplayName("Test TimeEntry#getTime(TimeUnit)")
    @Test
    public void getTimeTest() {
        val test = new TimeEntry(1, 2, 3, 4, 5, 6, 7);
        assertEquals(1, test.getTime(TimeUnit.DAYS));
        assertEquals(2, test.getTime(TimeUnit.HOURS));
        assertEquals(3, test.getTime(TimeUnit.MINUTES));
        assertEquals(4, test.getTime(TimeUnit.SECONDS));
        assertEquals(5, test.getTime(TimeUnit.MILLISECONDS));
        assertEquals(6, test.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, test.getTime(TimeUnit.NANOSECONDS));
    }

    @DisplayName("Test TimeEntry#addTime(TimeUnit, long)")
    @Test
    public void testAddTime(){
        val test = new TimeEntry(1, 2, 3, 4, 5, 6, 7);
        assertNotNull(test);
        assertEquals(1, test.getTime(TimeUnit.DAYS));
        assertEquals(2, test.getTime(TimeUnit.HOURS));
        assertEquals(3, test.getTime(TimeUnit.MINUTES));
        assertEquals(4, test.getTime(TimeUnit.SECONDS));
        assertEquals(5, test.getTime(TimeUnit.MILLISECONDS));
        assertEquals(6, test.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, test.getTime(TimeUnit.NANOSECONDS));
        val add = test.addTime(TimeUnit.DAYS, 10);
        assertNotNull(add);
        assertNotSame(test, add);
        assertEquals(11, add.getTime(TimeUnit.DAYS));
        assertEquals(2, add.getTime(TimeUnit.HOURS));
        assertEquals(3, add.getTime(TimeUnit.MINUTES));
        assertEquals(4, add.getTime(TimeUnit.SECONDS));
        assertEquals(5, add.getTime(TimeUnit.MILLISECONDS));
        assertEquals(6, add.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, add.getTime(TimeUnit.NANOSECONDS));
        val add2 = add.addTime(TimeUnit.HOURS, 10);
        assertNotNull(add2);
        assertNotSame(test, add2);
        assertNotSame(add, add2);
        assertEquals(11, add2.getTime(TimeUnit.DAYS));
        assertEquals(12, add2.getTime(TimeUnit.HOURS));
        assertEquals(3, add2.getTime(TimeUnit.MINUTES));
        assertEquals(4, add2.getTime(TimeUnit.SECONDS));
        assertEquals(5, add2.getTime(TimeUnit.MILLISECONDS));
        assertEquals(6, add2.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, add2.getTime(TimeUnit.NANOSECONDS));
        val add3 = add2.addTime(TimeUnit.MINUTES, 10);
        assertNotNull(add3);
        assertNotSame(test, add3);
        assertNotSame(add, add3);
        assertNotSame(add2, add3);
        assertEquals(11, add3.getTime(TimeUnit.DAYS));
        assertEquals(12, add3.getTime(TimeUnit.HOURS));
        assertEquals(13, add3.getTime(TimeUnit.MINUTES));
        assertEquals(4, add3.getTime(TimeUnit.SECONDS));
        assertEquals(5, add3.getTime(TimeUnit.MILLISECONDS));
        assertEquals(6, add3.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, add3.getTime(TimeUnit.NANOSECONDS));
        val add4 = add3.addTime(TimeUnit.SECONDS, 10);
        assertNotNull(add4);
        assertNotSame(test, add4);
        assertNotSame(add, add4);
        assertNotSame(add2, add4);
        assertNotSame(add3, add4);
        assertEquals(11, add4.getTime(TimeUnit.DAYS));
        assertEquals(12, add4.getTime(TimeUnit.HOURS));
        assertEquals(13, add4.getTime(TimeUnit.MINUTES));
        assertEquals(14, add4.getTime(TimeUnit.SECONDS));
        assertEquals(5, add4.getTime(TimeUnit.MILLISECONDS));
        assertEquals(6, add4.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, add4.getTime(TimeUnit.NANOSECONDS));
        val add5 = add4.addTime(TimeUnit.MILLISECONDS, 10);
        assertNotNull(add5);
        assertNotSame(test, add5);
        assertNotSame(add, add5);
        assertNotSame(add2, add5);
        assertNotSame(add3, add5);
        assertNotSame(add4, add5);
        assertEquals(11, add5.getTime(TimeUnit.DAYS));
        assertEquals(12, add5.getTime(TimeUnit.HOURS));
        assertEquals(13, add5.getTime(TimeUnit.MINUTES));
        assertEquals(14, add5.getTime(TimeUnit.SECONDS));
        assertEquals(15, add5.getTime(TimeUnit.MILLISECONDS));
        assertEquals(6, add5.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, add5.getTime(TimeUnit.NANOSECONDS));
        val add6 = add5.addTime(TimeUnit.MICROSECONDS, 10);
        assertNotNull(add6);
        assertNotSame(test, add6);
        assertNotSame(add, add6);
        assertNotSame(add2, add6);
        assertNotSame(add3, add6);
        assertNotSame(add4, add6);
        assertNotSame(add5, add6);
        assertEquals(11, add6.getTime(TimeUnit.DAYS));
        assertEquals(12, add6.getTime(TimeUnit.HOURS));
        assertEquals(13, add6.getTime(TimeUnit.MINUTES));
        assertEquals(14, add6.getTime(TimeUnit.SECONDS));
        assertEquals(15, add6.getTime(TimeUnit.MILLISECONDS));
        assertEquals(16, add6.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, add6.getTime(TimeUnit.NANOSECONDS));
        val add7 = add6.addTime(TimeUnit.NANOSECONDS, 10);
        assertNotNull(add7);
        assertNotSame(test, add7);
        assertNotSame(add, add7);
        assertNotSame(add2, add7);
        assertNotSame(add3, add7);
        assertNotSame(add4, add7);
        assertNotSame(add5, add7);
        assertNotSame(add6, add7);
        assertEquals(11, add7.getTime(TimeUnit.DAYS));
        assertEquals(12, add7.getTime(TimeUnit.HOURS));
        assertEquals(13, add7.getTime(TimeUnit.MINUTES));
        assertEquals(14, add7.getTime(TimeUnit.SECONDS));
        assertEquals(15, add7.getTime(TimeUnit.MILLISECONDS));
        assertEquals(16, add7.getTime(TimeUnit.MICROSECONDS));
        assertEquals(17, add7.getTime(TimeUnit.NANOSECONDS));
    }

    @DisplayName("Test TimeEntry#of(String)")
    @Test
    public void parseSingleString() {
        {
            final StringBuilder sb = new StringBuilder();
            for (val entry : TimeUnitValueTest.timeUnitValues) {
                sb.append(entry.getMiddle()).append(entry.getLeft());
            }
            final String parse = sb.toString();
            TimeEntry te = TimeEntry.of(parse);
            assertNotNull(te);
            assertEquals(0, te.getDays());
            assertEquals(0, te.getHours());
            assertEquals(0, te.getMinutes());
            assertEquals(0, te.getSeconds());
            assertEquals(0, te.getMs());
            assertEquals(0, te.getUs());
            assertEquals(0, te.getNs());
        }
        final long absValue = 150;
        {
            final StringBuilder sb = new StringBuilder();
            for (val entry : TimeUnitValueTest.timeUnitValues) {
                if (entry.getMiddle() >= 0) sb.append(entry.getMiddle()).append(entry.getLeft());
            }
            final String parse = sb.toString();
            TimeEntry te = TimeEntry.of(parse);
            assertNotNull(te);
            assertEquals(absValue * 3, te.getDays());
            assertEquals(absValue * 3, te.getHours());
            assertEquals(absValue * 4, te.getMinutes());
            assertEquals(absValue * 4, te.getSeconds());
            assertEquals(0, te.getMs());
            assertEquals(0, te.getUs());
            assertEquals(0, te.getNs());
        }
    }

    @DisplayName("Test TimeEntry#of(TimeUnitValue[])")
    @Test
    public void parseArray() {
        final long absValue = 150;
        {
            val parse = getTimeUnitValues().toArray(new TimeUnitValue[0]);
            TimeEntry te = TimeEntry.of(parse);
            assertNotNull(te);
            assertEquals(0, te.getDays());
            assertEquals(0, te.getHours());
            assertEquals(0, te.getMinutes());
            assertEquals(0, te.getSeconds());
            assertEquals(0, te.getMs());
            assertEquals(0, te.getUs());
            assertEquals(0, te.getNs());
        }
        {
            val parse = getTimeUnitValues();
            parse.removeIf(timeUnitValue -> timeUnitValue.getValue() < 0);
            val parseArray = parse.toArray(new TimeUnitValue[0]);
            TimeEntry te = TimeEntry.of(parseArray);
            assertNotNull(te);
            assertEquals(absValue * 3, te.getDays());
            assertEquals(absValue * 3, te.getHours());
            assertEquals(absValue * 4, te.getMinutes());
            assertEquals(absValue * 4, te.getSeconds());
            assertEquals(0, te.getMs());
            assertEquals(0, te.getUs());
            assertEquals(0, te.getNs());
        }
    }

    @DisplayName("Test TimeEntry#of(Iterable<TimeUnitValue>)")
    @Test
    public void parseIterable() {
        final long absValue = 150;
        {
            val parse = getTimeUnitValues();
            TimeEntry te = TimeEntry.of(parse);
            assertNotNull(te);
            assertEquals(0, te.getDays());
            assertEquals(0, te.getHours());
            assertEquals(0, te.getMinutes());
            assertEquals(0, te.getSeconds());
            assertEquals(0, te.getMs());
            assertEquals(0, te.getUs());
            assertEquals(0, te.getNs());
        }
        {
            val parse = getTimeUnitValues();
            parse.removeIf(timeUnitValue -> timeUnitValue.getValue() < 0);
            TimeEntry te = TimeEntry.of(parse);
            assertNotNull(te);
            assertEquals(absValue * 3, te.getDays());
            assertEquals(absValue * 3, te.getHours());
            assertEquals(absValue * 4, te.getMinutes());
            assertEquals(absValue * 4, te.getSeconds());
            assertEquals(0, te.getMs());
            assertEquals(0, te.getUs());
            assertEquals(0, te.getNs());
        }
    }

    @DisplayName("Test TimeEntry#of(TimeUnitValue)")
    @Test
    public void testOfTUV(){
        var tuv = new TimeUnitValue(TimeUnit.DAYS, 10);
        assertNotNull(tuv);
        var te = TimeEntry.of(tuv);
        assertNotNull(te);
        assertEquals(10, te.getDays());
        assertEquals(0, te.getHours());
        assertEquals(0, te.getMinutes());
        assertEquals(0, te.getSeconds());
        assertEquals(0, te.getMs());
        assertEquals(0, te.getUs());
        assertEquals(0, te.getNs());
        tuv = new TimeUnitValue(TimeUnit.HOURS, 10);
        assertNotNull(tuv);
        te = TimeEntry.of(tuv);
        assertNotNull(te);
        assertEquals(0, te.getDays());
        assertEquals(10, te.getHours());
        assertEquals(0, te.getMinutes());
        assertEquals(0, te.getSeconds());
        assertEquals(0, te.getMs());
        assertEquals(0, te.getUs());
        assertEquals(0, te.getNs());
        tuv = new TimeUnitValue(TimeUnit.MINUTES, 10);
        assertNotNull(tuv);
        te = TimeEntry.of(tuv);
        assertNotNull(te);
        assertEquals(0, te.getDays());
        assertEquals(0, te.getHours());
        assertEquals(10, te.getMinutes());
        assertEquals(0, te.getSeconds());
        assertEquals(0, te.getMs());
        assertEquals(0, te.getUs());
        assertEquals(0, te.getNs());
        tuv = new TimeUnitValue(TimeUnit.SECONDS, 10);
        assertNotNull(tuv);
        te = TimeEntry.of(tuv);
        assertNotNull(te);
        assertEquals(0, te.getDays());
        assertEquals(0, te.getHours());
        assertEquals(0, te.getMinutes());
        assertEquals(10, te.getSeconds());
        assertEquals(0, te.getMs());
        assertEquals(0, te.getUs());
        assertEquals(0, te.getNs());
        tuv = new TimeUnitValue(TimeUnit.MILLISECONDS, 10);
        assertNotNull(tuv);
        te = TimeEntry.of(tuv);
        assertNotNull(te);
        assertEquals(0, te.getDays());
        assertEquals(0, te.getHours());
        assertEquals(0, te.getMinutes());
        assertEquals(0, te.getSeconds());
        assertEquals(10, te.getMs());
        assertEquals(0, te.getUs());
        assertEquals(0, te.getNs());
        tuv = new TimeUnitValue(TimeUnit.MICROSECONDS, 10);
        assertNotNull(tuv);
        te = TimeEntry.of(tuv);
        assertNotNull(te);
        assertEquals(0, te.getDays());
        assertEquals(0, te.getHours());
        assertEquals(0, te.getMinutes());
        assertEquals(0, te.getSeconds());
        assertEquals(0, te.getMs());
        assertEquals(10, te.getUs());
        assertEquals(0, te.getNs());
        tuv = new TimeUnitValue(TimeUnit.NANOSECONDS, 10);
        assertNotNull(tuv);
        te = TimeEntry.of(tuv);
        assertNotNull(te);
        assertEquals(0, te.getDays());
        assertEquals(0, te.getHours());
        assertEquals(0, te.getMinutes());
        assertEquals(0, te.getSeconds());
        assertEquals(0, te.getMs());
        assertEquals(0, te.getUs());
        assertEquals(10, te.getNs());
    }

    @DisplayName("Test TimeEntry#setTime(TimeUnit, long)")
    @Test
    public void setTimeTest() {
        var test = new TimeEntry(1, 2, 3, 4, 5, 6, 7);
        assertEquals(1, test.getTime(TimeUnit.DAYS));
        assertEquals(2, test.getTime(TimeUnit.HOURS));
        assertEquals(3, test.getTime(TimeUnit.MINUTES));
        assertEquals(4, test.getTime(TimeUnit.SECONDS));
        assertEquals(5, test.getTime(TimeUnit.MILLISECONDS));
        assertEquals(6, test.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, test.getTime(TimeUnit.NANOSECONDS));
        test = test.setTime(TimeUnit.DAYS, 10);
        assertEquals(10, test.getTime(TimeUnit.DAYS));
        assertEquals(2, test.getTime(TimeUnit.HOURS));
        assertEquals(3, test.getTime(TimeUnit.MINUTES));
        assertEquals(4, test.getTime(TimeUnit.SECONDS));
        assertEquals(5, test.getTime(TimeUnit.MILLISECONDS));
        assertEquals(6, test.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, test.getTime(TimeUnit.NANOSECONDS));
        test = test.setTime(TimeUnit.HOURS, 11);
        assertEquals(10, test.getTime(TimeUnit.DAYS));
        assertEquals(11, test.getTime(TimeUnit.HOURS));
        assertEquals(3, test.getTime(TimeUnit.MINUTES));
        assertEquals(4, test.getTime(TimeUnit.SECONDS));
        assertEquals(5, test.getTime(TimeUnit.MILLISECONDS));
        assertEquals(6, test.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, test.getTime(TimeUnit.NANOSECONDS));
        test = test.setTime(TimeUnit.MINUTES, 12);
        assertEquals(10, test.getTime(TimeUnit.DAYS));
        assertEquals(11, test.getTime(TimeUnit.HOURS));
        assertEquals(12, test.getTime(TimeUnit.MINUTES));
        assertEquals(4, test.getTime(TimeUnit.SECONDS));
        assertEquals(5, test.getTime(TimeUnit.MILLISECONDS));
        assertEquals(6, test.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, test.getTime(TimeUnit.NANOSECONDS));
        test = test.setTime(TimeUnit.SECONDS, 13);
        assertEquals(10, test.getTime(TimeUnit.DAYS));
        assertEquals(11, test.getTime(TimeUnit.HOURS));
        assertEquals(12, test.getTime(TimeUnit.MINUTES));
        assertEquals(13, test.getTime(TimeUnit.SECONDS));
        assertEquals(5, test.getTime(TimeUnit.MILLISECONDS));
        assertEquals(6, test.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, test.getTime(TimeUnit.NANOSECONDS));
        test = test.setTime(TimeUnit.MILLISECONDS, 14);
        assertEquals(10, test.getTime(TimeUnit.DAYS));
        assertEquals(11, test.getTime(TimeUnit.HOURS));
        assertEquals(12, test.getTime(TimeUnit.MINUTES));
        assertEquals(13, test.getTime(TimeUnit.SECONDS));
        assertEquals(14, test.getTime(TimeUnit.MILLISECONDS));
        assertEquals(6, test.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, test.getTime(TimeUnit.NANOSECONDS));
        test = test.setTime(TimeUnit.MICROSECONDS, 15);
        assertEquals(10, test.getTime(TimeUnit.DAYS));
        assertEquals(11, test.getTime(TimeUnit.HOURS));
        assertEquals(12, test.getTime(TimeUnit.MINUTES));
        assertEquals(13, test.getTime(TimeUnit.SECONDS));
        assertEquals(14, test.getTime(TimeUnit.MILLISECONDS));
        assertEquals(15, test.getTime(TimeUnit.MICROSECONDS));
        assertEquals(7, test.getTime(TimeUnit.NANOSECONDS));
        test = test.setTime(TimeUnit.NANOSECONDS, 16);
        assertEquals(10, test.getTime(TimeUnit.DAYS));
        assertEquals(11, test.getTime(TimeUnit.HOURS));
        assertEquals(12, test.getTime(TimeUnit.MINUTES));
        assertEquals(13, test.getTime(TimeUnit.SECONDS));
        assertEquals(14, test.getTime(TimeUnit.MILLISECONDS));
        assertEquals(15, test.getTime(TimeUnit.MICROSECONDS));
        assertEquals(16, test.getTime(TimeUnit.NANOSECONDS));
    }

}
