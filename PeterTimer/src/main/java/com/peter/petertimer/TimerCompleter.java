package com.peter.petertimer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TimerCompleter implements TabCompleter {
	
	private TimerCommands tC;
	
	public TimerCompleter(TimerCommands tC) {
		this.tC = tC;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command com, String lable, String[] args) {
		List<String> l = new ArrayList<String>();
		if(args.length == 1) {
			l.add("list");
			l.add("add");
			l.add("remove");
			l.add("start");
			l.add("stop");
			l.add("reset");
			l.add("time");
			l.add("title");
			l = fix(l, args[0]);
		} else {
			if(args.length == 2 && !args[0].equals("list")) {
				l = tC.list();
				l = fix(l, args[1]);
			}
		}
		return l;
	}
	
	private List<String> fix(List<String> in, String str) {
		List<String> l = new ArrayList<String>();
		for(int i = 0; i < in.size(); i++) {
			if(in.get(i).substring(0,Math.min(str.length(), in.get(i).length())).equals(str)) {
				l.add(in.get(i));
			}
		}
		if(l.size()==0) {
			return null;
		}
		return l;
	}

}
