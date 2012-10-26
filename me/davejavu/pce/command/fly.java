package me.davejavu.pce.command;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class fly extends PalCommand {
	Plugin plugin;
	public fly(){}
	public fly(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("fly")) {
			if (sender instanceof Player) {
				if (permissionCheck(sender, "PalCraftEssentials.command.fly")) {
					Player player = (Player) sender;
					player.setAllowFlight(!player.getAllowFlight());
					sendMessage(sender, ChatColor.GOLD + "Flying" + ChatColor.WHITE + ": " + (!player.getAllowFlight() ? ChatColor.RED + "Off" : ChatColor.GREEN + "On"));
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
