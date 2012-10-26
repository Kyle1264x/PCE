package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;

public class pvp extends PalCommand {
	
	public pvp(){}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("pvp")) {
			if (sender instanceof Player) {
				CustomConfig f = getConfig(((Player)sender));
				boolean pvp = f.getFC().getBoolean("players." + ((Player)sender).getName().toLowerCase() + ".pvp");
				f.getFC().set("players." + ((Player)sender).getName().toLowerCase() + ".pvp", !pvp);
				f.save();
				sendMessage(sender, ChatColor.GOLD + "PVP" + ChatColor.WHITE + ": " + (!pvp ? ChatColor.GREEN + "on" : ChatColor.RED + "off"));
				return true;
			} else {
				sendMessage(sender, ChatColor.RED + "Only players in game can use this command!");
				return true;
			}
		}
		return false;
	}

}
