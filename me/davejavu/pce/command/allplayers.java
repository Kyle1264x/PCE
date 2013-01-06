package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import me.davejavu.pce.PalCommand;

public class allplayers extends PalCommand {
	
	public allplayers(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("allplayers")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.allplayers")) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("removepotions")) {
						for (Player p : Bukkit.getOnlinePlayers()) {
							for (PotionEffect pe : p.getActivePotionEffects()) {
								p.removePotionEffect(pe.getType());
							}
						}
						sendMessage(sender, ChatColor.GOLD + "All potion effects removed");
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + "Usage: /allplayers <removepotions>");
						return true;
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /allplayers <removepotions>");
					return true;
				}
			} else {
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		return false;
	}

}
