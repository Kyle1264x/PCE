package me.davejavu.pce.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;

public class blockplayer extends PalCommand {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("block")) {
			if (sender instanceof Player) {
				if (permissionCheck(sender, "PalCraftEssentials.command.block")) {
					CustomConfig pc = getConfig(((Player)sender));
					List<String> bP = pc.getFC().getStringList("block-list");
					if (bP.contains(args[0])) {
						bP.remove(args[0]);
						sendMessage(sender, ChatColor.RED + "Removed" + ChatColor.GOLD + " block for " + ChatColor.WHITE + args[0]);
						pc.getFC().set("block-list", bP);
						pc.save();
						return true;
					} else {
						bP.add(args[0]);
						sendMessage(sender, ChatColor.GREEN + "Enabled" + ChatColor.GOLD + " block for " + ChatColor.WHITE + args[0]);
						pc.getFC().set("block-list", bP);
						pc.save();
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
