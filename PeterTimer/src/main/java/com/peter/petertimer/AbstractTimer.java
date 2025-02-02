package com.peter.petertimer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public abstract class AbstractTimer {

	/** Internal timer name */
	protected String name;
	/** If the timer is currently running */
	protected boolean running;
	/** Scheduler for update events */
	protected final BukkitScheduler scheduler;
	/** Plugin info */
	protected final JavaPlugin plugin;
	/** Global timer number, used for id in default name */
	protected static int timerN;
	/** Map of bossbars associated with timer */
	protected final Map<String, DisplayBar> bars = new HashMap<>();
	/** If the color of the bar should change based on time */
	protected boolean autoChange = true;
	/** Map of callbacks by time remaining */
	protected final Map<Integer, TimeRunnable> callbacks = new HashMap<>();
	/** Maximum number to ticks between updates */
	protected int maxBetween = 1;

	/** Logger for timer */
	@SuppressWarnings("NonConstantLogger")
	public final Logger log;

	/**
	 * Initialize the timer
	 * @param name Timer name
	 * @param plugin Plugin the timer is running on
	 */
	public AbstractTimer(String name, JavaPlugin plugin) {
		this.name = name;
		this.plugin = plugin;
		log = plugin.getLogger();
		scheduler = plugin.getServer().getScheduler();
		bars.put("main", new DisplayBar(name, BarColor.GREEN, BarStyle.SOLID));
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
	 * Stops the timer
	 */
	public void stop() {
		running = false;
		for(DisplayBar b : bars.values()) {
			b.setVisible(false);
		}
	}

	/**
	 * Updates the timer. INTERNAL USE ONLY
	 * @param dTime Number of ticks since last update
	 */
	protected abstract void update(int dTime);

	/**
	 * Update display bars
	 * @param ticksRemaining Ticks remaining on the timer
	 * @param totalTicks Total (expected) ticks of the timer
	 */
	protected void updateBars(int ticksRemaining, int totalTicks) {
		if(autoChange) {
			if(ticksRemaining <= Math.min(200, totalTicks/5)) {
				for(DisplayBar b : bars.values()) {
					b.setColor(BarColor.RED);
				}
			} else if(ticksRemaining <= totalTicks/2) {
				for(DisplayBar b : bars.values()) {
					b.setColor(BarColor.YELLOW);
				}
			}
		}
		for(DisplayBar b : bars.values()) {
			b.update(format(ticksRemaining), (double)ticksRemaining/(double)totalTicks);
		}
	}

	/**
	 * Schedule the next update
	 * @param ticksRemaining Number of (expected) ticks remaining
	 */
	protected void scheduleNext(int ticksRemaining) {
		if(!running) {
			return;
		}
		int next = runTill(maxBetween, ticksRemaining);
		scheduler.scheduleSyncDelayedTask(plugin, () -> {
				 update(next);
		}, next);
	}

	/**
	 * Check the maximum number of ticks till the next update
	 * @param max Maximum ticks till next update
	 * @param remaining Ticks remaining in the timer
	 * @return Ticks till next update
	 */
	private int runTill(int max, int remaining) {
		int out = max;
		for(int i = 1; i < max; i++) {
			if(callbacks.get(remaining - i) != null) {
				out = i;
			}
		}
		if(remaining%20 > 0) { // ensure that we will tick on the second
			out = Math.min(out, remaining%20);
		}
		if(remaining%180 > 0) { // ensure that we will tick on the second
			out = Math.min(out, remaining%180);
		}
		if(out > remaining) { // Make sure we don't bypass the end
			out = remaining;
		}
		return out;
	}

	/**
	 * Returns a formatted string expressing the time in seconds and minutes
	 * @param ticks Amount of time in ticks
	 * @return Formatted time string
	 */
	protected String format(int ticks) {
		int s = ticks/20;
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

	/**
	 * Add a callback as specific ticks remaining
	 * @param ticks    Ticks remaining for the callback to be called at (or after)
	 * @param callback Callback function
	 */
	public void addCallback(int ticksRemaining, TimeRunnable callback) {
		callbacks.put(ticksRemaining, callback);
	}

	/**
	 * Remove a callback at ticks remaining
	 * @param ticksRemaining Callback trigger ticks remaining
	 * @return The callback returned
	 */
	public boolean removeCallback(int ticksRemaining) {
		if (callbacks.containsKey(ticksRemaining)) {
			callbacks.remove(ticksRemaining);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Set all callbacks for this timer
	 * @param callbacks Map of callbacks by ticks remaining
	 */
	public void setCallbacks(Map<Integer, TimeRunnable> callbacks) {
		this.callbacks.clear();
		for(Entry<Integer, TimeRunnable> entry : callbacks.entrySet()) {
			this.callbacks.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Return the time remaining in ticks
	 * @return Ticks remaining
	 */
	public abstract int getTime();

	/**
	 * Return the time remaining in seconds
	 * @return Seconds remaining
	 */
	public float getTimeSeconds() {
		return getTime() / 20f;
	}

	/**
	 * Returns the name of the timer
	 * @return Timer name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the title of the main bar
	 * @param title The new title
	 */
	public void setTitle(String title) {
		bars.get("main").setName(title);
	}

	/**
	 * Sets the title of one of the bars
	 * @param title The new title
	 * @param bar   The bar to change
	 */
	public void setTitle(String title, String bar) {
		if (bars.containsKey(bar)) {
			bars.get(bar).setName(title);
		} else {
			log.log(Level.WARNING, " func: setTitle; Invalid bar key: {0}", bar);
		}
	}

	/**
	 * Sets whether or not to automatically change the color of the bar
	 * @param change Whether or not to automatically change the color of the bar
	 */
	public void setAutoChange(boolean change) {
		autoChange = change;
	}

	/**
	 * Sets the color of the main bar
	 * @param color New color
	 */
	public void setColor(BarColor color) {
		bars.get("main").setColor(color);
	}

	/**
	 * Sets the color of the specified bar
	 * @param color New color
	 * @param bar   Bar to change
	 */
	public void setColor(BarColor color, String bar) {
		if (bars.containsKey(bar)) {
			bars.get(bar).setColor(color);
		} else {
			log.log(Level.WARNING, " func: setColor; Invalid bar key: {0}", bar);
		}
	}

	/**
	 * Sets the style of the main bar
	 * @param style New style
	 */
	public void setStyle(BarStyle style) {
		bars.get("main").setStyle(style);
	}

	/**
	 * Sets the style of the specified bar
	 * @param style New style
	 * @param bar   Bar to change
	 */
	public void setStyle(BarStyle style, String bar) {
		if (bars.containsKey(bar)) {
			bars.get(bar).setStyle(style);
		} else {
			log.log(Level.WARNING, " func: setStyle; Invalid bar key: {0}", bar);
		}
	}

	/**
	 * Adds a bar to the timer
	 * @param key   The key for the bar, used later to access it
	 * @param title The title of the bar
	 */
	public void addBar(String key, String title) {
		bars.put(key, new DisplayBar(title, BarColor.GREEN, BarStyle.SOLID));
	}

	/**
	 * Removes a bar from the timer
	 * @param key The bar to remove
	 */
	public void removeBar(String key) {
		bars.get(key).remove();
		bars.remove(key);
	}

	/**
	 * Gets a bar for more fine control
	 * @param bar Bar name
	 * @return Bar
	 */
	public DisplayBar getBar(String bar) {
		return bars.get(bar);
	}

	/**
	 * Adds all players to the main bar
	 */
	public void addAllPlayers() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			bars.get("main").addPlayer(player);
		}
	}

	/**
	 * Adds all players to a bar
	 * @param bar Bar to add them to
	 */
	public void addAllPlayers(String bar) {
		if (bars.containsKey(bar)) {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				bars.get(bar).addPlayer(player);
			}
		} else {
			log.log(Level.WARNING, " func: addAllPlayers; Invalid bar key: {0}", bar);
		}
	}

	/**
	 * Adds a player to the main bar
	 * @param player Player to add
	 */
	public void addPlayer(Player player) {
		bars.get("main").addPlayer(player);
	}

	/**
	 * Adds a player to a bar
	 * @param player   Player to add
	 * @param bar Bar to add them to
	 */
	public void addPlayer(Player player, String bar) {
		if (bars.containsKey(bar)) {
			bars.get(bar).addPlayer(player);
		} else {
			log.log(Level.WARNING, " func: addPlayer; Invalid bar key: {0}", bar);
		}
	}

	/**
	 * Adds a list of players to the main bar
	 * @param players List of player to add
	 */
	public void addPlayer(List<Player> players) {
		for (Player player : players) {
			bars.get("main").addPlayer(player);
		}
	}

	/**
	 * Adds a list of players to a bar
	 * @param players   List of players to add
	 * @param bar Bar to add them to
	 */
	public void addPlayer(List<Player> players, String bar) {
		if (bars.containsKey(bar)) {
			for (Player player : players) {
				bars.get(bar).addPlayer(player);
			}
		} else {
			log.log(Level.WARNING, " func: addPlayer; Invalid bar key: {0}", bar);
		}
	}

	/**
	 * Removes all players from the main bar
	 */
	public void removeAllPlayers() {
		List<Player> p = bars.get("main").getPlayers();
		for (Player player : p) {
			bars.get("main").removePlayer(player);
		}
	}

	/**
	 * Removes all players from a bar
	 * @param bar Bar to remove them from
	 */
	public void removeAllPlayers(String bar) {
		if (bars.containsKey(bar)) {
			List<Player> p = bars.get(bar).getPlayers();
			for (Player player : p) {
				bars.get(bar).removePlayer(player);
			}
		} else {
			log.log(Level.WARNING, " func: removeAllPlayers; Invalid bar key: {0}", bar);
		}
	}

	/**
	 * Removes a player from the timer
	 * @param player Player to remove
	 */
	public void removePlayer(Player player) {
		bars.get("main").removePlayer(player);
	}

	/**
	 * Removes a player from the timer
	 * @param player   Player to remove
	 * @param bar Bar to remove them from
	 */
	public void removePlayer(Player player, String bar) {
		if (bars.containsKey(bar)) {
			bars.get(bar).removePlayer(player);
		} else {
			log.log(Level.WARNING, " func: removePlayer; Invalid bar key: {0}", bar);
		}
	}

	/**
	 * Removes a list of players from the timer
	 * @param players List of players to remove
	 */
	public void removePlayer(List<Player> players) {
		for (Player player : players) {
			bars.get("main").removePlayer(player);
		}
	}

	/**
	 * Removes a list of players from the timer
	 * @param players   List of players to remove
	 * @param bar Bar to remove them from
	 */
	public void removePlayer(List<Player> players, String bar) {
		if (bars.containsKey(bar)) {
			for (Player player : players) {
				bars.get(bar).removePlayer(player);
			}
		} else {
			log.log(Level.WARNING, " func: removePlayer; Invalid bar key: {0}", bar);
		}
	}

	/**
	 * Calculate and set the maximum ticks between updates
	 * @param totalTicks Total ticks the timer is expected to run
	 */
	protected void calcMaxBetween(int totalTicks) {
		maxBetween = Math.clamp(totalTicks/180, 1, 20);
	}

}
