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

    public final World world;

    protected WorldDateTime(World world) {
        this.world = world;
    }

    public static WorldDateTime current(World world) {
        return new WorldDateTime(world).setTicks(world.getFullTime());
    }

    public static WorldDateTime of(World world, long time) {
        return new WorldDateTime(world).setTicks(time);
    }

    public WorldDateTime setTicks(long worldTime) {
        ticks = worldTime;
        hour = ticks / 1000;
        day = hour / 24;
        hour %= 24;
        return this;
    }

    public WorldDateTime setHour(long hour) {
        this.hour = hour;
        resetTime();
        return this;
    }

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

    public long getDay() {
        return day;
    }

    public long getHour() {
        return hour;
    }

    public long getTicks() {
        return ticks;
    }

    public WorldDateTime addDays(long days) {
        day += days;
        resetTime();
        return this;
    }

    public WorldDateTime addHours(long hours) {
        hour += hours;
        resetTime();
        return this;
    }

    public WorldDateTime addTicks(long ticks) {
        setTicks(this.ticks + ticks);
        return this;
    }

    public long ticksTill() {
        return ticksTill(world.getFullTime());
    }

    public long ticksTill(long currentWorldTime) {
        return currentWorldTime - ticks;
    }

    public float secondsTill() {
        return WORLD_TO_SECONDS * ticksTill();
    }

    public float secondsTill(long currentWorldTime) {
        return WORLD_TO_SECONDS * ticksTill(currentWorldTime);
    }

    @Override
    public String toString() {
        return String.format("WorldDateTime{date=%d,hour=%d}", day, hour);
    }

    public String formattedTime() {
        return String.format("%02d:%02d", day, hour);
    }

    public WorldDateTime copy() {
        return new WorldDateTime(world).setTicks(ticks);
    }
}
