package me.skrub.notes.commands;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.skrub.notes.Main;

public class withdraw implements CommandExecutor {
	
	public static boolean isInt(String str) { 
		try {  
			Double.parseDouble(str);  
			return true;
		} catch(NumberFormatException e){  
		    return false;  
		}  
	}
	
	private void createNote(Player p, Double amount) {
		if(amount > Main.econ.getBalance(p)) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("prefix") +  Main.plugin.getConfig().getString("notenough")));
			return;
		} else {
			Main.econ.format((long)Main.econ.getBalance(p));
			String currency = Main.plugin.getConfig().getString("currency");
			ItemStack note = new ItemStack(Material.PAPER);
			ItemMeta meta = note.getItemMeta();
			List<String> lore = Main.plugin.getConfig().getStringList("note.lore");
			String[] l = lore.toArray(new String[lore.size()]);
			for(int i = 0; i < l.length; i++) {
				if(l[i].contains("{value}")) {
					l[i] = l[i].replace("{value}", currency + NumberFormat.getInstance().format(amount));
					l[i] = ChatColor.translateAlternateColorCodes('&', l[i]);
				} else if(l[i].contains("{player}")) {
					l[i] = l[i].replace("{player}", p.getName());
					l[i] = ChatColor.translateAlternateColorCodes('&', l[i]);
				}
			}
		    List<String> fixedLore = Arrays.asList(l);  
			meta.setLore(fixedLore);
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("note.name")));
			note.setItemMeta(meta);
			p.getInventory().addItem(note);
			Main.econ.withdrawPlayer(p, amount);
			if(Main.plugin.getConfig().getBoolean("msgenabled")) {
				if(Main.plugin.getConfig().getString("withdraw").contains("{amount}")) {
					String msg = ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("prefix") + Main.plugin.getConfig().getString("withdraw").replace("{amount}", NumberFormat.getInstance().format(amount)));
					p.sendMessage(msg);
				}
			} else {
				return;
			}
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(sender.hasPermission("skrubnotes.withdraw")) {
			if(sender instanceof Player) {
				if(args.length != 1) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("prefix") + Main.plugin.getConfig().getString("synerror") + "/withdraw <amount>"));
				} else {
					if(!isInt(args[0])) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("prefix") + " You must specify an amount to withdraw"));
					} else {
						if(isInt(Main.plugin.getConfig().getString("minimum"))) {
							double min = Double.parseDouble((Main.plugin.getConfig().getString("minimum")));
							double input = Double.parseDouble(args[0]);
							if(input >= min) {
								createNote(((Player) sender).getPlayer(), Double.parseDouble(args[0]));
							} else {
								if(Main.plugin.getConfig().getString("belowmin").contains("{minimum}")) {
									String belowmin = Main.plugin.getConfig().getString("belowmin").replace("{minimum}", NumberFormat.getInstance().format(Double.parseDouble(Main.plugin.getConfig().getString("minimum"))));
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("prefix")) + ChatColor.translateAlternateColorCodes('&', belowmin));
								} else {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("prefix")) + Main.plugin.getConfig().getString("belowmin"));
								}
							}
						} else {
							createNote(((Player) sender).getPlayer(), Double.parseDouble(args[0]));
						}
					}
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("prefix") + "Only players can run that command!"));
			}
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("prefix")) + Main.plugin.getConfig().getString("noperm"));
		}
		return true;
	}

}
