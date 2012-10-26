package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftListener;

public class freeze extends PalCommand {
	
	public freeze(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("freeze")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.freeze")) {
				if (args.length > 0) {
					Player player = getPlayer(args[0]);
					if (player == null) {
						sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
						return true;
					}
					CustomConfig conf = getConfig(player);
					boolean fr = conf.getFC().getBoolean("freeze.boolean");
					if (conf.getFC().contains("freeze.boolean")) {
						if (fr == true) {
							conf.getFC().set("freeze.boolean", false);
							conf.getFC().set("freeze.time", 0L);
						} else {
							conf.getFC().set("freeze.boolean", true);
							conf.getFC().set("freeze.time", 0L);
						}
						
					} else {
						conf.getFC().set("freeze.boolean", true);
						conf.getFC().set("freeze.time", 0L);
					}
					if (args.length > 1) {
						long tempTime = PalCraftListener.parseTimeSpec(args[1],args[2]);
						conf.getFC().set("freeze.time", tempTime);
					}
					conf.save();
					sendMessage(sender, ChatColor.GOLD + "Freeze for " + ChatColor.WHITE + player.getDisplayName() + ": " + (!fr ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled") + (args.length > 1 ? ChatColor.GOLD + " for " + ChatColor.WHITE + args[1] + " " + args[2] : ""));
					player.sendMessage(ChatColor.GOLD + "Freeze" + ChatColor.WHITE + ": " + (!fr ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled") + (args.length > 1 ? ChatColor.GOLD + " for " + ChatColor.WHITE + args[1] + " " + args[2] : ""));
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
