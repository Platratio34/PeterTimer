package peterTimer;

import org.bukkit.scheduler.BukkitScheduler;

import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

public class Timer {
	
	private int totalTime;
	private int timeRemaning;
	private String title;
	private String name;
	private Boolean showTime;
	private Boolean running;
	private Map<Integer,TimeRunnable> callbacks;
	private int timeBetween = 1;
	private BukkitScheduler scheduler;
	private JavaPlugin plugin;
	private static int timerN;
	
	public Timer(int time, String name, Map<Integer,TimeRunnable> callbacks, Boolean showTime, JavaPlugin plugin) {
		this.showTime = showTime;
		this.name = name;
		this.callbacks = callbacks;
		this.plugin = plugin;
		if(time > 1) {
			totalTime = time;
		}
		scheduler = plugin.getServer().getScheduler();
		reset();
	}
	public Timer(int time, Map<Integer,TimeRunnable> callbacks, Boolean showTime, JavaPlugin plugin) {
		this.showTime = showTime;
		this.name = "default-" + timerN;
		this.callbacks = callbacks;
		this.plugin = plugin;
		if(time > 1) {
			totalTime = time;
		}
		scheduler = plugin.getServer().getScheduler();
		reset();
		timerN++;
	}
	
	public int getTicksRemaning() {
		return timeRemaning;
	}
	public int getSecondsRemaning() {
		return timeRemaning/20;
	}
	
	public void reset() {
		stop();
		timeRemaning = totalTime;
		running = true;
		if(showTime) {
			int s = timeRemaning/20;
			int m = s/60;
			s -= m*60;
			title = m + ":" + s;
		} else {
			title = name;
		}
	}
	
	public void start() {
		running = true;
		update(0);
	}
	public void stop() {
		running = false;
	}
	
	public void update(int dTime) {
		timeRemaning -= dTime;
		if(showTime) {
			int s = timeRemaning/20;
			int m = s/60;
			s -= m*60;
			title = m + ":" + s;
		}
		//update boss bar
		if(timeRemaning <= 0) {
			running = false;
			callbacks.get(0).run(this);
		} else {
			if(running) {
				callbacks.get(timeRemaning).run(this);
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					 
					 @Override
					 public void run() {
	                	 update(timeBetween);
					 }
					 
				 }, timeBetween);
			}
		}
	}
}
