package com.peter.petertimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class TimerCommands implements CommandExecutor {

	private final List<Timer> timers;
	private final Map<Integer, TimeRunnable> map;
	private final JavaPlugin plugin;

	@SuppressWarnings("NonConstantLogger")
	public final Logger log;

	public TimerCommands(JavaPlugin plugin) {
		this.plugin = plugin;
		log = plugin.getLogger();
		timers = new ArrayList<>();
		map = new HashMap<>();
		map.put(0, timer -> {
			Bukkit.broadcastMessage(ChatColor.GREEN + "Timer " + timer.getName() + " done");
			timer.stop();
			timer.removeAllPlayers();
		});
	}

	private Timer exists(String name) {
		for (int i = 0; i < timers.size(); i++) {
			if (timers.get(i).getName().equals(name)) {
				return timers.get(i);
			}
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command com, String label, String[] args) {

		if (args.length == 1) {
			if (args[0].equals("list")) {
				String names = "Timers: ";
				for (int i = 0; i < timers.size(); i++) {
					names += timers.get(i).getName();
					if (i < timers.size() - 1) {
						names += ", ";
					}
				}
				sender.sendMessage(names);
				return true;
			}
		}
		if (args.length >= 2) {
			Timer t = exists(args[1]);
			if (t != null) {
				switch (args[0]) {
					case "start" -> {
						t.addAllPlayers();
						t.start();
						sender.sendMessage("Timer " + args[1] + " started");
						return true;
					}
					case "stop" -> {
						t.stop();
						t.removeAllPlayers();
						sender.sendMessage("Timer " + args[1] + " stopped");
						return true;
					}
					case "reset" -> {
						t.reset();
						sender.sendMessage("Timer " + args[1] + " reset");
						return true;
					}
					case "remove" -> {
						timers.remove(t);
						sender.sendMessage("Timer " + args[1] + " removed");
						return true;
					}
					case "time" -> {
						if (args.length == 3) {
							t.stop();
							t.setMaxTime(Integer.parseInt(args[2]) * 20);
							t.reset();
							sender.sendMessage("Timer " + args[1] + " max time set to "
									+ (Integer.parseInt(args[2]) * 20) + " ticks and reset");
							return true;
						} else {
							sender.sendMessage(ChatColor.RED + "Invalid number of arguments. Add a time");
							return false;
						}
					}
					case "title" -> {
						if (args.length >= 3) {
							String title = "";
							for (int i = 2; i < args.length; i++) {
								if (i > 2) {
									title += " ";
								}
								title += args[i];
							}
							t.setTitle(title);
							sender.sendMessage("Timer " + args[1] + " title set to " + title);
							return true;
						} else {
							sender.sendMessage("Failed to set title of timer " + args[1] + ", no title");
							return false;
						}
					}
					default -> {
						sender.sendMessage(ChatColor.RED + "Invalid argument.");
						return false;
					}
				}
			} else {
				if (args[0].equals("add")) {
					timers.add(new Timer(1, args[1], map, plugin));
					sender.sendMessage("Timer " + args[1] + " added");
				} else {
					sender.sendMessage(ChatColor.RED + "That timer does not exist");
					return false;
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Invalid number of arguments.");
			return false;
		}
		return true;
	}

	public List<String> list() {
		List<String> l = new ArrayList<>();
		for (int i = 0; i < timers.size(); i++) {
			l.add(timers.get(i).getName());
		}
		return l;
	}

	public void StopAll() {
		log.info("Stopping all timers ...");
		for (int i = 0; i < timers.size(); i++) {
			timers.get(i).stop();
		}
		log.info(" ... done");
	}

	public void AddPlayer(Player p) {
		for (Timer t : timers) {
			t.addPlayer(p);
		}
	}

}
