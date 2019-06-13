package me.skrub.notes;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.skrub.notes.commands.skrubnotes;
import me.skrub.notes.commands.withdraw;
import me.skrub.notes.events.interact;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{
	
	public static Main plugin;
	
	public static Economy econ = null;
	
	public void onEnable() {
		plugin = this;
	    if (!setupEconomy()) {
	        getLogger().severe(String.format("Could not find dependency Vault! Shutting down...", new Object[] { getDescription().getName() }));
	        getServer().getPluginManager().disablePlugin(this);
	        return;
	    }
	    saveDefaultConfig();
	    plugin.getConfig().options().copyDefaults(true);
	    if (!plugin.getDataFolder().exists()) {
	      plugin.getDataFolder().mkdir();
	    }
	    getServer().getPluginCommand("skrubnotes").setExecutor(new skrubnotes());
	    getServer().getPluginCommand("withdraw").setExecutor(new withdraw());
	    getServer().getPluginManager().registerEvents(new interact(), this);
	}
	
	public void onDisable() {
		
	}
	
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = (Economy)rsp.getProvider();
		return (econ != null);
	}

}
