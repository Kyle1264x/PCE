package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.PalCommand;

public class spawn extends PalCommand {
	public spawn(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("spawn")) {
			if (sender instanceof Player) {
				Location spawnLoc = Bukkit.getWorlds().get(0).getSpawnLocation();
				spawnLoc.setY(Bukkit.getWorlds().get(0).getHighestBlockYAt(spawnLoc));
				((Player)sender).teleport(spawnLoc);
				sendMessage(sender, ChatColor.GOLD + "Teleported to spawn location");
				return true;
			} else {
				sendMessage(sender, ChatColor.RED + "Only players in game can use this command");
				return true;
			}
		}
		return false;
	}

}
