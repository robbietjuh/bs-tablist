package net.robbytu.banjoserver.tablist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcsg.double0negative.tabapi.TabAPI;

public class Main extends JavaPlugin implements Listener {
	final Main plugin = this;
	
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
		// Fetch the player and set priority for our plugin to update his or her tablist
		final Player p = event.getPlayer();
		TabAPI.setPriority(plugin, p, 0);
		
		// Work around a Bukkit "bug" (event gets fired before the user is actually really logged in...)
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				// Build a tablist for the player
				buildTablist(p);
			}
		}, 1);
	}
	
	private void buildTablist(Player p) {
		// Todo
	}
}
