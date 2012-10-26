package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftListener;

public class blockcmd extends PalCommand {
	public blockcmd(){}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("blockcmd")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.blockcmd")) {
				if (args.length > 0) {
					Player player = getPlayer(args[0]);
					if (player == null) {
						sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
						return true;
					}
					CustomConfig conf = getConfig(player);
					if (conf.getFC().getBoolean("block-commands.boolean") == true) {
						conf.getFC().set("block-commands.boolean", false);
						conf.getFC().set("block-commands.time", "off");
					} else {
						conf.getFC().set("block-commands.boolean", true);
						conf.getFC().set("block-commands.time", "forever");
					}
					if (args.length > 1) {
						long tempTime = PalCraftListener.parseTimeSpec(args[1],args[2]);
						conf.getFC().set("block-commands.time", tempTime + "");
					}
					conf.save();
					sendMessage(sender, ChatColor.GOLD + "Command block for " + ChatColor.WHITE + player.getDisplayName() + ": " + (conf.getFC().getBoolean("block-commands.boolean") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled") + (args.length > 1 ? ChatColor.GOLD + " for " + ChatColor.WHITE + args[1] + " " + args[2] : ""));
					player.sendMessage(ChatColor.GOLD + "Command block" + ChatColor.WHITE + ": " + (conf.getFC().getBoolean("block-commands.boolean") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled") + (args.length > 1 ? ChatColor.GOLD + " for " + ChatColor.WHITE + args[1] + " " + args[2] : ""));
					return true;
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /freeze <player> [number] [sec/min/hour]");
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
