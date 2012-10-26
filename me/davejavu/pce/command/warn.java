package me.davejavu.pce.command;

import java.sql.ResultSet;
import java.util.logging.Level;

import me.davejavu.pce.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class warn extends PalCommand {
	Plugin plugin;
	public warn() {}
	public warn(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("warn")) {
			if (permissionCheck(sender, "command.warn")) {
				if (args.length > 1) {
					if (args[0].equalsIgnoreCase("add")) {
						ResultSet r = Methods.getRows(Methods.con, "player_info");
						boolean ad = false;
						String warnings = "";
						try{
							while (r.next()) {
								if (args[1].equalsIgnoreCase(r.getString("player"))) {
									ad = true;
									warnings = r.getString("warnings");
								}
							}
						} catch (Exception e) {
							
						}
						StringBuilder wn = new StringBuilder();
						for (int i = 2; i < args.length; i++) {
							wn.append(args[i] + " ");
						}
						if (ad) {
							String warn = warnings + sender.getName() + " - " +  wn.toString() + ";";
							Methods.editRow(Methods.con, "player_info", "warnings='" + warn + "'", "player='" + args[1] + "'");
							sendMessage(sender, ChatColor.RED + "Warned " + args[1] + ChatColor.DARK_RED + " (" + warn.split(";").length + ")" + ChatColor.RED + ": " + ChatColor.WHITE + wn.toString());
							if (warn.split(";").length > 4) {
								String reason = "You have 5 warnings!";
								Bukkit.getServer().broadcastMessage(ChatColor.RED + "[" + ChatColor.DARK_RED + sender.getName() + ChatColor.RED + "] Banning " + args[1] + ": " + ChatColor.WHITE + reason);
								int id = 0;
								try {
									while (r.next()) {
										id = r.getInt("id") + 1;
									}
								} catch (Exception e) {
									log.log(Level.SEVERE, "A severe error occured!");
									
								}
								Methods.insertInfo(Methods.con, "perma_bans", "`id`,`player`,`reason`,`staff`,`date`", id + ",'" + args[1] + "','" + reason + "','" + sender.getName() + "','" + date + "'");
							}
							return true;
						} else {
							String rn = wn.toString().replace("'", "").replace(";", "");
							Methods.insertInfo(Methods.con, "player_info", "`player`,`warnings`,`bans`,`kicks`", "'" + args[1] + "','" + sender.getName() + " - " +  rn + ";" + "',' ',' '");
							sendMessage(sender, ChatColor.RED + "Warned " + args[1] + ChatColor.DARK_RED + " (1)" + ChatColor.RED + ": " + ChatColor.WHITE + rn);
							return true;
						}
					} else if (args[0].equalsIgnoreCase("list")) {
						ResultSet r = Methods.getRows(Methods.con, "player_info");
						boolean ad = false;
						String wn = "";
						try{
							while (r.next()) {
								if (args[1].equalsIgnoreCase(r.getString("player"))) {
									ad = true;
									wn = r.getString("warnings");
								}
							}
						} catch (Exception e) {
						}
						if (ad && !wn.equalsIgnoreCase(" ")) {
							int c = 1;
							sendMessage(sender, ChatColor.RED + args[1] + " has " + ChatColor.DARK_RED + wn.split(";").length + ChatColor.RED + " warnings:");
							for (String a : wn.split(";")) {
								sendMessage(sender,ChatColor.RED + "" + c + ": " + a);
								c++;
							}
							
							
						} else {
							sendMessage(sender, ChatColor.RED + args[1] + " has no warnings!");
							return true;
						}
					} else if (args[0].equalsIgnoreCase("remove")) {
						ResultSet r = Methods.getRows(Methods.con, "player_info");
						boolean ad = false;
						String wn = "";
						try{
							while (r.next()) {
								if (args[1].equalsIgnoreCase(r.getString("player"))) {
									ad = true;
									wn = r.getString("warnings");
								}
							}
						} catch (Exception e) {
							
						}
						if (ad && !wn.equalsIgnoreCase(" ")) {
							if (args[2].equalsIgnoreCase("all")) {
								Methods.editRow(Methods.con, "player_info", "warnings=' '", "player='" + args[1] + "'");
								sendMessage(sender, ChatColor.GOLD + "Removed all warnings from " + ChatColor.WHITE + args[1]);
								return true;
							} else {
								int id;
								try{
									id = Integer.parseInt(args[2]);
								} catch (Exception e) {
									sendMessage(sender, ChatColor.RED + "'" + args[2] + "' isn't a number!");
									return true;
								}
								StringBuilder nw = new StringBuilder();
								for (int i = 0; i < wn.split(";").length; i++) {
									String[] w = wn.split(";");
									if (i != (id - 1)) {
										nw.append(w[i] + ";");
									}
								}
								Methods.editRow(Methods.con, "player_info", "warnings='" + nw.toString() + "'", "player='" + args[1] + "'");
								sendMessage(sender, ChatColor.GOLD + "Removed warning " + ChatColor.WHITE + id + ChatColor.GOLD + " from " + ChatColor.WHITE + args[1]);
								return true;
							}
						} else {
							sendMessage(sender, ChatColor.RED + args[1] + " has no warnings!");
							return true;
						}
					} else {
						sendMessage(sender, ChatColor.RED + "Usage: /warn add <player> <reason>");
						sendMessage(sender, ChatColor.RED + "Usage: /warn list <player>");
						sendMessage(sender, ChatColor.RED + "Usage: /warn remove <player> <id/all>");
						return true;
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /warn add <player> <reason>");
					sendMessage(sender, ChatColor.RED + "Usage: /warn list <player>");
					sendMessage(sender, ChatColor.RED + "Usage: /warn remove <player> <id/all>");
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
