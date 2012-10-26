package me.davejavu.pce.command;

import java.sql.ResultSet;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.Methods;
import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class silent extends PalCommand {
	
	Plugin plugin;
	public silent(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	public silent(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("silent")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.silent")) {
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("ban")) {
						if (args.length > 1) {
							if (args[1].equalsIgnoreCase("ip")) {
								if (args[2].equalsIgnoreCase("-ip")) {
									ResultSet r = Methods.getRows(Methods.con, "ip_bans");
									boolean banned = false;
									int id = 0;
									try {
										while (r.next()) {
											if (args[3].equalsIgnoreCase(r.getString("ip"))) {
												banned = true;
												id = r.getInt("id") + 1;
											}
										}
									} catch (Exception e) {
										log.log(Level.SEVERE, "A severe error occured!");
										
									}
									
									if (!banned) {
										Bukkit.getServer().banIP(args[3]);
										Methods.insertInfo(Methods.con, "ip_bans", "`id`,`ip`,`staff`", id + ",'" + args[3] + "','" + sender.getName() + "'");
										for (Player p : Bukkit.getServer().getOnlinePlayers()) {
											if (p.hasPermission("PalCraftEssentials.command.silent")) {
													p.sendMessage(ChatColor.DARK_RED + "Silent - " + sender.getName() +  ChatColor.RED+  ": " + "Banning IP: " + ChatColor.WHITE + args[3]);
											}
										}
										return true;
									} else {
										sendMessage(sender, ChatColor.RED + "IP is already banned!");
										return true;
									}
								} else if (args[2].equalsIgnoreCase("-name")) {
									if (plugin.getConfig().contains("playerip." + args[3].toLowerCase())) {
										String ip = plugin.getConfig().getString("playerip." + args[3].toLowerCase());
										
										ResultSet r = Methods.getRows(Methods.con, "ip_bans");
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
											Methods.insertInfo(Methods.con, "ip_bans", "`id`,`ip`,`staff`", id + ",'" + ip + "','" + sender.getName() + "'");
											for (Player p : Bukkit.getServer().getOnlinePlayers()) {
												if (p.hasPermission("PalCraftEssentials.command.silent")) {
														p.sendMessage(ChatColor.DARK_RED + "Silent - " + sender.getName() +  ChatColor.RED+  ": " + "Banning IP of: " + ChatColor.WHITE + args[3] + " (" + ip + ")");
												}
											}
											return true;
										} else {
											sendMessage(sender, ChatColor.RED + "IP is already banned!");
											return true;
										}
									} else {
										sendMessage(sender, ChatColor.RED + "No IP logged for " + args[3]);
										return true;
									}
								} else {
									sendMessage(sender, ChatColor.RED + "Usage: /silent ban ip <-ip/-name> <ip/name>");
									return true;
								}
								
								
							} else if (args[1].equalsIgnoreCase("temp")) {
								ResultSet r = Methods.getRows(Methods.con, "temp_bans");
								boolean b = false;
								int id = -1;
								try{
									while(r.next()) {
										if (args[2].equalsIgnoreCase(r.getString("player"))) {
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
									for (int i = 5; i < args.length; i++) {
										a.append(args[i] + " ");
									}
									if (id == -1) {
										id = 1;
									}
									Methods.insertInfo(Methods.con, "temp_bans", "`id`,`player`,`length`,`staff`,`reason`,`when`,`date`", id + ",'" + args[2] + "','" + args[3] + " " + args[4] + "','" + sender.getName() + "','" + a.toString() + "','" + String.valueOf(System.currentTimeMillis()) + "','" + date + "'");
									ResultSet pi = Methods.getRows(Methods.con, "player_info");
									boolean cn = false;
									String prevbans = "";
									try{
										while(pi.next()) {
											if (args[2].equalsIgnoreCase(pi.getString("player"))) {
												cn = true;
												prevbans = pi.getString("bans");
											}
										}
									} catch (Exception e) {
										
									}
									if (cn) {
										String bans = prevbans + "(Temp) " + sender.getName() + " - " + a.toString() + ";";
										Methods.editRow(Methods.con, "player_info", "bans='" + bans + "'", "player='" + args[2] + "'");
										sendMessage(sender, ChatColor.RED + args[2] + " has been banned " + bans.split(";").length + " times.");
									} else {
										String bans = "(Temp) " + sender.getName() + " - " + a.toString() + ";";
										Methods.insertInfo(Methods.con, "player_info", "`player`,`warnings`,`bans`,`kicks`", "'" + args[2] + "',' ','" + bans + "',' '");
									}
									
									for (Player p : Bukkit.getServer().getOnlinePlayers()) {
										if (p.hasPermission("PalCraftEssentials.command.silent")) {
												p.sendMessage(ChatColor.DARK_RED + "Silent - " + sender.getName() +  ChatColor.RED+  ": " + "Temp-Banning " + args[2] + ": " + ChatColor.WHITE + a.toString() + "- for " + args[3] + " " + args[4]);
										}
									}
									if (Bukkit.getServer().getOfflinePlayer(args[2]).isOnline()) {
										Bukkit.getServer().getPlayer(args[2]).kickPlayer("Temp banned (" + args[3] + args[4] + ") - " + a.toString());
									}
									return true;
								}
								
							} else {
								String banReason;
								boolean global = true;
								int ar = 2;
								String p2n = "";
								if (args[0].equalsIgnoreCase("-g") || args[0].equalsIgnoreCase("-l")) {
									global = args[0].equalsIgnoreCase("-g");
									p2n = args[2];
									ar = 3;
								} else {
									p2n = args[1];
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
									if (Bukkit.getServer().getPlayer(p2n).hasPermission("PalCraftEssentials.ban.exempt")) {
										sendMessage(sender, ChatColor.RED + "You can't ban " + p2n);
										return true;
									}
								}
								ResultSet r = Methods.getRows(Methods.con, "perma_bans");
								boolean banned = false;
								int id = 0;
								try {
									while (r.next()) {
										if (p2n.equalsIgnoreCase(r.getString("player"))) {
											banned = true;
										}
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
										Methods.insertInfo(Methods.con, "perma_bans", "`id`,`player`,`reason`,`staff`,`date`", id + ",'" + playertwo.getName() + "','" + banReason + "','" + sender.getName() + "','" + date + "'");
									} else{
										plugin.reloadConfig();
										playertwo.setBanned(true);
										plugin.getConfig().set("local.bans." + p2n.toLowerCase(), banReason);
										plugin.saveConfig();
									}
									
									if (playertwo.isOnline()) {
										Bukkit.getServer().getPlayer(p2n).kickPlayer(banReason);
									}
									ResultSet pi = Methods.getRows(Methods.con, "player_info");
									
									
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
										Methods.editRow(Methods.con, "player_info", "bans='" + ban + "'", "player='" + p2n + "'");
										sendMessage(sender, ChatColor.RED + p2n + " has been banned " + ((ban.split(";").length) > 2 ? ChatColor.DARK_RED : ChatColor.RED) + (ban.split(";").length) + " times.");
									} else {
										String ban = sender.getName() + " - " + banReason + ";";
										Methods.insertInfo(Methods.con, "player_info", "`player`,`warnings`,`bans`,`kicks`", "'" + p2n + "',' ','" + ban + "',' '");
									}
									for (Player p : Bukkit.getServer().getOnlinePlayers()) {
										if (p.hasPermission("PalCraftEssentials.command.silent")) {
												p.sendMessage(ChatColor.DARK_RED + "Silent - " + sender.getName() +  ChatColor.RED+  ": " + "Banning " + p2n + ": " + ChatColor.WHITE + banReason);
										}
									}
									return true;
								}
							}
						} else {
							sendMessage(sender, ChatColor.RED + "<> = required, [] = optional");
							sendMessage(sender, ChatColor.RED + "Usage: /silent ban [-g/-l] <player> <reason>");
							sendMessage(sender, ChatColor.RED + "Usage: /silent ban ip <-ip/-name> <ip/name>");
							sendMessage(sender, ChatColor.RED + "Usage: /silent ban temp <player> <value> <sec/min/hour/day/week/month> <reason>");
							sendMessage(sender, ChatColor.RED + "Usage: /silent ban <list/reason/whobanned> <player>");
							return true;
						}
						
					} else if (args[0].equalsIgnoreCase("kick")) {
						if (args.length > 1) {
							if (args.length > 2) {
								if (Bukkit.getServer().getOfflinePlayer(args[1]).isOnline()) {
									StringBuilder reason = new StringBuilder();
									for (int i = 1; i < args.length; i++) {
										reason.append(args[i] + " ");
									}
									if (Bukkit.getServer().getPlayer(args[0]).hasPermission("PalCraftEssentials.kick.exempt")) {
										sendMessage(sender, ChatColor.RED + "You can't kick " + args[1]);
										return true;
									} else {
										ResultSet r = Methods.getRows(Methods.con, "player_info");
										boolean isInTable = false;
										String prevKicks = "";
										try{
											while(r.next()) {
												if (args[1].equalsIgnoreCase(r.getString("player"))) {
													isInTable = true;
													prevKicks = r.getString("kicks");
												}
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
										String kick = sender.getName() + " - " + reason.toString() + ";";
										Bukkit.getServer().getPlayer(args[1]).kickPlayer(reason.toString());
										for (Player p : Bukkit.getServer().getOnlinePlayers()) {
											if (p.hasPermission("PalCraftEssentials.command.silent")) {
												p.sendMessage(ChatColor.DARK_RED + "Silent - " + sender.getName() +  ChatColor.RED+  ": " + "Kicking " + args[1] + ": " + ChatColor.WHITE + reason.toString());
											}
										}
										prevKicks += kick;
										if (isInTable) {
											Methods.editRow(Methods.con, "player_kicks", "player='" + args[1] + "'","kicks='" + prevKicks + kick + "'");
											sendMessage(sender, ChatColor.RED + args[1] + " has been kicked " + (prevKicks.split(";").length > 4 ? ChatColor.DARK_RED : "") + prevKicks.split(";").length + ChatColor.RED + " time" + (prevKicks.split(";").length > 1 ? "s" : "") + ".");
										} else {
											Methods.insertInfo(Methods.con, "player_kicks", "`player`,`warnings`,`bans`,`kicks`","'" + args[1] + "',' ',' ','" + kick + "'");
										}
										return true;
									}
								} else {
									sendMessage(sender, ChatColor.RED + "Player is not online!");
								}
							} else {
								sendMessage(sender, ChatColor.RED + "Usage: /silent kick <player> <reason>");
								return true;
							}
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /silent kick <player> <reason>");
							return true;
						}
					} else if (args[0].equalsIgnoreCase("unban")){
						if (args.length == 3) {
							if (args[1].equalsIgnoreCase("player")) {
								boolean banned = false;
								ResultSet ra = Methods.getRows(Methods.con, "perma_bans");
								int id = -1;
								try {
									while (ra.next()) {
										if (args[2].equalsIgnoreCase(ra.getString("player"))) {
											id = ra.getInt("id");
											banned = true;
										}
									}
								} catch (Exception e) {}
								if (banned) {
									Bukkit.getServer().getOfflinePlayer(args[2]).setBanned(false);
									Methods.deleteRow(Methods.con, "perma_bans", id, "player = '" + args[2] + "'");
									for (Player p : Bukkit.getServer().getOnlinePlayers()) {
										if (p.hasPermission("PalCraftEssentials.command.silent")) {
												p.sendMessage(ChatColor.DARK_RED + "Silent - " + sender.getName() +  ChatColor.RED+  ": " + "Unbanning " + args[2]);
										}
									}
									return true;
								} else {
									if (Bukkit.getServer().getOfflinePlayer(args[2]).isBanned()) {
										Bukkit.getServer().getOfflinePlayer(args[2]).setBanned(false);
										Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Unbanning " + args[2]);
										plugin.reloadConfig();
										plugin.getConfig().set("local.bans." + args[2].toLowerCase(), null);
										plugin.saveConfig();
									} else {
										sendMessage(sender, ChatColor.RED + args[2] + " wasn't banned!");
										return true;
									}
								}
							} else if (args[1].equalsIgnoreCase("ip")) {
								boolean banned = false;
								ResultSet r = Methods.getRows(Methods.con, "ip_bans");
								
								try {
									while (r.next()) {
										if (r.getString("ip").equalsIgnoreCase(args[2])) {
											banned = true;
										}
									}
								} catch (Exception e) {
									
								}
								
								banned = Bukkit.getServer().getIPBans().contains(args[2]);
								if (banned) {
									Bukkit.getServer().unbanIP(args[2]);
									int id = -1;
									try{
										while(r.next()) {
											if (r.getString("ip").equalsIgnoreCase(args[2])) {
												id = r.getInt("id");
											}
										}
									} catch (Exception e) {
										
									}
									if (id != -1){
										Methods.deleteRow(Methods.con, "ip_bans", id, "ip = '" + args[2] + "'");
									}
									for (Player p : Bukkit.getServer().getOnlinePlayers()) {
										if (p.hasPermission("PalCraftEssentials.command.silent")) {
												p.sendMessage(ChatColor.DARK_RED + "Silent - " + sender.getName() +  ChatColor.RED+  ": " + "Unbanning " + args[2]);
										}
									}
								} else {
									sendMessage(sender, ChatColor.RED + args[2] + " wasn't banned!");
									return true;
								}
							} else if (args[1].equalsIgnoreCase("temp")) {
								int id = -1;
								ResultSet r = Methods.getRows(Methods.con, "temp_bans");
								
								try {
									while (r.next()) {
										if (args[2].equalsIgnoreCase(r.getString("player"))) {
											id = r.getInt("id");
										}
									}
								} catch (Exception e) {
									
								}
								if (id != -1) {
									Methods.deleteRow(Methods.con, "temp_bans", id, "player = '" + args[2] + "'");
									for (Player p : Bukkit.getServer().getOnlinePlayers()) {
										if (p.hasPermission("PalCraftEssentials.command.silent")) {
												p.sendMessage(ChatColor.DARK_RED + "Silent - " + sender.getName() +  ChatColor.RED+  ": " + "Un temp-banning " + args[2]);
										}
									}
									return true;
								} else {
									sendMessage(sender, ChatColor.RED + "Player is not temp banned!");
								}
							} else {
								sendMessage(sender, ChatColor.RED + "Usage: /silent unban ip <ip>");
								sendMessage(sender, ChatColor.RED + "Usage: /silent unban <player/temp> <player>");
								return true;
							}
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /silent unban ip <ip>");
							sendMessage(sender, ChatColor.RED + "Usage: /silent unban <player/temp> <player>");
							return true;
						}
					} else {
						sendMessage(sender, ChatColor.RED + "Usage: /silent <ban/kick/unban>");
						return true;
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /silent <ban/kick/unban>");
					return true;
				}
			} else {
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		
		
		
		
		
		if (cmd.getName().equalsIgnoreCase("pbreload")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.reload")) {
				Bukkit.getServer().getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("PalCraftEssentials"));
				Bukkit.getServer().getPluginManager().enablePlugin(Bukkit.getServer().getPluginManager().getPlugin("PalCraftEssentials"));
				sendMessage(sender, ChatColor.GOLD + "PalCraftEssentials reloaded.");
				return true;
			} else {
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		return false;
	}

}
