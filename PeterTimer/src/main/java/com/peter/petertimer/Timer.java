package com.peter.petertimer;

import java.util.Map;

import org.bukkit.boss.BarColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Timer extends AbstractTimer {
	
	/** Total time in ticks */
	private int totalTime;
	/** * Time remaining in ticks */
	private int timeRemaning;
	
	/**
	 * Constructor for Timer
	 * @param time Starting time
	 * @param name Name of the Timer
	 * @param callbacks Map of ticks remaining to callbacks
	 * @param plugin The Plugin making the timer
	 */
	@SuppressWarnings("OverridableMethodCallInConstructor")
	public Timer(int time, String name, Map<Integer,TimeRunnable> callbacks, JavaPlugin plugin) {
		super(name, plugin);
		setCallbacks(callbacks);

		if(time > 1) {
			totalTime = time;
		}
		
		reset();
	}
	/**
	 * Constructor for Timer. Timer will be named "default-#"
	 * @param time Starting time
	 * @param callbacks Map of ticks remaining to callbacks
	 * @param plugin The Plugin making the timer
	 */
	@SuppressWarnings("OverridableMethodCallInConstructor")
	public Timer(int time, Map<Integer,TimeRunnable> callbacks, JavaPlugin plugin) {
		super("default-" + timerN++, plugin);
		setCallbacks(callbacks);
		
		if(time > 1) {
			totalTime = time;
		}
		
		reset();
	}
	/**
	 * Constructor for Timer
	 * @param time Starting time
	 * @param name Name of the Timer
	 * @param plugin The Plugin making the timer
	 */
	@SuppressWarnings("OverridableMethodCallInConstructor")
	public Timer(int time, String name, JavaPlugin plugin) {
		super(name, plugin);
		
		if(time > 1) {
			totalTime = time;
		}
		
		reset();
	}

    @Override
    public int getTime() {
        return timeRemaning;
    }
	
	/**
	 * Resets the Timer:
	 * setting time remaining to max time,
	 * and reseting bossbar title to name or time
	 */
	public void reset() {
		stop();
		calcMaxBetween(totalTime);
		timeRemaning = totalTime;
		for(DisplayBar b : bars.values()) {
			b.update(format(timeRemaning), 1.0);
			b.setColor(BarColor.GREEN);
		}
		
	}
	
	/**
	 * Updates the timer. INTERNAL USE ONLY
	 * @param dTime Number of ticks since last update
	 */
	@Override
	protected void update(int dTime) {
		timeRemaning -= dTime;
		if(timeRemaning <= 0) {
			running = false;
			callbacks.get(0).run(this);
		} else {
			if(running) {
				if(callbacks.get(timeRemaning) != null) {
					callbacks.get(timeRemaning).run(this);
				}
				scheduleNext(timeRemaning);
			}
		}
		updateBars(timeRemaning, totalTime);
	}
	
	/**
	 * Sets the time remaining only if the timer is stopped
	 * @param t New time remaining
	 * @return If the time was set
	 */
	public boolean setTime(int t) {
		if(!running) {
			timeRemaning = t;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Sets the max time only if the timer is stopped
	 * @param t New max time
	 * @return If the max time was set
	 */
	public boolean setMaxTime(int t) {
		if(!running) {
			totalTime = t;
			return true;
		} else {
			return false;
		}
	}
}
