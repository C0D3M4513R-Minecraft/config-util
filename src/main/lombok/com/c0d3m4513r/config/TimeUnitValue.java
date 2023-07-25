package com.c0d3m4513r.config;

import com.c0d3m4513r.Tuple;
import com.c0d3m4513r.logger.Logging;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import lombok.val;
import org.checkerframework.checker.index.qual.LengthOf;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.units.qual.Time;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;

import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Value
@AllArgsConstructor(onConstructor_ = {@SideEffectFree})
@Time
@Getter(onMethod_ = {@Pure})
public class TimeUnitValue implements Comparable<TimeUnitValue> {
    @lombok.NonNull TimeUnit unit;
    long value;

    /**
     * Parses one TimeUnitValue from the end of the string.
     * @param parse The string to parse.
     * @return A tuple containing the parsed TimeUnitValue and the remaining string.
     */
    @SideEffectFree
    static @NonNull Tuple<@NonNull String, @Nullable TimeUnitValue> ofInternal(@NonNull String parse){
        @Nullable
        TimeUnit u;
        @MonotonicNonNull
        Long numberValue = null;
        @NonNull
        @Time final String value;
        boolean negative = false;
        @NonNull
        final String unit;
        Logging.INSTANCE.debug("[Conf] Parsing string '{}'",parse);
        {
            val splitNonDigit = Convert.splitPredicateOffEnd(parse, c->!Character.isDigit(c));
            unit = splitNonDigit.getRight();
            val splitDigit = Convert.splitPredicateOffEnd(splitNonDigit.getLeft(), Character::isDigit);
            value = splitDigit.getRight();
            parse = splitDigit.getLeft();
            @LengthOf("parse")
            final int length = parse.length();
            if (length > 0){
                final char possiblePlusOrMinus = parse.charAt(length - 1);
                if (possiblePlusOrMinus == '-') {
                    negative = true;
                    parse = parse.substring(0, length - 1);
                }else if (possiblePlusOrMinus == '+'){
                    parse = parse.substring(0, length - 1);
                }
            }
            Logging.INSTANCE.debug("[Conf] Split off the following Strings for the unit '{}' and number '{}'",unit, value);
        }
        u = Convert.timeUnit.get(unit);
        Logging.INSTANCE.trace("[Conf] Now trying to parse the number before that last unit.");
        try{
            numberValue = Long.parseLong(value);
            if (negative){
                numberValue = -numberValue;
            }
        }catch (NumberFormatException e){
            Logging.INSTANCE.error("[Conf] Could not parse the number string as a number", e);
        }
        if (u == null){
            Logging.INSTANCE.error("[Conf] Could not parse the unit string as a TimeUnit");
            return new Tuple<>(parse, null);
        } else if (numberValue == null) {
            Logging.INSTANCE.error("[Conf] Could not parse the number string as a number");
            return new Tuple<>(parse, null);
        }
        Logging.INSTANCE.debug("[Conf] Found unit '{}' and number '{}'",u.name(), numberValue);
        return new Tuple<>(parse, new TimeUnitValue(u,numberValue));
    }

    @SideEffectFree
    public static @Nullable TimeUnitValue of(String parse){
        parse = parse.trim().replaceAll("[+,]", "");
        final @NonNull Tuple<@NonNull String, @Nullable TimeUnitValue> tuple = ofInternal(parse);
        if (!tuple.getLeft().isEmpty()){
            Logging.INSTANCE.error("[Conf] More of the input could be parsed '{}', but that wasn't requested, so it is being ignored.",parse);
        }
        return tuple.getRight();
    }

    /**
     * Parses a unit and a value to a TimeEntry.
     * The valid units are looked up in {@link Convert#timeUnit}.
     * The value is parsed as a long.
     * @param unit The unit to parse
     * @param value The value to parse
     * @return Returns a TimeEntry with the given unit and value, or null if the unit or value is invalid.
     */
    @SideEffectFree
    public static @Nullable TimeUnitValue of(@NonNull final String unit, @NonNull final String value){
        long longValue;
        try {
            longValue = Long.parseLong(value);
        }catch (NumberFormatException e){
            Logging.INSTANCE.error("[Conf] Could not parse the number string '"+value+"' as a number: ", e);
            return null;
        }
        final TimeUnit unitValue = Convert.timeUnit.get(unit);
        if (unitValue == null) {
            Logging.INSTANCE.error("[Conf] Could not parse the unit string '{}' as a unit.", unit);
            return null;
        }
        return new TimeUnitValue(unitValue, longValue);
    }

    /**
     * Parses a list of TimeUnitValues from the given string.
     * If parsing a single value fails, the whole parsing fails.
     * @param parse The string to parse.
     * @return a list of parsed TimeUnitValues contained in this string in an immutable list
     */
    @SideEffectFree
    @SuppressWarnings("purity.not.sideeffectfree.call")
    public static @NonNull List<TimeUnitValue> ofList(@NonNull String parse){
        parse = parse.trim().replaceAll("[+,]", "");
        Logging.INSTANCE.debug("[Conf] Parsing string '{}'",parse);
        @NonNull
        final List<TimeUnitValue> result = new LinkedList<>();
        while(!parse.isEmpty()){
            Tuple<@NonNull String, @Nullable TimeUnitValue> tuple = ofInternal(parse);
            parse = tuple.getLeft();
            val value = tuple.getRight();
            if (value != null){
                result.add(value);
            }else{
                Logging.INSTANCE.error("[Conf] Could not parse part of the following string '{}'",parse);
                result.clear();
                return Collections.unmodifiableList(result);
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Pure
    public ChronoUnit getChronoUnit(){
        return Convert.getChronoUnit(unit);
    }

    @SideEffectFree
    public BigInteger toNanos(){
        return BigInteger.valueOf(value).multiply(BigInteger.valueOf(unit.toNanos(1)));
    }

    @Pure
    @SuppressWarnings("purity.not.deterministic.call")
    @Override
    public int compareTo(TimeUnitValue o) {
        if (o.unit == unit)
            return Long.compare(value,o.value);
        return toNanos().compareTo(o.toNanos());
    }

    /**
     * Compares this TimeUnitValue to another TimeUnitValue.
     * If comparing this to a TimeEntry, it should be normalised first.
     * @param obj The object to compare to.
     * @return true if the objects are equal, false otherwise.
     */
    @Pure
    @Override
    public boolean equals(@Nullable Object obj){
        if (obj instanceof TimeUnitValue){
            TimeUnitValue tuv = (TimeUnitValue) obj;
            return compareTo(tuv)==0;
        }
        return false;
    }

    @SideEffectFree
    @SuppressWarnings("purity.not.deterministic.call")
    @Override
    public int hashCode(){
        return toNanos().hashCode();
    }
}
