package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.PalCommand;

public class setspawn extends PalCommand {
	public setspawn(){}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setspawn")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.setspawn")) {
				if (sender instanceof Player) {
					Location newSpawnLoc = ((Player)sender).getLocation();
					Bukkit.getWorlds().get(0).setSpawnLocation(newSpawnLoc.getBlockX(), newSpawnLoc.getBlockY(),newSpawnLoc.getBlockZ());
					sendMessage(sender, ChatColor.GOLD + "Set spawn location");
					return true;
				} else {
					sendMessage(sender, ChatColor.RED + "You need to be in game to use this command!");
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
