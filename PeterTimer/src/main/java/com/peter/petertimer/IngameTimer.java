package com.peter.petertimer;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Ingame time based timer. Runs untill the specified ingame time
 */
public class IngameTimer extends AbstractTimer {

    /** End time of the timer */
    protected WorldDateTime endTime;
    /** World to track the time in */
    protected final World world;
    /** Last world time the timer was updated at */
    protected long lastWorldTime;
    /** World time of when the timer was started */
    protected long startWorldTime;

    /**
     * Create a new world time timer
     * @param name Name of the timer
     * @param plugin Plugin the timer is used in
     * @param endTime World time for the timer to end
     * @param world World the timer tracks in
     */
    public IngameTimer(String name, JavaPlugin plugin, long endTime, World world) {
        super(name, plugin);
        this.endTime = WorldDateTime.of(world, endTime);
        this.world = world;
    }

    /**
     * Create a new world time timer
     * @param name Name of the timer
     * @param plugin Plugin the timer is used in
     * @param endTime World time for the timer to end
     */
    public IngameTimer(String name, JavaPlugin plugin, WorldDateTime endTime) {
        super(name, plugin);
        this.endTime = endTime.copy();
        this.world = endTime.world;
    }

    @Override
    public void start() {
        startWorldTime = world.getTime();
        lastWorldTime = startWorldTime;
        calcMaxBetween((int)endTime.ticksTill());
        super.start();
    }

    @Override
    protected void update(int dTime) {
        long worldTime = world.getTime();
        int last = (int)endTime.ticksTill(lastWorldTime);
        int cur = (int)endTime.ticksTill(worldTime);

        for(int i = last + 1; i < cur; i++) {
            TimeRunnable callback = callbacks.get(i);
            if(callback != null) {
                if(callback instanceof TimeRunnableExact) {
                    continue;
                }
                callback.run(this);
            }
        }
        TimeRunnable callback = callbacks.get(cur);
        if(callback != null) {
            callback.run(this);
        }
        lastWorldTime = worldTime;

        if(cur <= 0) {
            stop();
        } else {
            updateBars(cur, (int)endTime.ticksTill(startWorldTime));
            scheduleNext(cur);
        }
    }

    @Override
    public int getTime() {
        return (int) endTime.ticksTill();
    }

    /**
     * Change the end time of the timer
     * @param endTime New end time of the timer
     * @throws IllegalArguemtnException If the new end time is not for the same world as the timer
     */
    public void setEndTime(WorldDateTime endTime) {
        if(endTime.world != world) {
            throw new IllegalArgumentException("New time must be for the same world as the timer");
        }
        this.endTime = endTime.copy();
        if(running)
            calcMaxBetween((int)(endTime.getTicks() - startWorldTime));
    }

    /**
     * Get the end time of the timer
     * @return Current end time
     */
    public WorldDateTime getEndTime() {
        return endTime.copy();
    }

}
