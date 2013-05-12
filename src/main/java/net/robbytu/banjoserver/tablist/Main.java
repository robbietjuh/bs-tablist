package net.robbytu.banjoserver.tablist;

import net.robbytu.banjoserver.framework.api.ServerAPI;
import net.robbytu.banjoserver.framework.interfaces.Server;

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
		
		// Register a scheduler to update the tab list of every online player, every 30 seconds
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				updateTablist();
			}
		}, 20, 200);
		
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
				
				// Update player's tablist
				TabAPI.updatePlayer(p);
			}
		}, 1);
	}
	
	private void buildTablist(Player p) {
		// Calculate total online for later use
		int totalOnline = 0;
		for(Server server : ServerAPI.getOnlineServers()) {
			totalOnline += server.serverPlayers;
		}
		
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
		TabAPI.setTabString(plugin, p, 3, 2, ChatColor.DARK_AQUA + "" + totalOnline + " / 256");

		TabAPI.setTabString(plugin, p, 4, 0, TabAPI.nextNull());
		TabAPI.setTabString(plugin, p, 4, 1, TabAPI.nextNull());
		TabAPI.setTabString(plugin, p, 4, 2, TabAPI.nextNull());
		
		if(getServer().getServerName().equalsIgnoreCase("hub")) {
			// Players don't have to know which players are in the lobby/hub server. We'll
			// serve them other, hopefully more interesting information.
			
			// Fetch servers
			Server[] onlineServers = ServerAPI.getOnlineServers();
			Server[] offlineServers = ServerAPI.getOfflineServers();
			int curRow = 5;
			
			if(onlineServers.length > 0) {
				for(Server server : onlineServers) {
					if(!server.serverName.equalsIgnoreCase("hub")) {
						TabAPI.setTabString(plugin, p, curRow, 0, ChatColor.BOLD + "" + server.serverName);
						TabAPI.setTabString(plugin, p, curRow, 1, TabAPI.nextNull() + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "      Open");
						TabAPI.setTabString(plugin, p, curRow, 2, TabAPI.nextNull() + ChatColor.GRAY + "" + server.serverPlayers + " online");
						curRow ++;
					}
				}
				if(offlineServers.length > 0) {
					TabAPI.setTabString(plugin, p, curRow, 0, TabAPI.nextNull());
					TabAPI.setTabString(plugin, p, curRow, 1, TabAPI.nextNull());
					TabAPI.setTabString(plugin, p, curRow, 2, TabAPI.nextNull());
					curRow ++;
				}
			}
			
			if(offlineServers.length > 0) {
				for(Server server : offlineServers) {
					TabAPI.setTabString(plugin, p, curRow, 0, ChatColor.BOLD + "" + server.serverName);
					TabAPI.setTabString(plugin, p, curRow, 1, TabAPI.nextNull() + ChatColor.DARK_RED + "  Onderhoud");
					TabAPI.setTabString(plugin, p, curRow, 2, TabAPI.nextNull());
					curRow ++;
				}
			}
		}
		else {
			// Fill up the rest of the slots with whatever players are currently online
			Player[] playerList = getServer().getOnlinePlayers();
			int verticalMax = 15; // God I'm bad at maths... XD
			
			if(playerList.length < 15) verticalMax = (int) Math.ceil(playerList.length / 3d);
			
			for(int vertical = 0; vertical < verticalMax; vertical++) {
				for(int horizontal = 0; horizontal < 3; horizontal++) {
					if(vertical * 3 + horizontal > playerList.length - 1) {
						// We can't let this slot 'left open', so we'll have to put in some random
						// junk TabAPI perfectly can create for us using its nextNull() method
						TabAPI.setTabString(plugin, p, vertical + 5, horizontal, TabAPI.nextNull());
					}
					else {
						Player currentPlayer = playerList[vertical * 3 + horizontal];
						TabAPI.setTabString(plugin, p, vertical + 5, horizontal, (currentPlayer.isOp() ? ChatColor.RED + "" + ChatColor.BOLD : "") + currentPlayer.getPlayerListName());
					}
				}
			}
		}
	}
	
	private void updateTablist() {
		// Build a tab list for every online player
		for(Player p:getServer().getOnlinePlayers()) {
			buildTablist(p);
		}
		
		// Now send everyone's tablist
		TabAPI.updateAll();
	}
}
