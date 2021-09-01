package peterTimer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Timer {
	
	private int totalTime;
	private int timeRemaning;
	//private String title;
	private String name;
	private boolean showOnlyTime;
	private boolean running;
	private Map<Integer,TimeRunnable> callbacks;
	private int timeBetween = 1;
	private BukkitScheduler scheduler;
	private JavaPlugin plugin;
	private static int timerN;
	private Map<String,DisplayBar> bars;
	private boolean autoChange;
//	private String WARNSEQ = ConsoleMessageColors.WARN;
//	private String ERRORSEQ = ConsoleMessageColors.ERROR;
//	private String INFOSEQ = ConsoleMessageColors.INFO;
	
	public Logger log;
	
	/**
	 * Constructor for Timer
	 * @param time - Starting time
	 * @param name - Name of the Timer
	 * @param callbacks - Map of ticks remaining to callbacks
	 * @param showOnlyTime - Whether or not to show the time as the bossbar title
	 * @param plugin - The Plugin making the timer
	 */
	public Timer(int time, String name, Map<Integer,TimeRunnable> callbacks, Boolean showOnlyTime, JavaPlugin plugin) {
		this.showOnlyTime = showOnlyTime;
		this.name = name;
		this.callbacks = callbacks;
		this.plugin = plugin;
		
		log = plugin.getLogger();
		if(time > 1) {
			totalTime = time;
		}
		scheduler = plugin.getServer().getScheduler();
		autoChange = true;
		bars = new HashMap<String, DisplayBar>();
		bars.put("main", new DisplayBar(name, BarColor.GREEN, BarStyle.SOLID));
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
		bars = new HashMap<String, DisplayBar>();
		bars.put("main", new DisplayBar(name, BarColor.GREEN, BarStyle.SOLID));
		reset();
		timerN++;
	}
	/**
	 * Constructor for Timer
	 * @param time - Starting time
	 * @param callbacks - Map of ticks remaining to callbacks
	 * @param showOnlyTime - Whether or not to show the time as the bossbar title
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
		bars = new HashMap<String, DisplayBar>();
		bars.put("main", new DisplayBar(name, BarColor.GREEN, BarStyle.SOLID));
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
	 * @return seconds reaming(rounding down)
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
	@Deprecated
	public void setName(String name) {
		this.name = name;
		bars.get("main").setName(name);
	}
	
	/**
	 * Sets the title of the main bar
	 * @param title - the new title
	 */
	public void setTitle(String title) {
		bars.get("main").setName(title);
	}
	
	/**
	 * Sets the title of one of the bars
	 * @param title - the new title
	 * @param bar - the bar to change
	 */
	public void setTitle(String title, String bar) {
		if(bars.containsKey(bar)) {
			bars.get(bar).setName(title);
		} else {
			log.warning(" func: setTitle; Invalid bar key: " + bar);
		}
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
		stop();
		timeBetween = Math.max(Math.min(totalTime/180, 1), 20);
		timeBetween = clamp(totalTime/180, 1, 20);
		timeRemaning = totalTime;
		/*if(showOnlyTime) {
			title = format(timeRemaning);
		} else {
			title = name + " " + format(timeRemaning);
		}
		bars.get("main").update(format(timeRemaning), 1.0);*/
		for(DisplayBar b : bars.values()) {
			b.update(format(timeRemaning), 1.0);
			b.setColor(BarColor.GREEN);
		}
		
	}
	
	/**
	 * Starts the timer
	 */
	public void start() {
		if(running == false) {
			running = true;
			for(DisplayBar b : bars.values()) {
				b.setVisible(true);
			}
			update(0);
		}
	}
	
	/**
	 * Adds all players to the main bar
	 */
	public void addAllPlayers() {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			bars.get("main").addPlayer(player);
		}
	}
	/**
	 * Adds all players to a bar
	 * @param bar - Bar to add them to
	 */
	public void addAllPlayers(String bar) {
		if(bars.containsKey(bar)) {
			for(Player player : Bukkit.getServer().getOnlinePlayers()) {
				bars.get(bar).addPlayer(player);
			}
		} else {
			log.warning(" func: addAllPlayers; Invalid bar key: " + bar);
		}
	}
	
	/**
	 * Adds a player to the main bar
	 * @param p - Player to add
	 */
	public void addPlayer(Player p) {
		bars.get("main").addPlayer(p);
	}
	/**
	 * Adds a player to a bar
	 * @param p - Player to add
	 * @param bar - Bar to add them to
	 */
	public void addPlayer(Player p, String bar) {
		if(bars.containsKey(bar)) {
			bars.get(bar).addPlayer(p);
		} else {
			log.warning(" func: addPlayer; Invalid bar key: " + bar);
		}
	}
	/**
	 * Adds a list of players to the main bar
	 * @param p - List of player to add
	 */
	public void addPlayer(List<Player> p) {
		for(Player player : p) {
			bars.get("main").addPlayer(player);
		}
	}
	/**
	 * Adds a list of players to a bar
	 * @param p - List of players to add
	 * @param bar - Bar to add them to
	 */
	public void addPlayer(List<Player> p, String bar) {
		if(bars.containsKey(bar)) {
			for(Player player : p) {
				bars.get(bar).addPlayer(player);
			}
		} else {
			log.warning(" func: addPlayer; Invalid bar key: " + bar);
		}
	}
	
	/**
	 * Removes all players from the main bar
	 */
	public void removeAllPlayers() {
		List<Player> p = bars.get("main").getPlayers();
		for(Player player : p) {
			bars.get("main").removePlayer(player);
		}
	}
	/**
	 * Removes all players from a bar
	 * @param bar - Bar to remove them from
	 */
	public void removeAllPlayers(String  bar) {
		if(bars.containsKey(bar)) {
			List<Player> p = bars.get(bar).getPlayers();
			for(Player player : p) {
				bars.get(bar).removePlayer(player);
			}
		} else {
			log.warning(" func: removeAllPlayers; Invalid bar key: " + bar);
		}
	}
	
	/**
	 * Removes a player from the timer
	 * @param p - Player to remove
	 */
	public void removePlayer(Player p) {
		bars.get("main").removePlayer(p);
	}
	/**
	 * Removes a player from the timer
	 * @param p - Player to remove
	 * @param bar - Bar to remove them from
	 */
	public void removePlayer(Player p, String bar) {
		if(bars.containsKey(bar)) {
			bars.get(bar).removePlayer(p);
		} else {
			log.warning(" func: removePlayer; Invalid bar key: " + bar);
		}
	}
	/**
	 * Removes a list of players from the timer
	 * @param p - List of players to remove
	 */
	public void removePlayer(List<Player> p) {
		for(Player player : p) {
			bars.get("main").removePlayer(player);
		}
	}
	/**
	 * Removes a list of players from the timer
	 * @param p - List of players to remove
	 * @param bar - Bar to remove them from
	 */
	public void removePlayer(List<Player> p, String bar) {
		if(bars.containsKey(bar)) {
			for(Player player : p) {
				bars.get(bar).removePlayer(player);
			}
		} else {
			log.warning(" func: removePlayer; Invalid bar key: " + bar);
		}
	}
	
	/**
	 * Stops the timer
	 */
	public void stop() {
		running = false;
		for(DisplayBar b : bars.values()) {
			b.setVisible(false);
		}
	}
	
	/**
	 * Updates the timer
	 * @param dTime - number of ticks since last update
	 */
	public void update(int dTime) {
		timeRemaning -= dTime;
		if(timeRemaning <= 0) {
			running = false;
			callbacks.get(0).run(this);
		} else {
			if(running) {
				if(callbacks.get(timeRemaning) != null) {
					callbacks.get(timeRemaning).run(this);
				}
				int next = runTill(timeBetween, timeRemaning);
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					 
					 @Override
					 public void run() {
	                	 update(next);
					 }
					 
				 }, next);
			}
		}
		if(autoChange) {
			if(timeRemaning <= Math.min(200, totalTime/5)) {
				for(DisplayBar b : bars.values()) {
					b.setColor(BarColor.RED);
				}
			} else if(timeRemaning <= totalTime/2) {
				for(DisplayBar b : bars.values()) {
					b.setColor(BarColor.YELLOW);
				}
			}
		}
		for(DisplayBar b : bars.values()) {
			b.update(format(timeRemaning), (double)timeRemaning/(double)totalTime);
		}
		//Bukkit.getConsoleSender().sendMessage("Timer: " + title + ", " + (double)timeRemaning/(double)totalTime);
//		if(timeRemaning <= 0) {
//			running = false;
//			callbacks.get(0).run(this);
//		} else {
//			if(running) {
//				if(callbacks.get(timeRemaning) != null) {
//					callbacks.get(timeRemaning).run(this);
//				}
//				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
//					 
//					 @Override
//					 public void run() {
//	                	 update(timeBetween);
//					 }
//					 
//				 }, timeBetween);
//			}
//		}
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
	 * Sets the color of the main bar
	 * @param color - new color
	 */
	public void setColor(BarColor color) {
		bars.get("main").setColor(color);
	}
	/**
	 * Sets the color of a bar
	 * @param color - new color
	 * @param bar - Bar to change
	 */
	public void setColor(BarColor color, String bar) {
		if(bars.containsKey(bar)) {
			bars.get(bar).setColor(color);
		} else {
			log.warning(" func: setColor; Invalid bar key: " + bar);
		}
	}
	
	/**
	 * Sets the style of the boss bar
	 * @param style - new style
	 */
	public void setStyle(BarStyle style) {
		bars.get("main").setStyle(style);
	}
	/**
	 * Sets the style of the boss bar
	 * @param style - new style
	 * @param bar - Bar to change
	 */
	public void setStyle(BarStyle style, String bar) {
		if(bars.containsKey(bar)) {
			bars.get(bar).setStyle(style);
		} else {
			log.warning(" func: setStyle; Invalid bar key: " + bar);
		}
	}
	
	/**
	 * Adds a bar to the timer
	 * @param key - The key for the bar, used later to access it
	 * @param title - the title of the bar
	 */
	public void addBar(String key, String title) {
		bars.put(key, new DisplayBar(title, BarColor.GREEN, BarStyle.SOLID));
	}
	
	/**
	 * Removes a bar from the timer
	 * @param key - The bar to remove
	 */
	public void removeBar(String key) {
		bars.get(key).remove();
		bars.remove(key);
	}
	
	/**
	 * Returns a formated string expressing the time in seconds and minutes
	 * @param ticks - amount of time in ticks
	 * @return formated string
	 */
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
	
	private int runTill(int max, int current) {
		int out = max;
		for(int i = 1; i < max; i++) {
			if(callbacks.get(current - i) != null) {
				out = i;
			}
		}
//		System.out.println(timeRemaning%20);
		if(timeRemaning%20 > 0) {
			out = Math.min(out, timeRemaning%20);
		}
		if(timeRemaning%180 > 0) {
			out = Math.min(out, timeRemaning%180);
		}
		return out;
	}
	
	private int clamp(int i, int min, int max) {
		return Math.min(Math.max(i,  min), max);
	}
	
	public void addCallback(int t, TimeRunnable c) {
		callbacks.put(t, c);
	}
	
	public boolean removeCallback(int t) {
		if(callbacks.containsKey(t)) {
			callbacks.remove(t);
			return true;
		} else {
			return false;
		}
	}
}
