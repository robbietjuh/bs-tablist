package net.robbytu.banjoserver.tablist;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		// Register for events
		getServer().getPluginManager().registerEvents(this, this);
		
		// We're done! Yay!
		getLogger().info("Tablist has been enabled.");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Tablist has been disabled.");
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		// Todo
	}
}
