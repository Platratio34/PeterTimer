package peterTimer;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
	
	public TimerCommands tc;
	
	public JoinListener(TimerCommands tc) {
		this.tc = tc;
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
        tc.AddAllPlayer();
    }
}
