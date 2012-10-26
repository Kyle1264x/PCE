package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class tpdeny extends PalCommand {
	Plugin plugin;
	public tpdeny(){}
	public tpdeny(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tpdeny")) {
			if (sender instanceof Player) {
				if (permissionCheck(sender, "PalCraftEssentials.command.tpdeny")) {
					if (PalCraftEssentials.tpa.containsKey(((Player)sender).getName().toLowerCase())) {
						String tpTo = PalCraftEssentials.tpa.get(((Player)sender).getName().toLowerCase());
						OfflinePlayer opl = Bukkit.getOfflinePlayer(tpTo);
						if (opl.isOnline()) {
							Player pl = opl.getPlayer();
							sendMessage(sender, ChatColor.RED + "Denied " + ChatColor.GOLD + "teleport request from " + ChatColor.WHITE + pl.getDisplayName());
							pl.sendMessage(ChatColor.WHITE + ((Player)sender).getDisplayName() + ChatColor.RED + " denied " + ChatColor.GOLD + "your teleport request");
							PalCraftEssentials.tpa.remove(((Player)sender).getName().toLowerCase());
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Noone to deny a tp from!");
							return true;
						}
					} else {
						sendMessage(sender, ChatColor.RED + "Noone to deny a tp from!");
						return true;
					}
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