package me.skrub.notes.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.skrub.notes.Main;

public class skrubnotes implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(sender.hasPermission("skrubnotes.skrub")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b---------------[&6&lSkrubNotes&b]---------------"));
				sender.sendMessage("");
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/sn reload &6- &bReload the config.yml file."));
				sender.sendMessage("");
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bVersion: &c" + Main.plugin.getDescription().getVersion()));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bMade by: &c" + Main.plugin.getDescription().getAuthors()));
			} else if(args.length == 1) {
				if(sender.hasPermission("skrubnotes.skrub.reload")) {
					if(args[0].equals("reload")) {
						Main.plugin.reloadConfig();
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("prefix") + Main.plugin.getConfig().getString("reload_msg")));
					}
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b---------------[&6&lSkrubNotes&b]---------------"));
				sender.sendMessage("");
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/sn reload &6- &bReload the config.yml file."));
				sender.sendMessage("");
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bVersion: &c" + Main.plugin.getDescription().getVersion()));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bMade by: &c" + Main.plugin.getDescription().getAuthors()));
			}
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("prefix")) + Main.plugin.getConfig().getString("noperm"));
		}
		return true;
	}

}