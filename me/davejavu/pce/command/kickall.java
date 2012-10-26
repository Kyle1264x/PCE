package me.davejavu.pce.command;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class kickall extends PalCommand {
	Plugin plugin;
	public kickall(){}
	public kickall(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("kickall")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.kickall")) {
				String kickReason = "You have been kicked.";
				if (args.length > 0) {
					kickReason = "";
					for (String s : args) {
						kickReason += s + " ";
					}
				}
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (sender instanceof Player) {
						if (p != ((Player) sender)) {
							p.kickPlayer(kickReason);
						}
					}
				}
				sendMessage(sender, ChatColor.GOLD + "Kicked all players.");
				return true;
			} else {
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		return false;
	}
}
