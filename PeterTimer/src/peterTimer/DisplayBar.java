package peterTimer;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/**
 * In between class for Minecraft's built in bossbar and timers
 */
public class DisplayBar {
	
	/**
	 * Minecraft BossBar
	 */
	public BossBar bar;
	/**
	 * Display name of bar
	 */
	private String name;
	/**
	 * If the name should be included in bar title
	 */
	private boolean showName;
	/**
	 * If time remaining should be in bar title
	 */
	private boolean showTime;
	
	/**
	 * Create a new DisplayBar with name, color and style. Shows both name and time in title
	 * @param name Display name
	 * @param color Bar display color
	 * @param style Bar display style
	 */
	public DisplayBar(String name, BarColor color, BarStyle style) {
		this.name = name;
		showName = true;
		showTime = true;
		bar = Bukkit.createBossBar(name, color, style);
	}
	/**
	 * Create a new DisplayBar with name, color and style. Shows both name and time in title
	 * @param name Display name
	 * @param color Bar display color
	 * @param style Bar display style
	 * @param showName Show name in title
	 * @param showTime Show time remaining in title
	 */
	public DisplayBar(String name, BarColor color, BarStyle style, boolean showName, boolean showTime) {
		this.name = name;
		this.showName = showName;
		this.showTime = showTime;
		bar = Bukkit.createBossBar(name, color, style);
	}
	
	/**
	 * Update time remaining text and bar progress
	 * @param time Time remaining formated string
	 * @param progress Bar progress from 0 to 1
	 */
	public void update(String time, double progress) {
		String title = "";
		if(showName) {
			title = name + " ";
		}
		if(showTime) {
			title += time + "";
		}
		bar.setTitle(title);
		bar.setProgress(progress);
	}
	
	/**
	 * Set the display name of the bar
	 * @param name Display name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Set the display color of the bar
	 * @param color Display color
	 */
	public void setColor(BarColor color) {
		bar.setColor(color);
	}
	
	/**
	 * Set the display style of the bar
	 * @param style Display style
	 */
	public void setStyle(BarStyle style) {
		bar.setStyle(style);
	}
	
	/**
	 * Set bar visibility
	 * @param visible If the bar should be visible
	 */
	public void setVisible(boolean visible) {
		bar.setVisible(visible);
	}
	
	/**
	 * Hide the bar from a player
	 * @param p Player to hide the bar from
	 */
	public void removePlayer(Player p) {
		bar.removePlayer(p);
	}
	
	/**
	 * Show the bar to a player
	 * @param p Player to show the bar to
	 */
	public void addPlayer(Player p) {
		bar.addPlayer(p);
	}
	
	/**
	 * Get all of the players who can see this bar
	 * @return List of all players who can see the bar
	 */
	public List<Player> getPlayers() {
		return bar.getPlayers();
	}
	
	/**
	 * Hide this bar (Does to remove players)
	 */
	public void remove() {
		bar.setVisible(false);
	}
	
	/**
	 * Sets the display name to be included in the title
	 */
	public void showName() {
		showName = true;
	}
	/**
	 * Sets whether the display name should be included in the title
	 * @param showName If the display name should be included in the title
	 */
	public void showName(boolean showName) {
		this.showName = showName;
	}
	/**
	 * Sets the display name to not be show in title
	 */
	public void hideName() {
		showName = false;
	}
	
	/**
	 * Sets the time remaining to be show in the title
	 */
	public void showTime() {
		showTime = true;
	}
	/**
	 * Sets whether the time remaining to be show in the title
	 * @param showTime If the time remaining should be in the title
	 */
	public void showTime(boolean showTime) {
		this.showTime = showTime;
	}
	/**
	 * Sets the time remaining to not be shown in the title
	 */
	public void hideTime() {
		showTime = false;
	}
}
