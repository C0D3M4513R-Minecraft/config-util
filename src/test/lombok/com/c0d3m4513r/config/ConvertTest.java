package com.c0d3m4513r.config;

import com.c0d3m4513r.PurePredicate;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ConvertTest {
    @DisplayName("Test Convert.timeUnit#get(String)")
    @Test
    public void testAsTimeUnit(){
        Assertions.assertEquals(TimeUnit.SECONDS, Convert.timeUnit.get("s"));
        assertEquals(TimeUnit.SECONDS, Convert.timeUnit.get("sec"));
        assertEquals(TimeUnit.SECONDS, Convert.timeUnit.get("second"));
        assertEquals(TimeUnit.SECONDS, Convert.timeUnit.get("seconds"));
        assertEquals(TimeUnit.MINUTES, Convert.timeUnit.get("m"));
        assertEquals(TimeUnit.MINUTES, Convert.timeUnit.get("min"));
        assertEquals(TimeUnit.MINUTES, Convert.timeUnit.get("minute"));
        assertEquals(TimeUnit.MINUTES, Convert.timeUnit.get("minutes"));
        assertEquals(TimeUnit.HOURS, Convert.timeUnit.get("h"));
        assertEquals(TimeUnit.HOURS, Convert.timeUnit.get("hour"));
        assertEquals(TimeUnit.HOURS, Convert.timeUnit.get("hours"));
        assertEquals(TimeUnit.DAYS, Convert.timeUnit.get("d"));
        assertEquals(TimeUnit.DAYS, Convert.timeUnit.get("day"));
        assertEquals(TimeUnit.DAYS, Convert.timeUnit.get("days"));
    }
    @DisplayName("Test Convert#isLower(TimeUnit, TimeUnit)")
    @Test
    public void testIsLower() {
        //ns
        assertFalse(Convert.isLower(TimeUnit.NANOSECONDS, TimeUnit.NANOSECONDS));
        assertTrue(Convert.isLower(TimeUnit.NANOSECONDS, TimeUnit.MICROSECONDS));
        assertTrue(Convert.isLower(TimeUnit.NANOSECONDS, TimeUnit.MILLISECONDS));
        assertTrue(Convert.isLower(TimeUnit.NANOSECONDS, TimeUnit.SECONDS));
        assertTrue(Convert.isLower(TimeUnit.NANOSECONDS, TimeUnit.MINUTES));
        assertTrue(Convert.isLower(TimeUnit.NANOSECONDS, TimeUnit.HOURS));
        assertTrue(Convert.isLower(TimeUnit.NANOSECONDS, TimeUnit.DAYS));
        //us
        assertFalse(Convert.isLower(TimeUnit.MICROSECONDS, TimeUnit.NANOSECONDS));
        assertFalse(Convert.isLower(TimeUnit.MICROSECONDS, TimeUnit.MICROSECONDS));
        assertTrue(Convert.isLower(TimeUnit.MICROSECONDS, TimeUnit.MILLISECONDS));
        assertTrue(Convert.isLower(TimeUnit.MICROSECONDS, TimeUnit.SECONDS));
        assertTrue(Convert.isLower(TimeUnit.MICROSECONDS, TimeUnit.MINUTES));
        assertTrue(Convert.isLower(TimeUnit.MICROSECONDS, TimeUnit.HOURS));
        assertTrue(Convert.isLower(TimeUnit.MICROSECONDS, TimeUnit.DAYS));
        //ms
        assertFalse(Convert.isLower(TimeUnit.MILLISECONDS, TimeUnit.NANOSECONDS));
        assertFalse(Convert.isLower(TimeUnit.MILLISECONDS, TimeUnit.MICROSECONDS));
        assertFalse(Convert.isLower(TimeUnit.MILLISECONDS, TimeUnit.MILLISECONDS));
        assertTrue(Convert.isLower(TimeUnit.MILLISECONDS, TimeUnit.SECONDS));
        assertTrue(Convert.isLower(TimeUnit.MILLISECONDS, TimeUnit.MINUTES));
        assertTrue(Convert.isLower(TimeUnit.MILLISECONDS, TimeUnit.HOURS));
        assertTrue(Convert.isLower(TimeUnit.MILLISECONDS, TimeUnit.DAYS));
        //seconds
        assertFalse(Convert.isLower(TimeUnit.SECONDS, TimeUnit.NANOSECONDS));
        assertFalse(Convert.isLower(TimeUnit.SECONDS, TimeUnit.MICROSECONDS));
        assertFalse(Convert.isLower(TimeUnit.SECONDS, TimeUnit.MILLISECONDS));
        assertFalse(Convert.isLower(TimeUnit.SECONDS, TimeUnit.SECONDS));
        assertTrue(Convert.isLower(TimeUnit.SECONDS, TimeUnit.MINUTES));
        assertTrue(Convert.isLower(TimeUnit.SECONDS, TimeUnit.HOURS));
        assertTrue(Convert.isLower(TimeUnit.SECONDS, TimeUnit.DAYS));
        //minutes
        assertFalse(Convert.isLower(TimeUnit.MINUTES, TimeUnit.NANOSECONDS));
        assertFalse(Convert.isLower(TimeUnit.MINUTES, TimeUnit.MICROSECONDS));
        assertFalse(Convert.isLower(TimeUnit.MINUTES, TimeUnit.MILLISECONDS));
        assertFalse(Convert.isLower(TimeUnit.MINUTES, TimeUnit.SECONDS));
        assertFalse(Convert.isLower(TimeUnit.MINUTES, TimeUnit.MINUTES));
        assertTrue(Convert.isLower(TimeUnit.MINUTES, TimeUnit.HOURS));
        assertTrue(Convert.isLower(TimeUnit.MINUTES, TimeUnit.DAYS));
        //hours
        assertFalse(Convert.isLower(TimeUnit.HOURS, TimeUnit.NANOSECONDS));
        assertFalse(Convert.isLower(TimeUnit.HOURS, TimeUnit.MICROSECONDS));
        assertFalse(Convert.isLower(TimeUnit.HOURS, TimeUnit.MILLISECONDS));
        assertFalse(Convert.isLower(TimeUnit.HOURS, TimeUnit.SECONDS));
        assertFalse(Convert.isLower(TimeUnit.HOURS, TimeUnit.MINUTES));
        assertFalse(Convert.isLower(TimeUnit.HOURS, TimeUnit.HOURS));
        assertTrue(Convert.isLower(TimeUnit.HOURS, TimeUnit.DAYS));
        //days
        assertFalse(Convert.isLower(TimeUnit.DAYS, TimeUnit.NANOSECONDS));
        assertFalse(Convert.isLower(TimeUnit.DAYS, TimeUnit.MICROSECONDS));
        assertFalse(Convert.isLower(TimeUnit.DAYS, TimeUnit.MILLISECONDS));
        assertFalse(Convert.isLower(TimeUnit.DAYS, TimeUnit.SECONDS));
        assertFalse(Convert.isLower(TimeUnit.DAYS, TimeUnit.MINUTES));
        assertFalse(Convert.isLower(TimeUnit.DAYS, TimeUnit.HOURS));
        assertFalse(Convert.isLower(TimeUnit.DAYS, TimeUnit.DAYS));
    }
    @DisplayName("Test Convert#nextLower(TimeUnit)")
    @Test
    public void testNextLower(){
        assertEquals(TimeUnit.NANOSECONDS, Convert.nextLowerUnit(TimeUnit.NANOSECONDS));
        assertEquals(TimeUnit.NANOSECONDS, Convert.nextLowerUnit(TimeUnit.MICROSECONDS));
        assertEquals(TimeUnit.MICROSECONDS, Convert.nextLowerUnit(TimeUnit.MILLISECONDS));
        assertEquals(TimeUnit.MILLISECONDS, Convert.nextLowerUnit(TimeUnit.SECONDS));
        assertEquals(TimeUnit.SECONDS, Convert.nextLowerUnit(TimeUnit.MINUTES));
        assertEquals(TimeUnit.MINUTES, Convert.nextLowerUnit(TimeUnit.HOURS));
        assertEquals(TimeUnit.HOURS, Convert.nextLowerUnit(TimeUnit.DAYS));
    }

    public static List<Arguments> sourcesNextLowerBounded(){
        val units = TimeUnit.values();
        val list = new ArrayList<Arguments>(units.length * units.length);
        for (val unit : units) {
            for (TimeUnit bound : units) {
                list.add(Arguments.of(unit, bound));
            }
        }
        return list;
    }

    @DisplayName("Test Convert#nextLowerBounded(TimeUnit, TimeUnit)")
    @ParameterizedTest
    @MethodSource("sourcesNextLowerBounded")
    public void testNextLowerBounded(TimeUnit unit, TimeUnit bound){
        val lower = Convert.nextLowerUnit(unit);
        assertEquals(Convert.isLower(lower, bound) ? bound : lower, Convert.nextLowerUnitBounded(unit, bound));
    }

    @DisplayName("Test Convert#getChronoUnit(TimeUnit)")
    @Test
    public void testGetChronoUnit(){
        assertEquals(ChronoUnit.NANOS, Convert.getChronoUnit(TimeUnit.NANOSECONDS));
        assertEquals(ChronoUnit.MICROS, Convert.getChronoUnit(TimeUnit.MICROSECONDS));
        assertEquals(ChronoUnit.MILLIS, Convert.getChronoUnit(TimeUnit.MILLISECONDS));
        assertEquals(ChronoUnit.SECONDS, Convert.getChronoUnit(TimeUnit.SECONDS));
        assertEquals(ChronoUnit.MINUTES, Convert.getChronoUnit(TimeUnit.MINUTES));
        assertEquals(ChronoUnit.HOURS, Convert.getChronoUnit(TimeUnit.HOURS));
        assertEquals(ChronoUnit.DAYS, Convert.getChronoUnit(TimeUnit.DAYS));
    }


    public static Stream<Arguments> sourcesSplitPredicateOffEnd(){
        return Stream.of(
                Arguments.of((PurePredicate<Character>)(c) -> c == 'a', "bbb", "aaaaaaa"),
                Arguments.of((PurePredicate<Character>)Character::isDigit, "bbb", "4321"),
                Arguments.of((PurePredicate<Character>)(c) -> c == 'a', "aaabbb", ""),
                Arguments.of((PurePredicate<Character>)(c) -> c == 'b', "aaa", "bbbb"),
                Arguments.of((PurePredicate<Character>)(ignored) -> true, "", "bbbbaaaa")
        );
    }

    @DisplayName("Test Convert#splitPredicateOffEnd(String, PurePredicate)")
    @ParameterizedTest
    @MethodSource("sourcesSplitPredicateOffEnd")
    public void testSplitPredicateOffEnd(PurePredicate<Character> predicate, String expectedRemainder, String expectedSplit){
        val ret = Convert.splitPredicateOffEnd(expectedRemainder + expectedSplit, predicate);
        assertEquals(expectedRemainder, ret.getLeft());
        assertEquals(expectedSplit, ret.getRight());
    }
}
