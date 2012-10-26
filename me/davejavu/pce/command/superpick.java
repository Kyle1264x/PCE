package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;

public class superpick extends PalCommand {
	
	public superpick(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("superpick")) {
			if (sender instanceof Player) {
				if (permissionCheck(sender, "PalCraftEssentials.command.superpick")) {
					CustomConfig conf = getConfig(((Player)sender));
					boolean s = conf.getFC().getBoolean("superpick");
					conf.getFC().set("superpick", !s);
					conf.save();
					sendMessage(sender, ChatColor.GOLD + "SuperPick" + ChatColor.WHITE + ": " + (!s ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
					return true;

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
