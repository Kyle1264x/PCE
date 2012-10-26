package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class delwarp extends PalCommand {
	
	public delwarp(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("delwarp")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.delwarp")) {
				if (args.length > 0) {
					String folder = "./plugins/PalCraftEssentials/warps";
					CustomConfig wC = new CustomConfig(folder, args[0].toLowerCase() + ".yml");
					if (wC.getFC().contains("world")) {
						String err = PalCraftEssentials.deleteFile(folder, args[0].toLowerCase() + ".yml");
						if (!err.equalsIgnoreCase("none")) {
							sendMessage(sender, ChatColor.RED + "Could not delete warp: " + ChatColor.WHITE + err);
							return true;
						}
						sendMessage(sender, ChatColor.GOLD + "Deleted warp " + ChatColor.WHITE + args[0]);
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + "Warp does not exist!");
						return true;
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /delwarp <warp>");
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
