package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class tptoggle extends PalCommand {
	Plugin plugin;
	
	public tptoggle(){}
	public tptoggle(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tptoggle")) {
			if (sender instanceof Player) {
				if (permissionCheck(sender, "PalCraftEssentials.command.tptoggle")) {
					CustomConfig config = getConfig((Player)sender);
					boolean tp = config.getFC().getBoolean("tp");
					config.getFC().set("tp", !tp);
					config.save();
					sendMessage(sender, ChatColor.GOLD + "Teleport: " + (!tp ? ChatColor.GREEN + "true" : ChatColor.RED + "false"));
					return true;
				} else {
					noPermission(cmd.getName(), sender);
					return true;
				}
			} else {
				sendMessage(sender, ChatColor.RED + "Only players in game can use this command");
				return true;
			}
		}
		return false;
	}

}
