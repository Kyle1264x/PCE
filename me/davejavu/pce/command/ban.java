package me.davejavu.pce.command;

import java.sql.ResultSet;
import java.util.logging.Level;

import me.davejavu.pce.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class ban extends PalCommand {
	
	Plugin plugin;
	public ban() {}
	public ban(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ban")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.ban")) {
				if (args.length > 1) {
					String banReason;
					if (args[0].equalsIgnoreCase("ip")) {
						if (args[1].equalsIgnoreCase("-ip")) {
							ResultSet r = Methods.getRows(Methods.mysqlConnect(host, port, database, username, password), "ip_bans");
							boolean banned = false;
							int id = 0;
							try {
								while (r.next()) {
									if (args[2].equalsIgnoreCase(r.getString("ip"))) {
										banned = true;
										id = r.getInt("id") + 1;
									}
								}
							} catch (Exception e) {
								log.log(Level.SEVERE, "A severe error occured!");
								
							}
							
							if (!banned) {
								Bukkit.getServer().banIP(args[2]);
								Methods.insertInfo(Methods.mysqlConnect(host, port, database, username, password), "ip_bans", "`id`,`ip`,`staff`", id + ",'" + args[1] + "','" + sender.getName() + "'");
								Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Banning IP: " + ChatColor.WHITE + args[1]);
								return true;
							} else {
								sendMessage(sender, ChatColor.RED + "IP is already banned!");
								return true;
							}
						} else if (args[1].equalsIgnoreCase("-name")) {
							if (getConfig(Bukkit.getOfflinePlayer(args[2])).getFC().contains("ip")) {
								String ip = getConfig(Bukkit.getOfflinePlayer(args[2])).getFC().getString("playerip." + args[2].toLowerCase());
								
								ResultSet r = Methods.getRows(Methods.mysqlConnect(host, port, database, username, password), "ip_bans");
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
									Methods.insertInfo(Methods.mysqlConnect(host, port, database, username, password), "ip_bans", "`id`,`ip`,`staff`", id + ",'" + args[1] + "','" + sender.getName() + "'");
									Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Banning IP: " + ChatColor.WHITE + ip);
									return true;
								} else {
									sendMessage(sender, ChatColor.RED + "IP is already banned!");
									return true;
								}
							} else {
								sendMessage(sender, ChatColor.RED + "No IP logged for " + args[2]);
								return true;
							}
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /ban ip <-ip/-name> <ip/name>");
							return true;
						}
						
						
					} else if (args[0].equalsIgnoreCase("temp")) {
						ResultSet r = Methods.getRows(Methods.mysqlConnect(host, port, database, username, password), "temp_bans");
						boolean b = false;
						int id = -1;
						try{
							while(r.next()) {
								if (args[1].equalsIgnoreCase(r.getString("player"))) {
									b = true;
								}
								id = r.getInt("id") + 1;
							}
						} catch (Exception e) {
							
						}
						if (b) {
							sendMessage(sender, ChatColor.RED + "Player is already temp banned!");
							return true;
						} else {
							StringBuilder a = new StringBuilder();
							for (int i = 4; i < args.length; i++) {
								a.append(args[i] + " ");
							}
							if (id == -1) {
								id = 1;
							}
							Methods.insertInfo(Methods.mysqlConnect(host, port, database, username, password), "temp_bans", "`id`,`player`,`length`,`staff`,`reason`,`when`,`date`", id + ",'" + args[1] + "','" + args[2] + " " + args[3] + "','" + sender.getName() + "','" + a.toString() + "','" + String.valueOf(System.currentTimeMillis()) + "','" + date + "'");
							ResultSet pi = Methods.getRows(Methods.mysqlConnect(host, port, database, username, password), "player_info");
							boolean cn = false;
							String prevbans = "";
							try{
								while(pi.next()) {
									if (args[1].equalsIgnoreCase(pi.getString("player"))) {
										cn = true;
										prevbans = pi.getString("bans");
									}
								}
							} catch (Exception e) {
								
							}
							if (cn) {
								String bans = prevbans + "(Temp) " + sender.getName() + " - " + a.toString() + ";";
								Methods.editRow(Methods.mysqlConnect(host, port, database, username, password), "player_info", "bans='" + bans + "'", "player='" + args[1] + "'");
								sendMessage(sender, ChatColor.RED + args[1] + " has been banned " + bans.split(";").length + " times.");
							} else {
								String bans = "(Temp) " + sender.getName() + " - " + a.toString() + ";";
								Methods.insertInfo(Methods.mysqlConnect(host, port, database, username, password), "player_info", "`player`,`warnings`,`bans`,`kicks`", "'" + args[1] + "',' ','" + bans + "',' '");
							}
							
							
							Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Temp-Banning " + args[1] + ": " + ChatColor.WHITE + a.toString() + "- for " + args[2] + " " + args[3]);
							if (Bukkit.getServer().getOfflinePlayer(args[1]).isOnline()) {
								Bukkit.getServer().getPlayer(args[1]).kickPlayer("Temp banned (" + args[2] + " " + args[3] + ") - " + a.toString());
							}
							return true;
						}
						
					} else if (args[0].equalsIgnoreCase("list")){
						ResultSet r = Methods.getRows(Methods.mysqlConnect(host, port, database, username, password), "player_info");
						boolean ad = false;
						String bn = "";
						try{
							while (r.next()) {
								if (args[1].equalsIgnoreCase(r.getString("player"))) {
									ad = true;
									bn = r.getString("bans");
								}
							}
						} catch (Exception e) {
							
						}
						if (ad && !bn.equalsIgnoreCase(" ")) {
							int c = 1;
							String[] bans = bn.split(";");
							for (String a : bans) {
								sendMessage(sender, ChatColor.RED + "" +  c + ": " + a);
								c++;
							}
							sendMessage(sender, ChatColor.RED + args[1] + " has been banned " + ((c - 1) > 2 ? ChatColor.DARK_RED : ChatColor.RED) + (c - 1) + " times:");
							
							return true;
						} else {
							sendMessage(sender, "derp" + ChatColor.RED + args[1] + " hasn't been banned!");
							return true;
						}
					} else if (args[0].equalsIgnoreCase("reason")) {
						ResultSet r = Methods.getRows(Methods.mysqlConnect(host, port, database, username, password), "perma_bans");
						boolean ad = false;
						String reason = "";
						try{
							while (r.next()) {
								if (r.getString("player").equalsIgnoreCase(args[1])) {
									ad = true;
									reason = r.getString("reason");
								}
							}
						} catch (Exception e) {
							
						}
						if (!ad) {
							ResultSet r2 = Methods.getRows(Methods.mysqlConnect(host, port, database, username, password), "temp_bans");
							try{
								while (r2.next()) {
									if (r2.getString("player").equalsIgnoreCase(args[1])) {
										ad = true;
										reason = r2.getString("reason");
									}
								}
							} catch (Exception e) {
								
							}
						}
						if (!ad || reason.equalsIgnoreCase("")) {
							sendMessage(sender, ChatColor.RED + args[1] + " isn't banned!");
							return true;
						} else {
							sendMessage(sender, ChatColor.WHITE + args[1] + ChatColor.GOLD + " was banned for: " + ChatColor.WHITE + reason);
							return true;
						}
						
					} else if (args[0].equalsIgnoreCase("whobanned")) {
						ResultSet r = Methods.getRows(Methods.mysqlConnect(host, port, database, username, password), "perma_bans");
						boolean ad = false;
						String staff = "";
						try{
							while (r.next()) {
								if (r.getString("player").equalsIgnoreCase(args[1])) {
									ad = true;
									staff = r.getString("staff");
								}
							}
						} catch (Exception e) {
							
						}
						if (!ad) {
							ResultSet r2 = Methods.getRows(Methods.mysqlConnect(host, port, database, username, password), "temp_bans");
							try{
								while (r2.next()) {
									if (r2.getString("player").equalsIgnoreCase(args[1])) {
										ad = true;
										staff = r2.getString("staff");
									}
								}
							} catch (Exception e) {
								
							}
						}
						if (!ad || staff.equalsIgnoreCase("")) {
							sendMessage(sender, ChatColor.RED + args[1] + " isn't banned!");
							return true;
						} else {
							sendMessage(sender, ChatColor.WHITE + args[1] + ChatColor.GOLD + " was banned by: " + ChatColor.WHITE + staff);
							return true;
						}
					} else {
						boolean global = true;
						int ar = 1;
						String p2n = "";
						if (args[0].equalsIgnoreCase("-l")) {
							global = false;
							p2n = args[1];
							ar = 2;
						} else {
							p2n = args[0];
						}
						
						if (args.length < (ar+1)) {
							banReason = "You have been banned!";
						} else {
							StringBuilder s = new StringBuilder();
							for (int i = ar; i < args.length; i++) {
								s.append(args[i] + " ");
							}
							banReason = s.toString();
						}
						StringBuilder br = new StringBuilder();
						for (char c : banReason.toCharArray()) {
							if (Character.toString(c).equalsIgnoreCase("'")) {
								br.append("");
							} else {
								br.append(c);
							}
						}
						if (Bukkit.getServer().getOfflinePlayer(p2n).isOnline()) {
							if (Bukkit.getServer().getPlayer(p2n).hasPermission("ban.exempt")) {
								sendMessage(sender, ChatColor.RED + "You can't ban " + p2n);
								return true;
							}
						}
						String banR = PalCraftListener.isBanned(Bukkit.getPlayer(p2n));
						boolean banned = false;
						if (banR.equalsIgnoreCase(" ")) {
							banned = false;
						} else {
							banned = true;
						}
						ResultSet r = Methods.getRows(Methods.mysqlConnect(host, port, database, username, password), "perma_bans");
						int id = 0;
						try {
							while (r.next()) {
								id = r.getInt("id") + 1;
							}
						} catch (Exception e) {
							log.log(Level.SEVERE, "A severe error occured!");
							
						}
						
						if (banned) {
							sendMessage(sender, ChatColor.RED + p2n + " is already banned!");
							return true;
						} else {
							OfflinePlayer playertwo = Bukkit.getServer().getOfflinePlayer(p2n);
							if (global) {
								Methods.insertInfo(Methods.mysqlConnect(host, port, database, username, password), "perma_bans", "`id`,`player`,`reason`,`staff`,`date`", id + ",'" + playertwo.getName() + "','" + banReason + "','" + sender.getName() + "','" + date + "'");
							} else{
								CustomConfig config = reloadConfig();
								playertwo.setBanned(true);
								config.getFC().set("local.bans." + p2n.toLowerCase(), banReason);
								saveConfig(config);
							}
							
							if (playertwo.isOnline()) {
								Bukkit.getServer().getPlayer(p2n).kickPlayer(banReason);
							}
							ResultSet pi = Methods.getRows(Methods.mysqlConnect(host, port, database, username, password), "player_info");
							
							
							boolean cn = false;
							String bans = "";
							try{
								while(pi.next()) {
									if (p2n.equalsIgnoreCase(pi.getString("player"))) {
										cn = true;
										bans = pi.getString("bans");
									}
								}
							} catch (Exception e) {
								
							}
							if (cn) {
								String ban = bans + sender.getName() + " - " + banReason + ";";
								Methods.editRow(Methods.mysqlConnect(host, port, database, username, password), "player_info", "bans='" + ban + "'", "player='" + p2n + "'");
								sendMessage(sender, ChatColor.RED + p2n + " has been banned " + ((ban.split(";").length) > 2 ? ChatColor.DARK_RED : ChatColor.RED) + (ban.split(";").length) + " times.");
							} else {
								String ban = sender.getName() + " - " + banReason + ";";
								Methods.insertInfo(Methods.mysqlConnect(host, port, database, username, password), "player_info", "`player`,`warnings`,`bans`,`bans`", "'" + p2n + "',' ','" + ban + "',' '");
							}
							
							Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Banning " + p2n + ": " + ChatColor.WHITE + banReason);
							return true;
						}
					}
				} else {
					sendMessage(sender, ChatColor.RED + "<> = required, [] = optional");
					sendMessage(sender, ChatColor.RED + "Usage: /ban [-l] <player> <reason>");
					sendMessage(sender, ChatColor.RED + "Usage: /ban ip <-ip/-name> <ip/name>");
					sendMessage(sender, ChatColor.RED + "Usage: /ban temp <player> <value> <sec/min/hour/day/week/month> <reason>");
					sendMessage(sender, ChatColor.RED + "Usage: /ban <list/reason/whobanned> <player>");
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
