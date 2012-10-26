
package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class setwarp extends PalCommand {
	Plugin plugin;
	public setwarp(){}
	public setwarp(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setwarp")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.setwarp")) {
				if (sender instanceof Player) {
					Location warpLoc = ((Player)sender).getLocation();
					if (args.length == 1) {
						if (args[0].startsWith("-page")) {
							sendMessage(sender, ChatColor.RED + "You can't start warps with -page.");
							return true;
						} else {
							String err = PalCraftEssentials.setWarp(args[0], warpLoc);
							if (err.equalsIgnoreCase("noerror")) {
								sendMessage(sender, ChatColor.GOLD + "'" + ChatColor.WHITE + args[0] + ChatColor.GOLD + "' set!");
								return true;
							} else {
								log.info("'" + err + "'");
								sendMessage(sender, ChatColor.RED + "Error: '" + ChatColor.WHITE + err + ChatColor.RED + "'");
								return true;
							}
						}
					} else {
						sendMessage(sender, ChatColor.RED + "Usage: /setwarp <name>");
						return true;
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Only players in game can use this command!");
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
