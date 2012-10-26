package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class tphere extends PalCommand {
	Plugin plugin;
	public tphere(){}
	public tphere(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tphere")) {
			if (sender instanceof Player) {
				if (permissionCheck(sender, "PalCraftEssentials.command.tphere")) {
					if (args.length == 1) {
						Player p2 = getPlayer(args[0]);
						if (p2 == null) {
							sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
							return true;
						}
						if (!getConfig(p2).getFC().getBoolean("tp")) {
							sendMessage(sender, ChatColor.RED + "'" + p2.getDisplayName() + "' has teleportation disabled.");
							return true;
						}
						p2.teleport(((Player)sender));
						sendMessage(sender, ChatColor.GOLD + "Teleported " + ChatColor.WHITE + p2.getDisplayName() + ChatColor.GOLD + " to you");
						p2.sendMessage(ChatColor.GOLD + "Teleported to " + ChatColor.WHITE + ((Player)sender).getDisplayName());
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + "Usage: /tphere <player>");
						return true;
					}
				} else {
					noPermission(cmd.getName(), sender);
					return true;
				}
			} else {
				sendMessage(sender, ChatColor.RED + "This command can only be used in game!");
				return true;
			}
		}
		return false;
	}
}
