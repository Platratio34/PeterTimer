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
	
	public DisplayBar(String name, BarColor color, BarStyle style) {
		this.name = name;
		bar = Bukkit.createBossBar(name, color, style);
	}
	
	public void update(String time, double progress) {
		bar.setTitle(name + time);
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
}
