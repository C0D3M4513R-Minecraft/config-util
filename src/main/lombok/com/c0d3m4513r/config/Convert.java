package com.c0d3m4513r.config;

import com.c0d3m4513r.PurePredicate;
import com.c0d3m4513r.Tuple;
import com.c0d3m4513r.logger.Logging;
import org.checkerframework.checker.index.qual.LTEqLengthOf;
import org.checkerframework.checker.index.qual.LengthOf;
import org.checkerframework.checker.index.qual.LessThan;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.KeyFor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class Convert {
    /**
     * Maps possible strings for time units to their respective {@link TimeUnit} values.
     * This map is immutable. Any attempt to modify it will result in an {@link UnsupportedOperationException}.
     */
    public static final @NonNull Map<@KeyFor("timeUnit") @NonNull String, @NonNull TimeUnit> timeUnit;
    /**
     * The entries of the {@link Convert#timeUnit} Map.
     * This set is immutable. Any attempt to modify it will result in an {@link UnsupportedOperationException}.
     * The contained Map Entries are also immutable. Any attempt to modify them will result in an {@link UnsupportedOperationException}.
     */
    public static final @NonNull Set<Map.Entry<@NonNull @KeyFor("timeUnit") String, @NonNull TimeUnit>> timeUnitEntries;
    /**
     * The maximum length of a key in the {@link Convert#timeUnitEntries} and {@link Convert#timeUnit} collections.
     */
    public static final long maxUnitLength;

    static {
        Map<@NonNull String, @NonNull TimeUnit> immediateMap = new HashMap<>();
        immediateMap.put("s",TimeUnit.SECONDS);
        immediateMap.put("sec",TimeUnit.SECONDS);
        immediateMap.put("second",TimeUnit.SECONDS);
        immediateMap.put("seconds",TimeUnit.SECONDS);
        immediateMap.put("m",TimeUnit.MINUTES);
        immediateMap.put("min",TimeUnit.MINUTES);
        immediateMap.put("minute",TimeUnit.MINUTES);
        immediateMap.put("minutes",TimeUnit.MINUTES);
        immediateMap.put("h",TimeUnit.HOURS);
        immediateMap.put("hour",TimeUnit.HOURS);
        immediateMap.put("hours",TimeUnit.HOURS);
        immediateMap.put("d",TimeUnit.DAYS);
        immediateMap.put("day",TimeUnit.DAYS);
        immediateMap.put("days",TimeUnit.DAYS);
        timeUnit = Collections.unmodifiableMap(immediateMap);
        timeUnitEntries = Collections.unmodifiableSet(timeUnit.entrySet());
        maxUnitLength = timeUnit.keySet().parallelStream().mapToInt(String::length).max().orElse(0);
    }

    /**
     * @param unit the unit to convert
     * @param bound the lower bound, below which the unit will not be converted
     * @return next lower TimeUnit from unit, but with lower bound of bound
     */
    @Pure
    public static @NonNull TimeUnit nextLowerUnitBounded(TimeUnit unit, TimeUnit bound){
        final TimeUnit lower = nextLowerUnit(unit);
        return isLower(bound,lower)?lower:bound;
    }

    /**
     * Gets the next lower unit (if exists, or the given unit), of the given unit
     * @param unit the unit to get the next lower unit of
     * @return the next lower unit if exists, or the given unit
     */
    @Pure
    public static @NonNull TimeUnit nextLowerUnit(TimeUnit unit){
        switch (unit){
            case NANOSECONDS:
            case MICROSECONDS:return TimeUnit.NANOSECONDS;
            case MILLISECONDS:return TimeUnit.MICROSECONDS;
            case SECONDS:return TimeUnit.MILLISECONDS;
            case MINUTES:return TimeUnit.SECONDS;
            case HOURS:return TimeUnit.MINUTES;
            case DAYS:return TimeUnit.HOURS;
            default:return unit;
        }
    }

    /**
     *
     * @param u1 the first unit
     * @param u2 the second unit
     * @return is u1 lower than u2
     */
    @Pure
    public static boolean isLower(TimeUnit u1, TimeUnit u2){
        switch (u1){
            case NANOSECONDS:
                return u2 != TimeUnit.NANOSECONDS;
            case MICROSECONDS:
                switch (u2){
                    case NANOSECONDS:
                    case MICROSECONDS:
                        return false;
                    default:
                        return true;
                }
            case MILLISECONDS:
                switch (u2){
                    case NANOSECONDS:
                    case MICROSECONDS:
                    case MILLISECONDS:
                        return false;
                    default:
                        return true;
                }
            case SECONDS:
                switch (u2){
                    case NANOSECONDS:
                    case MICROSECONDS:
                    case MILLISECONDS:
                    case SECONDS:
                        return false;
                    default:
                        return true;
                }
            case MINUTES:
                switch (u2){
                    case NANOSECONDS:
                    case MICROSECONDS:
                    case MILLISECONDS:
                    case SECONDS:
                    case MINUTES:
                        return false;
                    default:
                        return true;
                }
            case HOURS:
                switch (u2){
                    case NANOSECONDS:
                    case MICROSECONDS:
                    case MILLISECONDS:
                    case SECONDS:
                    case MINUTES:
                    case HOURS:
                        return false;
                    default:
                        return true;
                }
            case DAYS:
                switch (u2){
                    case NANOSECONDS:
                    case MICROSECONDS:
                    case MILLISECONDS:
                    case SECONDS:
                    case MINUTES:
                    case HOURS:
                    case DAYS:
                        return false;
                    default:
                        return true;
                }
            default:
                return false;
        }
    }

    /**
     * Converts a TimeUnit to a ChronoUnit.
     * @param unit The TimeUnit to convert.
     * @return The ChronoUnit that is equivalent to the given TimeUnit.
     */
    @Pure
    public static ChronoUnit getChronoUnit(TimeUnit unit){
        switch (unit){
            case DAYS: return ChronoUnit.DAYS;
            case HOURS: return ChronoUnit.HOURS;
            case MINUTES: return ChronoUnit.MINUTES;
            case MILLISECONDS: return ChronoUnit.MILLIS;
            case MICROSECONDS: return ChronoUnit.MICROS;
            case NANOSECONDS: return ChronoUnit.NANOS;
            case SECONDS:
            default: return ChronoUnit.SECONDS;
        }
    }

    /**
     * Splits characters matching the predicate off the end of the string.
     * Stops once one character is not matched to the predicate.
     * Specifically this implementation guarantees the following things:
     * - The predicate will only be called, if the given string is not empty.
     * - The predicate will only be called once for each character.
     * - The predicate will be called in order from the end of the string to the beginning.
     * - once the predicate returns false, it will not be called again.
     * @param parse The string to parse.
     * @param predicate The predicate to test the characters with. It should be a pure function.
     * @return A tuple containing the remaining string and the non-digit characters (in that order).
     */
    @SideEffectFree
    public static @NonNull Tuple<@NonNull String, @NonNull String> splitPredicateOffEnd(final String parse, final PurePredicate<Character> predicate){
        @LengthOf("parse")
        final int length = parse.length();
        @LessThan("length + 1")
        @LTEqLengthOf("parse")
        @NonNegative
        int unitLength = 0;
        Logging.INSTANCE.debug("[Conf] Splitting '{}' according to the predicate", parse);
        //Greedily gets the non-matching characters at the end of the string.
        while(length > unitLength){
            final char character = parse.charAt(length - unitLength - 1);
            if (predicate.test(character)){
                //because checkers get this, but not unitLength++...
                //@checkerframework.jankness
                unitLength += 1;
            }else{
                break;
            }
        }
        @LTEqLengthOf("parse")
        @NonNegative
        final int restLength = (length - unitLength);
        Logging.INSTANCE.debug("[Conf] Splitting '{}' at index '{}'", parse, restLength);
        if (restLength == 0) return new Tuple<>("", parse);
        return new Tuple<>(parse.substring(0, restLength), parse.substring(restLength));
    }
}
