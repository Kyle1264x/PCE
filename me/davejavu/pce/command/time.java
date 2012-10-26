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

public class time extends PalCommand {
	Plugin plugin;
	public time(){}
	public time (PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("time")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.time")) {
				if (args.length > 0) {
					World changeTimeIn;
					long time = 0;
					if (sender instanceof Player) {
						changeTimeIn = ((Player)sender).getWorld();
					} else {
						changeTimeIn = Bukkit.getWorlds().get(0);
					}
					if (args[0].equalsIgnoreCase("day")) {
						time = 0;
					} else if (args[0].equalsIgnoreCase("night")) {
						time = 18000;
					} else {
						sendMessage(sender, ChatColor.RED + "Usage: /time <day/night>");
						return true;
					}
					changeTimeIn.setTime(time);
					sendMessage(sender, ChatColor.GOLD + "Set time to " + ChatColor.WHITE + args[0] + ChatColor.GOLD + " in world " + ChatColor.WHITE + changeTimeIn.getName());
					return true;
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /time <day/night>");
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
