package net.robbytu.banjoserver.tablist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
		// Build up the basic tablist
		TabAPI.setTabString(plugin, p, 0, 0, TabAPI.nextNull());
		TabAPI.setTabString(plugin, p, 0, 1, ChatColor.RED + "" + ChatColor.BOLD + "Banjoserver");
		TabAPI.setTabString(plugin, p, 0, 2, TabAPI.nextNull());
		
		TabAPI.setTabString(plugin, p, 1, 0, TabAPI.nextNull());
		TabAPI.setTabString(plugin, p, 1, 1, TabAPI.nextNull());
		TabAPI.setTabString(plugin, p, 1, 2, TabAPI.nextNull());
		
		TabAPI.setTabString(plugin, p, 2, 0, ChatColor.AQUA + "Server");
		TabAPI.setTabString(plugin, p, 2, 1, ChatColor.AQUA + "Online in " + getServer().getServerName());
		TabAPI.setTabString(plugin, p, 2, 2, ChatColor.AQUA + "Totaal online");

		TabAPI.setTabString(plugin, p, 3, 0, ChatColor.DARK_AQUA + getServer().getServerName());
		TabAPI.setTabString(plugin, p, 3, 1, ChatColor.DARK_AQUA + "" + getServer().getOnlinePlayers().length);
		TabAPI.setTabString(plugin, p, 3, 2, ChatColor.DARK_AQUA + "0 / 256"); // Todo: bs-framework implementation
		
		if(getServer().getServerName().equalsIgnoreCase("hub")) {
			// Players don't have to know which players are in the lobby/hub server. We'll
			// serve them other, hopefully more interesting information.
			// Todo
		}
		else {
			// Fill up the rest of the slots with whatever players are online
			// Todo
		}
	}
}
