package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class tpa extends PalCommand {
	public tpa() {}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tpa")) {
			if (sender instanceof Player) {
				if (permissionCheck(sender, "PalCraftEssentials.command.tpa")) {
					if (args.length == 1) {
						Player p2 = getPlayer(args[0]);
						if (p2 == null) {
							sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
							return true;
						}
						p2.sendMessage(ChatColor.WHITE + ((Player)sender).getDisplayName() + ChatColor.GOLD + " would like to teleport to you. Either type " + ChatColor.GREEN + "/tpaccept" + ChatColor.GOLD + " or " + ChatColor.RED + "/tpdeny");
						sendMessage(sender, ChatColor.GOLD + "Request sent to " + ChatColor.WHITE + p2.getDisplayName());
						PalCraftEssentials.tpa.put(p2.getName().toLowerCase(), ((Player)sender).getName().toLowerCase() + ",1");
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + "Usage: /tpa <player>");
						return true;
					}
				} else {
					noPermission(cmd.getName(), sender);
					return true;
				}
			} else {
				sendMessage(sender, ChatColor.RED + "Only players in game can use this command!");
				return true;
			}
		}
		return false;
	}

}
