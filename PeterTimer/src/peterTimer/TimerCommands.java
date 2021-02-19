package peterTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class TimerCommands implements CommandExecutor {

	private List<Timer> timers;
	private Map<Integer, TimeRunnable> map;
	private JavaPlugin plugin;
	
	private String WARNSEQ = ConsoleMessageColors.WARN;
	private String ERRORSEQ = ConsoleMessageColors.ERROR;
	private String INFOSEQ = ConsoleMessageColors.INFO;
	
	public TimerCommands(JavaPlugin plugin) {
		this.plugin = plugin;
		timers = new ArrayList<Timer>();
		map = new HashMap<Integer, TimeRunnable>();
		map.put(0, new TimeRunnable() {
			
			public void run(Timer timer) {
				Bukkit.broadcastMessage(ChatColor.GREEN + "Timer " + timer.getName() + " done");
				timer.stop();
				timer.removeAllPlayers();
			}
			
		});
	}
	
	private Timer exists(String name) {
		for(int i = 0; i < timers.size(); i++) {
			if(timers.get(i).getName().equals(name)) {
				return timers.get(i);
			}
		}
		return null;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command com, String lable, String[] args) {
		
		if(args.length == 1) { 
			if(args[0].equals("list")) {
				String names = "Timers: ";
				for(int i = 0; i < timers.size(); i++) {
					names += timers.get(i).getName();
					if(i < timers.size() - 1) {
						names += ", ";
					}
				}
				sender.sendMessage(names);
				return true;
			}
		}
		if(args.length >= 2) {
			Timer t = exists(args[1]);
			if(t != null) {
				if(args[0].equals("start")) {
					t.addAllPlayers();
					t.start();
					sender.sendMessage("Timer " + args[1] + " started");
					return true;
				} else if(args[0].equals("stop")) {
					t.stop();
					t.removeAllPlayers();
					sender.sendMessage("Timer " + args[1] + " stoped");
					return true;
				} else if(args[0].equals("reset")) {
					t.reset();
					sender.sendMessage("Timer " + args[1] + " reset");
					return true;
				} else if(args[0].equals("remove")) {
					timers.remove(t);
					sender.sendMessage("Timer " + args[1] + " removed");
					return true;
				} else if(args[0].equals("time")) {
					if(args.length == 3) {
						t.stop();
						t.setMaxTime(Integer.parseInt(args[2])*20);
						t.reset();
						sender.sendMessage("Timer " + args[1] + " max time set to " + (Integer.parseInt(args[2])*20) + " ticks and reset");
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + "Invalid number of arguments. Add a time");
						return false;
					}
				} else if(args[0].equals("title")) {
					if(args.length >= 3) {
						String title = "";
						for(int i = 2; i < args.length; i++) {
							if(i > 2) {
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
				} else {
					sender.sendMessage(ChatColor.RED + "Invalid argument.");
					return false;
				}
			} else {
				if(args[0].equals("add")) {
					timers.add(new Timer(1, args[1], map, false, plugin));
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
		List<String> l = new ArrayList<String>();
		for(int i = 0; i < timers.size(); i++) {
			l.add(timers.get(i).getName());
		}
		return l;
	}
	
	public void StopAll() {
		System.out.println(INFOSEQ + "Stopping all timers");
		for(int i = 0; i < timers.size(); i++) {
			timers.get(i).stop();
		}
		System.out.println(INFOSEQ + " ... done");
	}
	
	public void AddPlayer(Player p) {
//		for(int i = 0; i < timers.size(); i++) {
//			timers.get(i).addPlayer(p);
//		}
		System.out.println("Adding player to timers");
		for(Timer t : timers) {
			t.addPlayer(p);
//			System.out.println("Adding player to timer " + t.getName());
		}
	}

}
