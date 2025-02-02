package com.peter.petertimer;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class Main extends JavaPlugin {

	@SuppressWarnings("NonConstantLogger")
	public Logger logger;

	private TimerCommands com;

	public boolean dev = false;

	@Override
	public void onEnable() {

		logger = super.getLogger();

		PluginDescriptionFile pdf = this.getDescription();
		if (pdf.getVersion().contains("-Dev")) {
			// System.out.println(WARNSEQ + "You are running a dev version of this plugin");
			logger.warning("You are running a dev version of this plugin");
			dev = true;
		}
		if (!Bukkit.getVersion().contains(pdf.getAPIVersion())) {
			logger.log(Level.WARNING,
					"You are running a unsupported server version, check for a new version for you server. (Server:{0}, Plugin:{1})",
					new Object[] { Bukkit.getVersion(), pdf.getAPIVersion() });
		}
		PluginManager pm = Bukkit.getServer().getPluginManager();
		com = new TimerCommands(this);
		pm.registerEvents(new JoinListener(com), this);
		PluginCommand command = getCommand("timer");
		if (command == null) {
			logger.warning("Unable to register timer command");
		} else {
			command.setExecutor(com);
			command.setTabCompleter(new TimerCompleter(com));
		}

		// int time = 30 * 20; // 30 seconds in ticks
		// String name = "testTimer"; // Timer name
		// JavaPlugin plugin = this; // The plugin creating the timer

		// Timer timer = new Timer(time, name, this); // Create a new timer with time and name
		// timer.addAllPlayers(); // Show the timer to all players

		// timer.addBar("2", "2"); // Add a second bar with title "2"
		// timer.addAllPlayers("2"); // Show the second bar to all players
		// timer.setTitle("1"); // Set the title of the main bar to "1"

		// TimeRunnable done = timer1 -> { // Create callback for when timer is done
		// 	Bukkit.getConsoleSender().sendMessage("Timer Done"); // Send a message to the console saying the timer is done
		// 	timer1.stop(); // Stop the timer (Optional)
		// 	timer1.removeAllPlayers(); // Hide the main bar from all players (It wont show up if you restart it then)
		// 	timer1.removeAllPlayers("2"); // Hide the second bar from all players (It wont show up if you restart it then)
		// };
		
		// timer.addCallback(0, done); // Add the callback at 0 ticks remaining

		// timer.start(); // Start the timer

		// logger.info("Started PeterTimer");
		// if(!Bukkit.getServer().getClass().getPackage().getName().contains(pdf.getAPIVersion()))
		// {System.out.println(WARNSEQ + "Incorrect MC Version");}
	}

	public void registerEvents() {
		// This first line is optional, makes it faster with lots of classes
		PluginManager pm = Bukkit.getServer().getPluginManager();
		// pm.registerEvents(new , this);
	}

	@Override
	public void onDisable() {
		com.StopAll();
	}
}
