package peterTimer;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private Timer testTimer;
	
	@Override
	public void onEnable() {
		TimeRunnable done = new TimeRunnable() {
			@Override
			public void run(Timer timer) {
				Bukkit.getConsoleSender().sendMessage("Timer Done");
			}
		};
		HashMap<Integer,TimeRunnable> map = new HashMap<Integer,TimeRunnable>();
		map.put(0,done);
		Bukkit.getConsoleSender().sendMessage("PeterTimer Started");
		testTimer = new Timer(5*20, "testTimer", map, true, this);
		testTimer.start();
	}
	
	@Override
	public void onDisable() {
		
	}
}
