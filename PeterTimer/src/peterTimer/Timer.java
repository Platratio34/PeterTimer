package peterTimer;

import org.bukkit.scheduler.BukkitScheduler;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
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
	private BossBar bar;
	
	/**
	 * Constructor for Timer
	 * @param time - Starting time
	 * @param name - Name of the Timer
	 * @param callbacks - Map of ticks remaining to callbacks
	 * @param showTime - Whether or not to show the time as the bossbar title
	 * @param plugin - The Plugin making the timer
	 */
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
	/**
	 * Constructor for Timer
	 * @param time - Starting time
	 * @param callbacks - Map of ticks remaining to callbacks
	 * @param plugin - The Plugin making the timer
	 */
	public Timer(int time, Map<Integer,TimeRunnable> callbacks, JavaPlugin plugin) {
		showTime = true;
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
	
	/**
	 * Return the time remaining in ticks
	 * @return seconds ticks
	 */
	public int getTicksRemaning() {
		return timeRemaning;
	}
	/**
	 * Return the time remaining in seconds
	 * @return seconds reaming (rounding down)
	 */
	public int getSecondsRemaning() {
		return timeRemaning/20;
	}
	
	/**
	 * Resets the Timer
	 * -Sets time remaing to max time
	 * -resets bossbar title to name or time;
	 */
	public void reset() {
		bar = Bukkit.createBossBar(name, BarColor.WHITE, BarStyle.SOLID);
		stop();
		timeRemaning = totalTime;
		if(showTime) {
			int s = timeRemaning/20;
			int m = s/60;
			s -= m*60;
			title = m + ":" + s;
		} else {
			title = name;
		}
		bar.setTitle(title);
		bar.setProgress(1.0);
	}
	
	/**
	 * Starts the timer
	 */
	public void start() {
		running = true;
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			bar.addPlayer(player);
		}
		bar.setVisible(true);
		update(0);
	}
	/**
	 * Stops the timer
	 */
	public void stop() {
		running = false;
		bar.setVisible(false);
	}
	
	/**
	 * Updates the timer
	 * @param dTime - number of ticks since last update
	 */
	public void update(int dTime) {
		timeRemaning -= dTime;
		if(showTime) {
			int s = timeRemaning/20;
			int m = s/60;
			s -= m*60;
			title = m + ":" + s;
		}
		if(timeRemaning <= Math.min(200, totalTime/5)) {
				bar.setColor(BarColor.RED);
		} else if(timeRemaning <= totalTime/2) {
			bar.setColor(BarColor.YELLOW);
		}
		bar.setTitle(title);
		bar.setProgress((double)timeRemaning/(double)totalTime);
		//Bukkit.getConsoleSender().sendMessage("Timer: " + title + ", " + (double)timeRemaning/(double)totalTime);
		if(timeRemaning <= 0) {
			running = false;
			callbacks.get(0).run(this);
		} else {
			if(running) {
				if(callbacks.get(timeRemaning) != null) {
					callbacks.get(timeRemaning).run(this);
				}
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
