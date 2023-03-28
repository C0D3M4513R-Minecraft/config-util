package com.c0d3m4513r.config;

import com.c0d3m4513r.LoggerUtils;
import com.c0d3m4513r.logger.Logging;
import lombok.val;
import lombok.var;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TimeUnitValueTest {
    @BeforeAll
    public static void init(){
        LoggerUtils.initLogger("TimeUnitValueTest");
    }
    public static final @NonNull Long[] timeValues = {1L, 2L, 3L, 4L, 5L, 10L, 15L, 20L, 30L, 60L };
    public static final @NonNull List<ImmutableTriple<String, Long, TimeUnit>> timeUnitValues;
    public static final long absValue;
    static{
        ArrayList<ImmutableTriple<String, Long, TimeUnit>> array = new ArrayList<>(Convert.timeUnitEntries.size() * timeValues.length);
        long absValue1 = 0;
        for (val entry : Convert.timeUnitEntries) {
            for (Long value : timeValues) {
                absValue1 += value;
                array.add(new ImmutableTriple<>(entry.getKey(), value, entry.getValue()));
                array.add(new ImmutableTriple<>(entry.getKey(), -value, entry.getValue()));
            }
        }
        timeUnitValues = Collections.unmodifiableList(array);
        absValue = absValue1;
    }
    @Pure
    public static @NonNull List<ImmutableTriple<String, Long, TimeUnit>> singleValues(){
        return timeUnitValues;
    }

    @Test
    @DisplayName("Test TimeUnitValue#new(TimeUnit, Long)")
    public void testNew(){
        for (val value : timeValues) {
            for (val unit: TimeUnit.values()) {
                    val tuv = new TimeUnitValue(unit, value);
                    assertNotNull(tuv);
                    assertEquals(unit, tuv.getUnit());
                    assertEquals(value, tuv.getValue());
            }
        }
    }

    @ParameterizedTest
    @MethodSource("singleValues")
    @DisplayName("Test SingleValues TimeUnitValue#ofInternal(String)")
    public void testSingleValueOfInternal(final ImmutableTriple<String, Long, TimeUnit> entry){
        val unitString = entry.getLeft();
        val value = entry.getMiddle();
        val unit = entry.getRight();
        final String parse = value + unitString;
        Logging.INSTANCE.info("Parsing: {}", parse);
        val retValue = TimeUnitValue.ofInternal(parse);
        val timeUnitValue = retValue.getRight();
        assertEquals("", retValue.getLeft(), "Expected remaining string to be empty");
        assertNotNull(timeUnitValue);
        assertEquals(unit, timeUnitValue.getUnit());
        assertEquals(value, timeUnitValue.getValue());
        Logging.INSTANCE.info("Parsed: {} as {}", parse, retValue);
    }

    @Test
    @DisplayName("Test MultipleValues TimeUnitValue#ofInternal(String)")
    public void testMultipleValueOfInternal(){
        final StringBuilder sb = new StringBuilder();
        for (val entry : timeUnitValues) {
            sb.append(entry.getMiddle()).append(entry.getLeft());
        }
        final String parse = sb.toString();
        //constructed string to parse
        Logging.INSTANCE.info("Parsing: {}", parse);
        @NonNull
        String remaining = parse;
        @Nullable
        TimeUnitValue parsed;
        int i = timeUnitValues.size();
        do {
            val retValue = TimeUnitValue.ofInternal(remaining);
            remaining = retValue.getLeft();
            parsed = retValue.getRight();
            if (--i < 0) fail("Parsed more elements than expected.");
            val entry = timeUnitValues.get(i);
            assertNotNull(parsed);
            assertEquals(entry.getRight(), parsed.getUnit());
            assertEquals(entry.getMiddle(), parsed.getValue());
        } while(!remaining.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("singleValues")
    @DisplayName("Test TimeUnitValue#of(String)")
    public void testParseTimeUnitValueSingle(final ImmutableTriple<String, Long, TimeUnit> entry) {
        val unitString = entry.getLeft();
        val value = entry.getMiddle();
        val unit = entry.getRight();
        final String parse = value + unitString;
        Logging.INSTANCE.info("Parsing: {}", parse);
        final TimeUnitValue timeUnitValue = TimeUnitValue.of(parse);
        if(timeUnitValue == null){
            fail("TimeUnitValue is null");
        }
        Logging.INSTANCE.info("Parsed: {} as {}", parse, timeUnitValue);
        assertEquals(unit, timeUnitValue.getUnit());
        assertEquals(value, timeUnitValue.getValue());
    }

    @ParameterizedTest
    @MethodSource("singleValues")
    @DisplayName("Test TimeUnitValue#of(String, String)")
    public void testParseTimeUnitValueSeperateUnitValue(final ImmutableTriple<String, Long, TimeUnit> entry){
        val value = entry.getMiddle();
        val parsed = TimeUnitValue.of(value.toString(), entry.getLeft());
        assertNotNull(parsed);
        assertEquals(parsed.getValue(), value);
        assertEquals(parsed.getUnit(), entry.getRight());
    }

    @Test
    @DisplayName("Test TimeEntry#ofList(String)")
    public void testParseTimeUnitValueList(){
        final StringBuilder sb = new StringBuilder();
        for (val entry : timeUnitValues) {
            sb.append(entry.getMiddle()).append(entry.getLeft());
        }
        final String parse = sb.toString();
        val parsed = TimeUnitValue.ofList(parse);
        final ListIterator<TimeUnitValue> parsedIterator = parsed.listIterator(parsed.size());
        final Iterator<ImmutableTriple<String, Long, TimeUnit>> expectedIterator = timeUnitValues.iterator();
        int i = 0;
        while(parsedIterator.hasPrevious() && expectedIterator.hasNext()){
            val parsedEntry = parsedIterator.previous();
            val expectedEntry = expectedIterator.next();
            Logging.INSTANCE.info("[test] {} Parsed: {} as {} {}", i++, expectedEntry.getMiddle()+expectedEntry.getLeft(), parsedEntry.getValue(), parsedEntry.getUnit());
            assertEquals(expectedEntry.getRight(), parsedEntry.getUnit());
            assertEquals(expectedEntry.getMiddle(), parsedEntry.getValue());
        }
        if (parsedIterator.hasPrevious()){
            fail("Parsed list has more entries than expected");
        } else if (expectedIterator.hasNext()){
            fail("Parsed list has less entries than expected");
        }
    }

    @Test
    @DisplayName("Test TimeUnitValue#equals(TimeUnitValue)")
    public void testEqualsTimeUnitValue(){
        var timeUnitValue = new TimeUnitValue(TimeUnit.NANOSECONDS, 1);
        var test = new TimeUnitValue(TimeUnit.NANOSECONDS, 1);
        assertEquals(timeUnitValue, test);
        assertEquals(timeUnitValue, timeUnitValue);
        assertEquals(test, test);
        test = new TimeUnitValue(TimeUnit.NANOSECONDS, 2);
        assertEquals(test, test);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MICROSECONDS, 1);
        assertEquals(test, test);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MILLISECONDS, 1);
        assertEquals(test, test);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.SECONDS, 1);
        assertEquals(test, test);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MINUTES, 1);
        assertEquals(test, test);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.HOURS, 1);
        assertEquals(test, test);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.DAYS, 1);
        assertEquals(test, test);
        assertNotEquals(timeUnitValue, test);
        //us
        timeUnitValue = new TimeUnitValue(TimeUnit.MICROSECONDS, 1);
        test = new TimeUnitValue(TimeUnit.MICROSECONDS, 1);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MICROSECONDS, 2);
        assertEquals(test, test);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MILLISECONDS, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.SECONDS, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MINUTES, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.HOURS, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.DAYS, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.NANOSECONDS, 1_000);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        //ms
        timeUnitValue = new TimeUnitValue(TimeUnit.MILLISECONDS, 1);
        test = new TimeUnitValue(TimeUnit.MILLISECONDS, 1);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MILLISECONDS, 2);
        assertEquals(test, test);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.SECONDS, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MINUTES, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.HOURS, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.DAYS, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.NANOSECONDS, 1_000_000);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MICROSECONDS, 1_000);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        //seconds
        timeUnitValue = new TimeUnitValue(TimeUnit.SECONDS, 1);
        test = new TimeUnitValue(TimeUnit.SECONDS, 1);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.SECONDS, 2);
        assertEquals(test, test);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MINUTES, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.HOURS, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.DAYS, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MILLISECONDS, 1_000);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MICROSECONDS, 1_000_000);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.NANOSECONDS, 1_000_000_000);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        //minutes
        timeUnitValue = new TimeUnitValue(TimeUnit.MINUTES, 1);
        test = new TimeUnitValue(TimeUnit.MINUTES, 1);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MINUTES, 2);
        assertEquals(test, test);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.HOURS, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.DAYS, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.SECONDS, 60);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MILLISECONDS, 60_000);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MICROSECONDS, 60_000_000);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.NANOSECONDS, 60_000_000_000L);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        //hours
        timeUnitValue = new TimeUnitValue(TimeUnit.HOURS, 1);
        test = new TimeUnitValue(TimeUnit.HOURS, 1);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.HOURS, 2);
        assertEquals(test, test);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.DAYS, 1);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MINUTES, 60);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.SECONDS, 3_600);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MILLISECONDS, 3_600_000);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MICROSECONDS, 3_600_000_000L);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.NANOSECONDS, 3_600_000_000_000L);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        //days
        timeUnitValue = new TimeUnitValue(TimeUnit.DAYS, 1);
        test = new TimeUnitValue(TimeUnit.DAYS, 1);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.DAYS, 2);
        assertEquals(test, test);
        assertNotEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.HOURS, 24);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MINUTES, 1_440);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.SECONDS, 86_400);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MILLISECONDS, 86_400_000);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.MICROSECONDS, 86_400_000_000L);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
        test = new TimeUnitValue(TimeUnit.NANOSECONDS, 86_400_000_000_000L);
        assertEquals(test, test);
        assertEquals(timeUnitValue, test);
    }

    @SuppressWarnings("EqualsWithItself")
    private static void testLTEQGTCompare(TimeUnitValue timeUnitValue, TimeUnit unit, long expectedValue){
        var test = new TimeUnitValue(unit, expectedValue);
        assertEquals(0, test.compareTo(test));
        assertEquals(0, timeUnitValue.compareTo(test));
        assertEquals(0, test.compareTo(timeUnitValue));
        test = new TimeUnitValue(unit, expectedValue + 1);
        assertEquals(0, test.compareTo(test));
        assertEquals(-1, timeUnitValue.compareTo(test));
        assertEquals(1, test.compareTo(timeUnitValue));
        test = new TimeUnitValue(unit, expectedValue - 1);
        assertEquals(0, test.compareTo(test));
        assertEquals(1, timeUnitValue.compareTo(test));
        assertEquals(-1, test.compareTo(timeUnitValue));
    }

    @SuppressWarnings("EqualsWithItself")
    private static void testLTZCompare(TimeUnitValue timeUnitValue){
        for(val unit : TimeUnit.values()){
            val test = new TimeUnitValue(unit, 0);
            assertEquals(0, test.compareTo(test));
            assertEquals(1, timeUnitValue.compareTo(test));
            assertEquals(-1, test.compareTo(timeUnitValue));
        }
    }

    @SuppressWarnings("EqualsWithItself")
    private static void testCompareTo(TimeUnitValue timeUnitValue, TimeUnitValue test, int tuvCompareToTest){
        assertEquals(0, test.compareTo(test));
        assertEquals(tuvCompareToTest, timeUnitValue.compareTo(test));
        assertEquals(-tuvCompareToTest, test.compareTo(timeUnitValue));
    }
    @Test
    @DisplayName("Test TimeUnitValue#compareTo(TimeUnitValue)")
    @SuppressWarnings("EqualsWithItself")//in prod. code this would be redundant, but I'm testing the compliance of the CompareTo method, to the requirements of the Comparable interface
    public void testCompareToTimeUnitValue(){
        var timeUnitValue = new TimeUnitValue(TimeUnit.NANOSECONDS, 1);
        testLTZCompare(timeUnitValue);
        var test = new TimeUnitValue(TimeUnit.NANOSECONDS, 1);
        assertEquals(0, timeUnitValue.compareTo(timeUnitValue));
        testCompareTo(timeUnitValue, test, 0);
        test = new TimeUnitValue(TimeUnit.NANOSECONDS, 2);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.MICROSECONDS, 1);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.SECONDS, 1);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.MINUTES, 1);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.HOURS, 1);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.DAYS, 1);
        testCompareTo(timeUnitValue, test, -1);
        //us
        timeUnitValue = new TimeUnitValue(TimeUnit.MICROSECONDS, 1);
        testLTZCompare(timeUnitValue);
        test = new TimeUnitValue(TimeUnit.MILLISECONDS, 1);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.SECONDS, 1);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.MINUTES, 1);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.HOURS, 1);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.DAYS, 1);
        testCompareTo(timeUnitValue, test, -1);
        testLTEQGTCompare(timeUnitValue, TimeUnit.NANOSECONDS, 1_000);
        testLTEQGTCompare(timeUnitValue, TimeUnit.MICROSECONDS, 1);
        //ms
        timeUnitValue = new TimeUnitValue(TimeUnit.MILLISECONDS, 1);
        testLTZCompare(timeUnitValue);
        test = new TimeUnitValue(TimeUnit.SECONDS, 1);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.MINUTES, 1);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.HOURS, 1);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.DAYS, 1);
        testCompareTo(timeUnitValue, test, -1);
        testLTEQGTCompare(timeUnitValue, TimeUnit.NANOSECONDS, 1_000_000);
        testLTEQGTCompare(timeUnitValue, TimeUnit.MICROSECONDS, 1_000);
        testLTEQGTCompare(timeUnitValue, TimeUnit.MILLISECONDS, 1);
        //seconds
        timeUnitValue = new TimeUnitValue(TimeUnit.SECONDS, 1);
        testLTZCompare(timeUnitValue);
        test = new TimeUnitValue(TimeUnit.MINUTES, 1);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.HOURS, 1);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.DAYS, 1);
        testCompareTo(timeUnitValue, test, -1);
        testLTEQGTCompare(timeUnitValue, TimeUnit.NANOSECONDS, 1_000_000_000);
        testLTEQGTCompare(timeUnitValue, TimeUnit.MICROSECONDS, 1_000_000);
        testLTEQGTCompare(timeUnitValue, TimeUnit.MILLISECONDS, 1_000);
        testLTEQGTCompare(timeUnitValue, TimeUnit.SECONDS, 1);
        //minutes
        timeUnitValue = new TimeUnitValue(TimeUnit.MINUTES, 1);
        testLTZCompare(timeUnitValue);
        test = new TimeUnitValue(TimeUnit.HOURS, 1);
        testCompareTo(timeUnitValue, test, -1);
        test = new TimeUnitValue(TimeUnit.DAYS, 1);
        testCompareTo(timeUnitValue, test, -1);
        testLTEQGTCompare(timeUnitValue, TimeUnit.NANOSECONDS, 60_000_000_000L);
        testLTEQGTCompare(timeUnitValue, TimeUnit.MICROSECONDS, 60_000_000);
        testLTEQGTCompare(timeUnitValue, TimeUnit.MILLISECONDS, 60_000);
        testLTEQGTCompare(timeUnitValue, TimeUnit.SECONDS, 60);
        testLTEQGTCompare(timeUnitValue, TimeUnit.MINUTES, 1);
        //hours
        timeUnitValue = new TimeUnitValue(TimeUnit.HOURS, 1);
        testLTZCompare(timeUnitValue);
        test = new TimeUnitValue(TimeUnit.DAYS, 1);
        testCompareTo(timeUnitValue, test, -1);
        testLTEQGTCompare(timeUnitValue, TimeUnit.NANOSECONDS, 3_600_000_000_000L);
        testLTEQGTCompare(timeUnitValue, TimeUnit.MICROSECONDS, 3_600_000_000L);
        testLTEQGTCompare(timeUnitValue, TimeUnit.MILLISECONDS, 3_600_000);
        testLTEQGTCompare(timeUnitValue, TimeUnit.SECONDS, 3_600);
        testLTEQGTCompare(timeUnitValue, TimeUnit.MINUTES, 60);
        testLTEQGTCompare(timeUnitValue, TimeUnit.HOURS, 1);
        //days
        timeUnitValue = new TimeUnitValue(TimeUnit.DAYS, 1);
        testLTEQGTCompare(timeUnitValue, TimeUnit.NANOSECONDS, 86_400_000_000_000L);
        testLTEQGTCompare(timeUnitValue, TimeUnit.MICROSECONDS, 86_400_000_000L);
        testLTEQGTCompare(timeUnitValue, TimeUnit.MILLISECONDS, 86_400_000);
        testLTEQGTCompare(timeUnitValue, TimeUnit.SECONDS, 86_400);
        testLTEQGTCompare(timeUnitValue, TimeUnit.MINUTES, 1_440);
        testLTEQGTCompare(timeUnitValue, TimeUnit.HOURS, 24);
        testLTEQGTCompare(timeUnitValue, TimeUnit.DAYS, 1);
    }

    @Test
    @DisplayName("Test TimeUnitValue#getChronoUnit()")
    public void testGetChronoUnit(){
        assertEquals(ChronoUnit.NANOS, new TimeUnitValue(TimeUnit.NANOSECONDS, 0).getChronoUnit());
        assertEquals(ChronoUnit.MICROS, new TimeUnitValue(TimeUnit.MICROSECONDS, 0).getChronoUnit());
        assertEquals(ChronoUnit.MILLIS, new TimeUnitValue(TimeUnit.MILLISECONDS, 0).getChronoUnit());
        assertEquals(ChronoUnit.SECONDS, new TimeUnitValue(TimeUnit.SECONDS, 0).getChronoUnit());
        assertEquals(ChronoUnit.MINUTES, new TimeUnitValue(TimeUnit.MINUTES, 0).getChronoUnit());
        assertEquals(ChronoUnit.HOURS, new TimeUnitValue(TimeUnit.HOURS, 0).getChronoUnit());
        assertEquals(ChronoUnit.DAYS, new TimeUnitValue(TimeUnit.DAYS, 0).getChronoUnit());
    }

    @Test
    @DisplayName("Test TimeUnitValue#toNanos()")
    public void testToNanos(){
        assertEquals(1L, new TimeUnitValue(TimeUnit.NANOSECONDS, 1L).toNanos().longValue());
        assertEquals(1_000L, new TimeUnitValue(TimeUnit.MICROSECONDS, 1L).toNanos().longValue());
        assertEquals(1_000_000L, new TimeUnitValue(TimeUnit.MILLISECONDS, 1L).toNanos().longValue());
        assertEquals(1_000_000_000L, new TimeUnitValue(TimeUnit.SECONDS, 1L).toNanos().longValue());
        assertEquals(60_000_000_000L, new TimeUnitValue(TimeUnit.MINUTES, 1L).toNanos().longValue());
        assertEquals(3_600_000_000_000L, new TimeUnitValue(TimeUnit.HOURS, 1L).toNanos().longValue());
        assertEquals(86_400_000_000_000L, new TimeUnitValue(TimeUnit.DAYS, 1L).toNanos().longValue());
    }
}
