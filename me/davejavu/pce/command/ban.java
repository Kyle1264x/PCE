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
			return banPlayer(sender, args, false);
		}
		return false;
	}
	
	public static void banBan(String sender, String p2n, String banReason) {
		if (Bukkit.getServer().getOfflinePlayer(p2n).isOnline()) {
			if (Bukkit.getServer().getPlayer(p2n).hasPermission("PalCraftEssentials.exempt.ban")) {
				Bukkit.getPlayer(sender).sendMessage(ChatColor.RED + "You can't ban " + p2n);
				return;
			}
		}
		String banR = PalCraftListener.isBanned(Bukkit.getPlayer(p2n));
		boolean banned = false;
		if (banR.equalsIgnoreCase(" ")) {
			banned = false;
		} else {
			banned = true;
		}
		ResultSet r = MySQL.getAllRows(MySQL.con, "perma_bans");
		int id = 0;
		try {
			while (r.next()) {
				id = r.getInt("id") + 1;
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "A severe error occured!");
			
		}
		
		if (banned) {
			Bukkit.getPlayer(sender).sendMessage(ChatColor.RED + p2n + " is already banned!");
			return;
		} else {
			OfflinePlayer playertwo = Bukkit.getServer().getOfflinePlayer(p2n);
			MySQL.insertInfo(MySQL.con, "perma_bans", "`id`,`player`,`reason`,`staff`,`date`", id + ",'" + playertwo.getName() + "','" + banReason + "','" + sender + "','" + date + "'");
			if (playertwo.isOnline()) {
				Bukkit.getServer().getPlayer(p2n).kickPlayer(ChatColor.GOLD + "You were banned by " + ChatColor.RED + sender + ChatColor.GOLD + "\nReason" + ChatColor.WHITE + ": " + ChatColor.RED + banReason + ChatColor.GOLD + "\nAppeal at palcraft.com");
			}
			ResultSet pi = MySQL.getRow(MySQL.con, "player_info", "player='" + playertwo.getName() + "'");
			
			
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
				String ban = bans + sender + " - " + banReason + ";";
				MySQL.editRow(MySQL.con, "player_info", "bans='" + ban + "'", "player='" + p2n + "'");
				Bukkit.getPlayer(sender).sendMessage(ChatColor.RED + p2n + " has been banned " + ((ban.split(";").length) > 2 ? ChatColor.DARK_RED : ChatColor.RED) + (ban.split(";").length) + " times.");
			} else {
				String ban = sender + " - " + banReason + ";";
				MySQL.insertInfo(MySQL.con, "player_info", "`player`,`warnings`,`bans`", "'" + p2n + "',' ','" + ban + "'");
			}
			
			Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender + ChatColor.DARK_RED + "] " + ChatColor.RED + "Banning " + p2n + ": " + ChatColor.WHITE + banReason);
			return;
		}
	}
	
	
	public static boolean banPlayer(CommandSender sender, String[] args, boolean silent) {
		if (permissionCheck(sender, "PalCraftEssentials.command.ban")) {
			if (args.length > 1) {
				String banReason;
				if (args[0].equalsIgnoreCase("ip")) {
					sendMessage(sender, ChatColor.RED + "Please use /banip");
					return true;
				} else if (args[0].equalsIgnoreCase("temp")) {
					sendMessage(sender, ChatColor.RED + "Please use /tempban");
					return true;
				} else if (args[0].equalsIgnoreCase("list")){
					ResultSet r = MySQL.getRow(MySQL.con, "player_info", "player='" + Bukkit.getOfflinePlayer(args[1]).getName() + "'");
					boolean ad = false;
					String bn = "";
					try{
						while (r.next()) {
							if (args[1].equalsIgnoreCase(r.getString("player"))) {
								ad = true;
								bn = r.getString("bans");
							}
						}
					} catch (Exception e) {}
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
					ResultSet r = MySQL.getRow(MySQL.con, "perma_bans", "player='" + Bukkit.getOfflinePlayer(args[1]).getName() + "'");
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
						ResultSet r2 = MySQL.getRow(MySQL.con, "temp_bans", "player='" + Bukkit.getOfflinePlayer(args[1]).getName() + "'");
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
					ResultSet r = MySQL.getRow(MySQL.con, "perma_bans", "player='" + Bukkit.getOfflinePlayer(args[1]).getName() + "'");
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
						ResultSet r2 = MySQL.getRow(MySQL.con, "temp_bans", "player='" + Bukkit.getOfflinePlayer(args[1]).getName() + "'");
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
					String sa = PalCraftListener.isBanned(Bukkit.getOfflinePlayer(args[0]));
					if (!sa.equalsIgnoreCase(" ")) {
						sendMessage(sender, ChatColor.RED + args[0] + " is already banned!");
						return true;
					}
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
						if (Bukkit.getServer().getPlayer(p2n).hasPermission("PalCraftEssentials.exempt.ban")) {
							sendMessage(sender, ChatColor.RED + "You can't ban " + p2n);
							return true;
						}
					}
					String banR = PalCraftListener.isBanned(Bukkit.getOfflinePlayer(p2n));
					boolean banned = false;
					if (banR.equalsIgnoreCase(" ")) {
						banned = false;
					} else {
						banned = true;
					}
					ResultSet r = MySQL.getAllRows(MySQL.con, "perma_bans");
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
							MySQL.insertInfo(MySQL.con, "perma_bans", "`id`,`player`,`reason`,`staff`,`date`", id + ",'" + playertwo.getName() + "','" + banReason + "','" + sender.getName() + "','" + date + "'");
						} else{
							CustomConfig config = reloadConfig();
							playertwo.setBanned(true);
							config.getFC().set("local.bans." + p2n.toLowerCase(), banReason);
							saveConfig(config);
						}
						
						if (playertwo.isOnline()) {
							Bukkit.getServer().getPlayer(p2n).kickPlayer(ChatColor.GOLD + "You were banned by " + ChatColor.RED + sender.getName() + ChatColor.GOLD + "\nReason" + ChatColor.WHITE + ": " + ChatColor.RED + banReason + ChatColor.GOLD + "\nAppeal at palcraft.com");
						}
						ResultSet pi = MySQL.getRow(MySQL.con, "player_info", "player='" + playertwo.getName() + "'");
						
						
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
							MySQL.editRow(MySQL.con, "player_info", "bans='" + ban + "'", "player='" + p2n + "'");
							sendMessage(sender, ChatColor.RED + p2n + " has been banned " + ((ban.split(";").length) > 2 ? ChatColor.DARK_RED : ChatColor.RED) + (ban.split(";").length) + " times.");
						} else {
							String ban = sender.getName() + " - " + banReason + ";";
							MySQL.insertInfo(MySQL.con, "player_info", "`player`,`warnings`,`bans`", "'" + p2n + "',' ','" + ban + "'");
						}
						if (!silent) {
							Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Banning " + p2n + ": " + ChatColor.WHITE + banReason);
						}
						
						return true;
					}
				}
			} else {
				sendMessage(sender, ChatColor.RED + "<> = required, [] = optional");
				sendMessage(sender, ChatColor.RED + "Usage: /ban [-l] <player> <reason>");
				sendMessage(sender, ChatColor.RED + "Usage: /ban <list/reason/whobanned> <player>");
				return true;
			}
		} else {
			noPermission("ban", sender);
			return true;
		}
	}
	
	
}
