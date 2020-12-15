package peterTimer;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class Main extends JavaPlugin {
	
	private Timer testTimer;
	private TimerCommands com;
	
	public boolean dev = false;;

	private String WARNSEQ = ConsoleMessageColors.WARN;
	private String ERRORSEQ = ConsoleMessageColors.ERROR;
	private String INFOSEQ = ConsoleMessageColors.INFO;
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdf = this.getDescription();
		if(pdf.getVersion().contains("-Dev")) {
			System.out.println(WARNSEQ + "You are running a dev version of this plugin");
			dev = true;
		}
		com = new TimerCommands(this);
		getCommand("timer").setExecutor(com);
		getCommand("timer").setTabCompleter(new TimerCompleter(com));
		
//		TimeRunnable done = new TimeRunnable() {
//			public void run(Timer timer) {
//				Bukkit.getConsoleSender().sendMessage("Timer Done");
//				testTimer.stop();
//				testTimer.removeAllPlayers();
//				testTimer.removeAllPlayers("2");
//			}
//		};
//		HashMap<Integer,TimeRunnable> map = new HashMap<Integer,TimeRunnable>();
//		map.put(0,done);
//		Bukkit.getConsoleSender().sendMessage("PeterTimer Started");
//		testTimer = new Timer(30*20, "testTimer", map, true, this);
//		testTimer.addAllPlayers();
//		testTimer.addBar("2", "2");
//		testTimer.addAllPlayers("2");
//		testTimer.setTitle("1");
//		testTimer.start();
		
		System.out.println(INFOSEQ + "Started");
		//if(!Bukkit.getServer().getClass().getPackage().getName().contains(pdf.getAPIVersion())) {System.out.println(WARNSEQ + "Incorect MC Version");}
	}
	
	@Override
	public void onDisable() {
		com.StopAll();
	}
}
