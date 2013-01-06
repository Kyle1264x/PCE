package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;

public class setspawn extends PalCommand {
	public setspawn(){}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setspawn")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.setspawn")) {
				if (sender instanceof Player) {
					Location nSL = ((Player)sender).getLocation();
					CustomConfig conf = getConfig();
					FileConfiguration fc = conf.getFC();
					fc.set("spawn.x", nSL.getX());
					fc.set("spawn.y", nSL.getY());
					fc.set("spawn.z", nSL.getZ());
					fc.set("spawn.world", nSL.getWorld().getName());
					fc.set("spawn.pitch", nSL.getPitch());
					fc.set("spawn.yaw", nSL.getYaw());
					conf.save(fc);
					Bukkit.getWorlds().get(0).setSpawnLocation(nSL.getBlockX(), nSL.getBlockY(),nSL.getBlockZ());
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
