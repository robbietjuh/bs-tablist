package net.robbytu.banjoserver.tablist;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		getLogger().info("Tablist has been enabled.");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Tablist has been disabled.");
	}
}
