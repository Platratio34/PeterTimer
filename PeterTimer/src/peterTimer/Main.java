package peterTimer;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class Main extends JavaPlugin {
	
	public static Logger log;
	
	private TimerCommands com;
	
	public boolean dev = false;

//	private String WARNSEQ = ConsoleMessageColors.WARN;
//	private String ERRORSEQ = ConsoleMessageColors.ERROR;
//	private String INFOSEQ = ConsoleMessageColors.INFO;
	
	@Override
	public void onEnable() {

		log = super.getLogger();
		
		PluginDescriptionFile pdf = this.getDescription();
		if(pdf.getVersion().contains("-Dev")) {
//			System.out.println(WARNSEQ + "You are running a dev version of this plugin");
			log.warning("You are running a dev version of this plugin");
			dev = true;
		}
		if(!Bukkit.getVersion().contains(pdf.getAPIVersion())) {
			log.warning("You are running a unsuported server version, check for a new version for you server. (Server:" + Bukkit.getVersion() + ", Plugin:" + pdf.getAPIVersion() + ")");
		}
		PluginManager pm = Bukkit.getServer().getPluginManager();
		com = new TimerCommands(this);
		pm.registerEvents(new JoinListener(com), this);
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
		
		int time = 30 * 20; // 30 seconds in ticks
		String name = "testTimer"; // Timer name
		JavaPlugin plugin = this; // The plugin creating the timer

		Timer timer = new Timer(time, name, this); // Create a new timer with time and name
		timer.addAllPlayers(); // Show the timer to all players

		timer.addBar("2", "2"); // Add a second bar with title "2"
		timer.addAllPlayers("2"); // Show the second bar to all players
		timer.setTitle("1"); // Set the title of the main bar to "1"

		TimeRunnable done = new TimeRunnable() { // Create callback for when timer is done
			public void run(Timer timer) {
				Bukkit.getConsoleSender().sendMessage("Timer Done"); // Send a message to the console saying the timer is done
				timer.stop(); // Stop the timer (Optional)
				timer.removeAllPlayers(); // Hide the main bar from all players (It wont show up if you restart it then)
				timer.removeAllPlayers("2"); // Hide the second bar from all players (It wont show up if you restart it then)
			}
		};
		timer.addCallback(0,done); // Add the callback at 0 ticks remaining

		timer.start(); // Start the timer
		
		log.info("Started");
		//if(!Bukkit.getServer().getClass().getPackage().getName().contains(pdf.getAPIVersion())) {System.out.println(WARNSEQ + "Incorect MC Version");}
	}
	
	public void registerEvents() {
	    //This first line is optional, makes it faster with lots of classes
	    PluginManager pm = Bukkit.getServer().getPluginManager();
//	    pm.registerEvents(new , this);
	}
	
	@Override
	public void onDisable() {
		com.StopAll();
	}
}
