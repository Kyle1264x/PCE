package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.davejavu.pce.PalCommand;

public class serverinfo extends PalCommand {
	
	public serverinfo(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("serverinfo")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.serverinfo")) {
				long usedMem = (Runtime.getRuntime().totalMemory() / 1024 / 1024) - (Runtime.getRuntime().freeMemory() / 1024 / 1024);
				long freeMem = Runtime.getRuntime().freeMemory() / 1024 / 1024;
				long totalMem = Runtime.getRuntime().totalMemory() / 1024 / 1024;
				long processors = Runtime.getRuntime().availableProcessors();
				sendMessage(sender, ChatColor.GOLD + "Memory (used/total (free))" + ChatColor.WHITE + ": " + usedMem + "/" + totalMem + " (" + freeMem + ")");
				sendMessage(sender, ChatColor.GOLD + "Processors available" + ChatColor.WHITE + ": " + processors);
				sendMessage(sender, ChatColor.GOLD + "Worlds" + ChatColor.WHITE + ":");
				for (World w : Bukkit.getWorlds()) {
					sendMessage(sender, ChatColor.YELLOW + "- " + ChatColor.WHITE + w.getName() + ": " + w.getEntities().size() + " entities, " + w.getPlayers().size() + " players, " + w.getEnvironment().toString());
				}
			} else {
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		return false;
	}

}
