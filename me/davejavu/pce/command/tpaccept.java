package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class tpaccept extends PalCommand {
	public tpaccept(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tpaccept")) {
			if (sender instanceof Player) {
				if (permissionCheck(sender, "PalCraftEssentials.command.tpaccept")) {
					if (PalCraftEssentials.tpa.containsKey(((Player)sender).getName().toLowerCase())) {
						String tT = PalCraftEssentials.tpa.get(((Player)sender).getName().toLowerCase());
						String [] tTs = tT.split(",");
						String tpTo = tTs[0];
						int x = Integer.parseInt(tTs[1]);
						if (x == 1) {
							OfflinePlayer p = Bukkit.getOfflinePlayer(tpTo);
							if (p.isOnline()) {
								Bukkit.getPlayer(tpTo).teleport(((Player)sender));
								sendMessage(sender, ChatColor.GREEN + "Accepted " + ChatColor.GOLD + "teleport request from " + ChatColor.WHITE + p.getPlayer().getDisplayName());
								PalCraftEssentials.tpa.remove(((Player)sender).getName().toLowerCase());
								Bukkit.getPlayer(tpTo).sendMessage(ChatColor.GOLD + ((Player)sender).getDisplayName() + ChatColor.GREEN + " accepted " + ChatColor.GOLD + "your teleport request");
								return true;
							} else {
								sendMessage(sender, ChatColor.RED + "Noone to accept a tp from!");
								return true;
							}
						} else {
							OfflinePlayer p = Bukkit.getOfflinePlayer(tpTo);
							if (p.isOnline()) {
								((Player)sender).teleport(p.getPlayer());
								sendMessage(sender, ChatColor.GREEN + "Accepted " + ChatColor.GOLD + "teleport request from " + ChatColor.WHITE + p.getPlayer().getDisplayName());
								PalCraftEssentials.tpa.remove(((Player)sender).getName().toLowerCase());
								Bukkit.getPlayer(tpTo).sendMessage(ChatColor.GOLD + ((Player)sender).getDisplayName() + ChatColor.GREEN + " accepted " + ChatColor.GOLD + "your teleport request");
								return true;
							} else {
								sendMessage(sender, ChatColor.RED + "Noone to accept a tp from!");
								return true;
							}
						}
					} else {
						sendMessage(sender, ChatColor.RED + "Noone to accept a tp from!");
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
