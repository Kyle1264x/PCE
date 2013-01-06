package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;

public class censor extends PalCommand {

	public censor(){}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("censor")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.censor")) {
				if (args.length > 0) {
					String player = args[0].toLowerCase();
					CustomConfig conf = getConfig(player);
					boolean censor = conf.getFC().getBoolean("censor");
					if (censor) {
						conf.getFC().set("censor", false);
					} else {
						conf.getFC().set("censor", true);
					}
					conf.save();
					sendMessage(sender, ChatColor.GOLD + "Censor for " + ChatColor.WHITE + args[0] + ": " + (!censor ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
					return true;
					
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /censor <player>");
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
