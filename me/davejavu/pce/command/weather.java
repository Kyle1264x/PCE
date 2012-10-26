package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class weather extends PalCommand {
	Plugin plugin;
	public weather(){}
	public weather(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("weather")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.weather")) {
				World changeWeatherIn;
				
				if (sender instanceof Player) {
					changeWeatherIn = ((Player)sender).getWorld();
				} else {
					changeWeatherIn = Bukkit.getWorlds().get(0);
				}
				if (args[0].equalsIgnoreCase("sun")) {
					changeWeatherIn.setWeatherDuration(0);
				} else if (args[0].equalsIgnoreCase("rain")) {
					changeWeatherIn.setWeatherDuration(0);
				} else if (args[0].equalsIgnoreCase("thunder")) {
					changeWeatherIn.setThundering(true);
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /weather <sun/rain/thunder>");
					return true;
				}
				sendMessage(sender, ChatColor.GOLD + "Set weather to " + ChatColor.WHITE + args[0] + ChatColor.GOLD + " in world " + ChatColor.WHITE + changeWeatherIn.getName());
				return true;
			} else {
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		return false;
	}

}
