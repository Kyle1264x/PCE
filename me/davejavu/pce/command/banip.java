package me.davejavu.pce.command;

import java.sql.ResultSet;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.davejavu.pce.MySQL;
import me.davejavu.pce.PalCommand;

public class banip extends PalCommand {
	
	public banip(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("banip")) {
			return banIP(sender, args);
		}
		return false;
	}

	public static boolean banIP(CommandSender sender, String[] args) {
		if (permissionCheck(sender, "PalCraftEssentials.command.banip")) {
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("-name")) {
					if (getConfig(Bukkit.getOfflinePlayer(args[1]).getName().toLowerCase()).getFC().contains("ip")) {
						if (Bukkit.getOfflinePlayer(args[1]).isOnline()) {
							if (Bukkit.getPlayer(args[1]).hasPermission("PalCraftEssentials.exempt.ban")) {
								sendMessage(sender, ChatColor.RED + "You can't ban " + args[1] + "'s ip!");
								return true;
							}
						}
						
						
						String ip = getConfig(Bukkit.getOfflinePlayer(args[1]).getName().toLowerCase()).getFC().getString("ip");
						
						ResultSet r = MySQL.getRow(MySQL.con, "ip_bans", "ip='" + args[1] + "'");
						boolean banned = false;
						int id = 0;
						try {
							while (r.next()) {
								if (ip.equalsIgnoreCase(r.getString("ip"))) {
									banned = true;
									id = r.getInt("id") + 1;
								}
							}
						} catch (Exception e) {
							log.log(Level.SEVERE, "A severe error occured!");
							
						}
						
						if (!banned) {
							Bukkit.getServer().banIP(ip);
							MySQL.insertInfo(MySQL.con, "ip_bans", "`id`,`ip`,`staff`", id + ",'" + args[1] + "','" + sender.getName() + "'");
							Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Banning IP: " + ChatColor.WHITE + ip);
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "IP is already banned!");
							return true;
						}
					} else {
						sendMessage(sender, ChatColor.RED + "No IP logged for " + args[1]);
						return true;
					}
				} else {
					boolean b = true;
					try{
						if (args[0].contains(".")) {
							for (String x : args[0].split(".")) {
								Integer.parseInt(x);
							}
						}
						
					} catch (NumberFormatException nfe) {
						b = false;
					}
					if (b) {
						ResultSet r = MySQL.getRow(MySQL.con, "ip_bans", "ip='" + args[0] + "'");
						boolean banned = false;
						int id = 0;
						try {
							while (r.next()) {
								if (args[0].equalsIgnoreCase(r.getString("ip"))) {
									banned = true;
									id = r.getInt("id") + 1;
								}
							}
						} catch (Exception e) {
							log.log(Level.SEVERE, "A severe error occured!");
							
						}
						
						if (!banned) {
							Bukkit.getServer().banIP(args[0]);
							MySQL.insertInfo(MySQL.con, "ip_bans", "`id`,`ip`,`staff`", id + ",'" + args[0] + "','" + sender.getName() + "'");
							Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Banning IP: " + ChatColor.WHITE + args[0]);
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "IP is already banned!");
							return true;
						}
					} else {
						sendMessage(sender, ChatColor.RED + "Usage: /banip <ip/-name> [name]");
						return true;
					}
				}
			} else {
				sendMessage(sender, ChatColor.RED + "Usage: /banip <ip/-name> [name]");
				return true;
			}
		} else {
			noPermission("banip", sender);
			return true;
		}
	}

}
