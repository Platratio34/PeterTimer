package peterTimer;

import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;
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
	private boolean showOnlyTime;
	private boolean running;
	private Map<Integer,TimeRunnable> callbacks;
	private int timeBetween = 1;
	private BukkitScheduler scheduler;
	private JavaPlugin plugin;
	private static int timerN;
	private BossBar bar;
	private boolean autoChange;
	
	/**
	 * Constructor for Timer
	 * @param time - Starting time
	 * @param name - Name of the Timer
	 * @param callbacks - Map of ticks remaining to callbacks
	 * @param showTime - Whether or not to show the time as the bossbar title
	 * @param plugin - The Plugin making the timer
	 */
	public Timer(int time, String name, Map<Integer,TimeRunnable> callbacks, Boolean showOnlyTime, JavaPlugin plugin) {
		this.showOnlyTime = showOnlyTime;
		this.name = name;
		this.callbacks = callbacks;
		this.plugin = plugin;
		if(time > 1) {
			totalTime = time;
		}
		scheduler = plugin.getServer().getScheduler();
		autoChange = true;
		reset();
	}
	/**
	 * Constructor for Timer
	 * @param time - Starting time
	 * @param callbacks - Map of ticks remaining to callbacks
	 * @param plugin - The Plugin making the timer
	 */
	@Deprecated
	public Timer(int time, Map<Integer,TimeRunnable> callbacks, JavaPlugin plugin) {
		showOnlyTime = true;
		this.name = "default-" + timerN;
		this.callbacks = callbacks;
		this.plugin = plugin;
		if(time > 1) {
			totalTime = time;
		}
		scheduler = plugin.getServer().getScheduler();
		autoChange = true;
		reset();
		timerN++;
	}
	/**
	 * Constructor for Timer
	 * @param time - Starting time
	 * @param callbacks - Map of ticks remaining to callbacks
	 * @param showTime - Whether or not to show the time as the bossbar title
	 * @param plugin - The Plugin making the timer
	 */
	public Timer(int time, Map<Integer,TimeRunnable> callbacks, Boolean showOnlyTime, JavaPlugin plugin) {
		this.showOnlyTime = showOnlyTime;
		this.name = "default-" + timerN;
		this.callbacks = callbacks;
		this.plugin = plugin;
		if(time > 1) {
			totalTime = time;
		}
		scheduler = plugin.getServer().getScheduler();
		autoChange = true;
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
	 * Returns the name of the timer
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the timer
	 * @param name - new name for the timer
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets whether or not to automatically change the color of the bar
	 * @param change - Whether or not to automatically change the color of the bar
	 */
	public void setAutoChange(boolean change) {
		autoChange = change;
	}
	
	/**
	 * Resets the Timer
	 * -Sets time remaing to max time
	 * -resets bossbar title to name or time;
	 */
	public void reset() {
		bar = Bukkit.createBossBar(name, BarColor.GREEN, BarStyle.SOLID);
		stop();
		timeRemaning = totalTime;
		if(showOnlyTime) {
			title = format(timeRemaning);
		} else {
			title = name + " " + format(timeRemaning);
		}
		bar.setTitle(title);
		bar.setProgress(1.0);
	}
	
	/**
	 * Starts the timer
	 */
	public void start() {
		if(running == false) {
			running = true;
			bar.setVisible(true);
			update(0);
		}
	}
	/**
	 * Adds all players to the Timer
	 */
	public void addAllPlayers() {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			bar.addPlayer(player);
		}
	}
	
	/**
	 * Adds a player to the timer
	 * @param p - Player to add
	 */
	public void addPlayer(Player p) {
		bar.addPlayer(p);
	}
	/**
	 * Adds a list of players to the timer
	 * @param p - List of player to add
	 */
	public void addPlayer(List<Player> p) {
		for(Player player : p) {
			bar.addPlayer(player);
		}
	}
	
	/**
	 * Removes all players from the timer
	 */
	public void removeAllPlayers() {
		List<Player> p = bar.getPlayers();
		for(Player player : p) {
			bar.removePlayer(player);
		}
	}
	
	/**
	 * Removes a player from the timer
	 * @param p - Player to remove
	 */
	public void removePlayer(Player p) {
		bar.removePlayer(p);
	}
	/**
	 * Removes a list of players from the timer
	 * @param p - List of players to remove
	 */
	public void removePlayer(List<Player> p) {
		for(Player player : p) {
			bar.removePlayer(player);
		}
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
		if(showOnlyTime) {
			title = format(timeRemaning);
		} else {
			title = name + " " + format(timeRemaning);
		}
		if(autoChange) {
			if(timeRemaning <= Math.min(200, totalTime/5)) {
					bar.setColor(BarColor.RED);
			} else if(timeRemaning <= totalTime/2) {
				bar.setColor(BarColor.YELLOW);
			}
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
	
	/**
	 * Sets the time remaining, if the timer is stopped
	 * @param t - new time remaining
	 * @return completed
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
	 * Sets the max time, if the timer is stopped
	 * @param t - new max time
	 * @return completed
	 */
	public boolean setMaxTime(int t) {
		if(!running) {
			totalTime = t;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Sets the color of the boss bar
	 * @param color - new color
	 */
	public void setColor(BarColor color) {
		bar.setColor(color);
	}
	
	/**
	 * Sets the style of the boss bar
	 * @param style - new style
	 */
	public void setStyle(BarStyle style) {
		bar.setStyle(style);
	}
	
	private String format(int ticks) {
		int s = timeRemaning/20;
		int m = s/60;
		s -= m*60;
		String ss = s +"";
		String sm = m +"";
		if(ss.length()==1) {
			ss = "0" + ss;
		}
		if(sm.length()==1) {
			sm = "0" + sm;
		}
		return sm + ":" + ss;
	}
}
