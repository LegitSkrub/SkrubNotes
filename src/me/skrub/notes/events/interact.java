package me.skrub.notes.events;

import java.text.NumberFormat;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.skrub.notes.Main;

public class interact implements Listener{
	
	public static boolean isInt(String str) { 
		try {  
			Double.parseDouble(str);  
			return true;
		} catch(NumberFormatException e){  
		    return false;  
		}  
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		List<String> lore = Main.plugin.getConfig().getStringList("note.lore");
		if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		if (!(p.getItemInHand().hasItemMeta())) {
			return;
		}
		if (!(p.getItemInHand().getItemMeta().hasLore())) {
			return;
		}
		List<String> loreList = p.getItemInHand().getItemMeta().getLore();
		if(p.getItemInHand().getItemMeta().getLore().size() == loreList.size()) {
			String[] l = loreList.toArray(new String[loreList.size()]);
			String[] la = lore.toArray(new String[lore.size()]);
			for(int i = 0; i < l.length; i++) {
				if(l[i].contains("$")) {
					String currency = Main.plugin.getConfig().getString("currency");
					String[] loreParts = l[i].split("\\" + currency);
					l[i] = l[i].replace(currency, "");
					if(la[i].contains("{value}")) {
						la[i] = la[i].replace("{value}", loreParts[1]);
						la[i] = ChatColor.translateAlternateColorCodes('&', la[i]);
						if(ChatColor.stripColor(la[i]).equals(ChatColor.stripColor(l[i]))) {
							loreParts[1] = loreParts[1].replaceAll(",", "");
							if(isInt(loreParts[1])) {
								ItemStack pitem = p.getItemInHand();
								Main.econ.depositPlayer(p, Double.parseDouble(loreParts[1]));
								if(Main.plugin.getConfig().getString("deposit").contains("{amount}")) {
									String msg = Main.plugin.getConfig().getString("deposit").replace("{amount}", currency + NumberFormat.getInstance().format(Double.parseDouble(loreParts[1])));
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
								} else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("deposit")));
								}
								if (pitem.getAmount() > 1) {
									pitem.setAmount(pitem.getAmount() - 1);
								} else {
									p.setItemInHand(new ItemStack(Material.AIR));
								}
								break;
							} else {
								break;
							}
						}
					}
				}
			}
		}
	}

}
