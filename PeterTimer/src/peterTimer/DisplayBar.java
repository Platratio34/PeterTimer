package peterTimer;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class DisplayBar {
	
	public BossBar bar;
	private String name;
	private boolean showName;
	private boolean showTime;
	
	public DisplayBar(String name, BarColor color, BarStyle style) {
		this.name = name;
		showName = true;
		showTime = true;
		bar = Bukkit.createBossBar(name, color, style);
	}
	public DisplayBar(String name, BarColor color, BarStyle style, boolean showName, boolean showTime) {
		this.name = name;
		this.showName = showName;
		this.showTime = showTime;
		bar = Bukkit.createBossBar(name, color, style);
	}
	
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setColor(BarColor color) {
		bar.setColor(color);
	}
	
	public void setStyle(BarStyle style) {
		bar.setStyle(style);
	}
	
	public void setVisible(boolean visible) {
		bar.setVisible(visible);
	}
	
	public void removePlayer(Player p) {
		bar.removePlayer(p);
	}
	
	public void addPlayer(Player p) {
		bar.addPlayer(p);
	}
	
	public List<Player> getPlayers() {
		return bar.getPlayers();
	}
	
	public void remove() {
		bar.setVisible(false);
	}
	
	public void showName() {
		showName = true;
	}
	public void showName(boolean showName) {
		this.showName = showName;
	}
	public void hideName() {
		showName = false;
	}
	
	public void showTime() {
		showTime = true;
	}
	public void showTime(boolean showTime) {
		this.showTime = showTime;
	}
	public void hideTime() {
		showTime = false;
	}
}
