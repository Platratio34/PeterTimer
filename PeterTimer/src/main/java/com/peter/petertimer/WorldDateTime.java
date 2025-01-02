package com.peter.petertimer;

import org.bukkit.World;

public class WorldDateTime {

    /**
     * Converter for a period of world time to real world time.
     * Equal to {@code (20 * 60) / (1000 * 24)}
     */
    public static final float WORLD_TO_SECONDS = (20f * 60f) / (1000f * 24f); // 20 minutes per 24 hours of 1000 quantums

    protected long ticks = 0;

    protected long day = 0;
    protected long hour = 0;

    /** World the time is represented in */
    public final World world;

    protected WorldDateTime(World world) {
        this.world = world;
    }

    /** Create a new WorldDateTime for the current time */
    public static WorldDateTime current(World world) {
        return new WorldDateTime(world).setTicks(world.getFullTime());
    }

    /** Create a new WorldDateTime for the specified time */
    public static WorldDateTime of(World world, long time) {
        return new WorldDateTime(world).setTicks(time);
    }

    /**
     * Set the tick time
     * @param worldTime Ticks since world start
     * @return This
     */
    public WorldDateTime setTicks(long worldTime) {
        ticks = worldTime;
        hour = ticks / 1000;
        day = hour / 24;
        hour %= 24;
        return this;
    }

    /**
     * Set the hour time
     * @param hour Hours [0-24)
     * @return This
     */
    public WorldDateTime setHour(long hour) {
        this.hour = hour;
        resetTime();
        return this;
    }

    /**
     * Set the day component
     * @param day Days since world start
     * @return This
     */
    public WorldDateTime setDay(long day) {
        this.day = day;
        resetTime();
        return this;
    }

    protected void resetTime() {
        ticks = (day * 24) + hour;

        hour = ticks;
        day = hour / 24;
        hour %= 24;

        ticks *= 1000;
    }

    /**
     * Get the number of days since the world started
     * @return Days
     */
    public long getDay() {
        return day;
    }

    /**
     * Get the number hours since the day started
     * @return Hours
     */
    public long getHour() {
        return hour;
    }

    /**
     * Get the number of ticks since the world started
     * @return Ticks
     */
    public long getTicks() {
        return ticks;
    }

    /**
     * Add days to the current time
     * @param days Days to add
     * @return This
     */
    public WorldDateTime addDays(long days) {
        day += days;
        resetTime();
        return this;
    }

    /**
     * Add hours to the current time
     * @param hours Hours to add
     * @return This
     */
    public WorldDateTime addHours(long hours) {
        hour += hours;
        resetTime();
        return this;
    }

    /**
     * Add ticks to the current time
     * @param ticks Ticks to add
     * @return This
     */
    public WorldDateTime addTicks(long ticks) {
        setTicks(this.ticks + ticks);
        return this;
    }

    /**
     * Get the number of ticks until this time
     * @return Ticks till the time represented by this object
     */
    public long ticksTill() {
        return ticksTill(world.getFullTime());
    }
    /**
     * Get the number of ticks until this time
     * @param currentWorldTime Current world time in ticks
     * @return Ticks till the time represented by this object
     */
    public long ticksTill(long currentWorldTime) {
        return currentWorldTime - ticks;
    }

    /**
     * Get the nubmer of seconds (rounded to the nearest tick) until this time
     * @return Seconds till the time represeted by this object
     */
    public float secondsTill() {
        return WORLD_TO_SECONDS * ticksTill();
    }
    /**
     * Get the nubmer of seconds (rounded to the nearest tick) until this time
     * @param currentWorldTime Current world time in ticks
     * @return Seconds till the time represeted by this object
     */
    public float secondsTill(long currentWorldTime) {
        return WORLD_TO_SECONDS * ticksTill(currentWorldTime);
    }

    @Override
    public String toString() {
        return String.format("WorldDateTime{date=%d,hour=%d}", day, hour);
    }

    /**
     * Get the time represted by this object as a formatted string
     * @return Time formateed as DD:HH
     */
    public String formattedTime() {
        return String.format("%02d:%02d", day, hour);
    }

    /**
     * Create a copy of this time
     * @return Copy of This
     */
    public WorldDateTime copy() {
        return new WorldDateTime(world).setTicks(ticks);
    }
}
