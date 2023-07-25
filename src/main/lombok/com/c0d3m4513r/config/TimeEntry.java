package com.c0d3m4513r.config;

import lombok.*;
import org.checkerframework.checker.calledmethods.qual.CalledMethods;
import org.checkerframework.checker.calledmethods.qual.EnsuresCalledMethods;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.units.qual.*;
import org.checkerframework.common.value.qual.ArrayLen;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Data
@Setter(AccessLevel.NONE)
@Getter(value = AccessLevel.PUBLIC, onMethod_ = @Pure)
@NoArgsConstructor(onConstructor_ = {@SideEffectFree})
@AllArgsConstructor(onConstructor_ = {@SideEffectFree, @SuppressWarnings("purity.not.sideeffectfree.assign.field")})
@Builder
@Time
//delombok messes with the order of annotations, so we have to suppress this warning
@SuppressWarnings("type.anno.before.modifier")
public class TimeEntry implements Comparable<TimeEntry> {
    @Builder.Default
    private @Time long days = 0;
    @Builder.Default
    private @h long hours = 0;
    @Builder.Default
    private @min long minutes = 0;
    @Builder.Default
    private @s long seconds = 0;
    @Builder.Default
    private @s(Prefix.milli) long ms = 0;
    @Builder.Default
    private @s(Prefix.micro) long us = 0;
    @Builder.Default
    private @s(Prefix.nano) long ns = 0;

    /**
     * Copy constructor. Copies all fields from the given TimeEntry
     * @param entry The TimeEntry to copy
     */
    @SideEffectFree
    @SuppressWarnings("purity.not.sideeffectfree.assign.field")
    public TimeEntry(@org.checkerframework.checker.nullness.qual.NonNull final TimeEntry entry){
        this.days = entry.days;
        this.hours = entry.hours;
        this.minutes = entry.minutes;
        this.seconds = entry.seconds;
        this.ms = entry.ms;
        this.us = entry.us;
        this.ns = entry.ns;
    }
    @SideEffectFree
    @SuppressWarnings("purity.not.sideeffectfree.assign.field")
    public TimeEntry(final @NonNull TimeUnit unit, final long value, @org.checkerframework.checker.nullness.qual.NonNull final TimeEntry timeEntry){
        this(timeEntry);
        if (unit == TimeUnit.NANOSECONDS) {
            ns = value;
        } else if (unit == TimeUnit.MICROSECONDS) {
            us = value;
        } else if (unit == TimeUnit.MILLISECONDS) {
            ms = value;
        } else if (unit == TimeUnit.SECONDS) {
            seconds = value;
        } else if (unit == TimeUnit.MINUTES) {
            minutes = value;
        } else if (unit == TimeUnit.HOURS) {
            hours = value;
        } else if (unit == TimeUnit.DAYS) {
            days = value;
        } else {
            throw new IllegalArgumentException("Too many enum variants");
        }
    }

    /**
     * @param unit The TimeUnit to get the value of
     * @return Returns the time amount of the given unit
     */
    @Pure
    public long getTime(@NonNull TimeUnit unit){
        if (unit == TimeUnit.NANOSECONDS) {
            return ns;
        } else if (unit == TimeUnit.MICROSECONDS) {
            return us;
        } else if (unit == TimeUnit.MILLISECONDS) {
            return ms;
        } else if (unit == TimeUnit.SECONDS) {
            return seconds;
        } else if (unit == TimeUnit.MINUTES) {
            return minutes;
        } else if (unit == TimeUnit.HOURS) {
            return hours;
        } else if (unit == TimeUnit.DAYS) {
            return days;
        }
        throw new RuntimeException("Too many enum variants");
    }

    /**
     * Sets the time amount of the given unit to the given value
     * @param unit The TimeUnit to set the value of
     * @param value The value to set the time to
     * @return Returns a new TimeEntry with the given value changed to the supplied value
     */
    @SideEffectFree
    public TimeEntry setTime(TimeUnit unit, long value){
        return new TimeEntry(unit, value, this);
    }

    /**
     * Adds the given value to the time amount of the given unit
     * @param unit The TimeUnit to add the value to
     * @param value The value to add to the time amount
     * @return Returns a new TimeEntry with the given value added to the supplied unit
     */
    @SideEffectFree
    public TimeEntry addTime(TimeUnit unit, long value){
        return new TimeEntry(unit, value + getTime(unit), this);
    }

    /**
     * Parses a string in the format of "TimeUnitValue[+TimeUnitValue]*" into a TimeEntry.
     * Where TimeUnitValue is of the format "long[,]?TimeUnit".
     * And TimeUnit can be looked up in {@link Convert#timeUnit} or {@link Convert#timeUnitEntries}
     * @param parse The String to parse
     * @return Returns a TimeEntry with the parsed values
     */
    @SideEffectFree
    @Nullable
    public static TimeEntry of(@NonNull String parse){
        final List<TimeUnitValue> times = TimeUnitValue.ofList(parse);
        return of(times);
    }

    @SideEffectFree
    public static @Nullable TimeEntry of(@org.checkerframework.checker.nullness.qual.NonNull TimeUnitValue... times){
        return TimeEntry.of(Arrays.asList(times));
    }

    /**
     * Parses any amount of TimeUnitValues into a TimeEntry.
     * @param times The TimeUnitValues to parse
     * @return Returns a TimeEntry with the parsed values
     */
    @SideEffectFree
    public static @Nullable TimeEntry of(@org.checkerframework.checker.nullness.qual.NonNull Iterable<TimeUnitValue> times){
        @Time long days = 0;
        @h long hours = 0;
        @m long minutes = 0;
        @s long seconds = 0;
        @s(Prefix.milli) long ms = 0;
        @s(Prefix.micro) long us = 0;
        @s(Prefix.nano) long ns = 0;
        for (val parsed:times) {
            if (parsed == null) return null;
            final TimeUnit unit = parsed.getUnit();
            if (unit == TimeUnit.DAYS) {
                days += parsed.getValue();
                continue;
            } else if (unit == TimeUnit.HOURS) {
                hours += parsed.getValue();
                continue;
            } else if (unit == TimeUnit.MINUTES) {
                minutes += parsed.getValue();
                continue;
            } else if (unit == TimeUnit.SECONDS) {
                seconds += parsed.getValue();
                continue;
            } else if (unit == TimeUnit.MILLISECONDS) {
                ms += parsed.getValue();
                continue;
            } else if (unit == TimeUnit.MICROSECONDS) {
                us += parsed.getValue();
                continue;
            } else if (unit == TimeUnit.NANOSECONDS) {
                ns += parsed.getValue();
            }
        }
        return new TimeEntry(days, hours, minutes, seconds, ms, us, ns);
    }

    /**
     * Creates a new TimeEntry from the given TimeUnitValue
     * @param tuv The TimeUnitValue to create the TimeEntry from
     * @return Returns a new TimeEntry with the given TimeUnitValue
     */
    @SideEffectFree
    public static @NonNull TimeEntry of(@NonNull TimeUnitValue tuv){
        final TimeUnit unit = tuv.getUnit();
        if (unit == TimeUnit.DAYS) {
            return new TimeEntry(tuv.getValue(), 0, 0, 0, 0, 0, 0);
        } else if (unit == TimeUnit.HOURS) {
            return new TimeEntry(0, tuv.getValue(), 0, 0, 0, 0, 0);
        } else if (unit == TimeUnit.MINUTES) {
            return new TimeEntry(0, 0, tuv.getValue(), 0, 0, 0, 0);
        } else if (unit == TimeUnit.SECONDS) {
            return new TimeEntry(0, 0, 0, tuv.getValue(), 0, 0, 0);
        } else if (unit == TimeUnit.MILLISECONDS) {
            return new TimeEntry(0, 0, 0, 0, tuv.getValue(), 0, 0);
        } else if (unit == TimeUnit.MICROSECONDS) {
            return new TimeEntry(0, 0, 0, 0, 0, tuv.getValue(), 0);
        } else if (unit == TimeUnit.NANOSECONDS) {
            return new TimeEntry(0, 0, 0, 0, 0, 0, tuv.getValue());
        }
        throw new IllegalArgumentException("Too many enum variants");
    }

    @SideEffectFree
    //@checkerframework.jankness
    //todo: apparently the index checker doesn't check, that `@ArrayLen(2)` is a stricter requirement than `@MinLen(2)`
    @SuppressWarnings("array.access.unsafe.high.constant")
    public @org.checkerframework.checker.nullness.qual.NonNull TimeEntry of(@org.checkerframework.checker.nullness.qual.NonNull @s(Prefix.nano) BigInteger bigIntNs){
        @ArrayLen(2)
//        @ArrayLenRange(from = 2, to = 2)
        //these two annotations are apparently "conflicting" with each other.
        //I'm confused by the index checker...
        BigInteger[] divAndRem = bigIntNs.divideAndRemainder(BigInteger.valueOf(1000)); //ns -> us; ns
        long ns = divAndRem[1].longValue();
        divAndRem = divAndRem[0].divideAndRemainder(BigInteger.valueOf(1000)); //us -> ms; us
        long us = divAndRem[1].longValue();
        divAndRem = divAndRem[0].divideAndRemainder(BigInteger.valueOf(1000));//ms -> s; ms
        long ms = divAndRem[1].longValue();
        divAndRem = divAndRem[0].divideAndRemainder(BigInteger.valueOf(1000));//s -> min; s
        long seconds = divAndRem[1].longValue();
        divAndRem = divAndRem[0].divideAndRemainder(BigInteger.valueOf(60)); //min -> h; min
        long minutes = divAndRem[1].longValue();
        divAndRem = divAndRem[0].divideAndRemainder(BigInteger.valueOf(24)); //hours -> day; hours
        long hours = divAndRem[1].longValue();
        long days = divAndRem[0].longValue();
        return new TimeEntry(days, hours, minutes, seconds, ms, us, ns);
    }

    /**
     * Adds the given time entry to this one
     * @param te The time entry to add
     * @return Returns a new TimeEntry with the result
     */
    @SideEffectFree
    public TimeEntry add(TimeEntry te){
        return of(getNanosBig().add(te.getNanosBig()));
    }

    /**
     * Subtracts the given time entry from this one
     * @param te The time entry to subtract
     * @return Returns a new TimeEntry with the result
     */
    @SideEffectFree
    @SuppressWarnings("unused")
    public TimeEntry sub(TimeEntry te){
        return of(getNanosBig().subtract(te.getNanosBig()));
    }

    @SideEffectFree
    @EnsuresCalledMethods(value = "this", methods = {"getMicrosBig", "getMillisBig", "getSecondsBig", "getMinutesBig", "getHoursBig", "getDaysBig"})
    @org.checkerframework.checker.nullness.qual.NonNull
    private BigInteger getNanosBig(){
        return BigInteger.valueOf(ns).add(getMicrosBig().multiply(BigInteger.valueOf(1000)));
    }

    @SideEffectFree
    @EnsuresCalledMethods(value = "this", methods = {"getMillisBig", "getSecondsBig", "getMinutesBig", "getHoursBig", "getDaysBig"})
    @org.checkerframework.checker.nullness.qual.NonNull
    private BigInteger getMicrosBig() {
        return BigInteger.valueOf(us).add(getMillisBig().multiply(BigInteger.valueOf(1000)));
    }

    @SideEffectFree
    @EnsuresCalledMethods(value = "this", methods = {"getSecondsBig", "getMinutesBig", "getHoursBig", "getDaysBig"})
    @org.checkerframework.checker.nullness.qual.NonNull
    private BigInteger getMillisBig() {
        return BigInteger.valueOf(ms).add(getSecondsBig().multiply(BigInteger.valueOf(1000)));
    }

    @SideEffectFree
    @EnsuresCalledMethods(value = "this", methods = {"getMinutesBig", "getHoursBig", "getDaysBig"})
    @org.checkerframework.checker.nullness.qual.NonNull
    private BigInteger getSecondsBig() {
        return BigInteger.valueOf(seconds).add(getMinutesBig().multiply(BigInteger.valueOf(60)));
    }

    @SideEffectFree
    @EnsuresCalledMethods(value = "this", methods = {"getHoursBig", "getDaysBig"})
    @org.checkerframework.checker.nullness.qual.NonNull
    private BigInteger getMinutesBig() {
        return BigInteger.valueOf(minutes).add(getHoursBig().multiply(BigInteger.valueOf(60)));
    }

    @SideEffectFree
    @EnsuresCalledMethods(value = "this", methods = {"getDaysBig"})
    @org.checkerframework.checker.nullness.qual.NonNull
    private BigInteger getHoursBig() {
        return BigInteger.valueOf(hours).abs().add(getDaysBig().multiply(BigInteger.valueOf(24)));
    }

    @SideEffectFree
    @org.checkerframework.checker.nullness.qual.NonNull
    private BigInteger getDaysBig() {
        return BigInteger.valueOf(days);
    }


    @SideEffectFree
    public BigInteger to(TimeUnit unit){
        return getNanosBig().divide(BigInteger.valueOf(TimeUnit.NANOSECONDS.convert(1, unit)));
    }

    @Pure
    @Override
    public String toString(){
        String output = "";
        if (days != 0) output += days + "d";
        if (hours != 0) output += hours + "h";
        if (minutes != 0) output += minutes + "m";
        if (seconds != 0) output += seconds + "s";
        if (ms != 0) output += ms + "ms";
        if (us != 0) output += us + "us";
        if (ns != 0) output += ns + "ns";
        return output;
    }


    @Pure
    //the to(TimeUnit) method is not Deterministic, because it creates a new object.
    //For the return value of this class that doesn't matter though, as long as comparing two BigIntegers is deterministic (which I assume it to be)
    @SuppressWarnings("purity.not.deterministic.call")
    @Override
    public int compareTo(@NonNull TimeEntry o) {
        return to(TimeUnit.NANOSECONDS).compareTo(o.to(TimeUnit.NANOSECONDS));
    }

    @Pure
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof TimeEntry) {
            return compareTo((TimeEntry) obj) == 0;
        }
        return false;
    }

    /**
     * Compares if one TimeEntry is equal to another.
     * Should only be called if both this, and obj are normalized.
     * @param obj The other TimeEntry
     * @return if obj and this have the same value.
     */
    @Pure
    @SuppressWarnings("unused")
    public boolean equals(@CalledMethods("normalize") TimeEntry this, @CalledMethods("normalize") final TimeEntry obj) {
        return ns == obj.ns &&
                us == obj.us &&
                ms == obj.ms &&
                seconds == obj.seconds &&
                minutes == obj.minutes &&
                hours == obj.hours &&
                days == obj.days;
    }
    @Pure
    //The to(TimeUnit) method is not Deterministic, because it creates a new object.
    //But that doesn't affect this method, because I assume that creating a hash of a BigInteger is so deterministic,
    //that it only relies on its value.
    @SuppressWarnings("purity.not.deterministic.call")
    @Override
    public int hashCode(){
        return to(TimeUnit.NANOSECONDS).hashCode();
    }
}
