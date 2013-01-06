package me.davejavu.pce.command;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.davejavu.pce.MySQL;
import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftListener;

public class tempban extends PalCommand {
	
	public tempban(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tempban")) {
			tempBanPlayer(sender, args);
		}
		return false;
	}

	public static boolean tempBanPlayer(CommandSender sender, String[] args) {
		if (permissionCheck(sender, "PalCraftEssentials.command.tempban")) {
			if (args.length > 3) {
				if (Bukkit.getOfflinePlayer(args[0]).isOp() || (Bukkit.getOfflinePlayer(args[0]).isOnline() && Bukkit.getOfflinePlayer(args[0]).getPlayer().hasPermission("PalCraftEssentials.exempt.ban"))) {
					sendMessage(sender, ChatColor.RED + "You can't ban " + args[0]);
					return true;
				}
				String sa = PalCraftListener.isBanned(Bukkit.getOfflinePlayer(args[0]));
				if (!sa.equalsIgnoreCase(" ")) {
					sendMessage(sender, ChatColor.RED + args[0] + " is already banned!");
					return true;
				}
				ResultSet r = MySQL.getRow(MySQL.con, "temp_bans", "player='" + Bukkit.getOfflinePlayer(args[0]).getName() + "'");
				boolean b = false;
				int id = -1;
				try{
					while(r.next()) {
						if (args[0].equalsIgnoreCase(r.getString("player"))) {
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
					for (int i = 3; i < args.length; i++) {
						a.append(args[i] + " ");
					}
					if (id == -1) {
						id = 1;
					}
					long when = System.currentTimeMillis();
					MySQL.insertInfo(MySQL.con, "temp_bans", "`id`,`player`,`length`,`staff`,`reason`,`when`,`date`", id + ",'" + args[0] + "','" + args[1] + " " + args[2] + "','" + sender.getName() + "','" + a.toString() + "','" + String.valueOf(when) + "','" + date + "'");
					ResultSet pi = MySQL.getRow(MySQL.con, "player_info", "player='" + Bukkit.getOfflinePlayer(args[0]).getName() + "'");
					boolean cn = false;
					String prevbans = "";
					try{
						while(pi.next()) {
							if (args[0].equalsIgnoreCase(pi.getString("player"))) {
								cn = true;
								prevbans = pi.getString("bans");
							}
						}
					} catch (Exception e) {
						
					}
					if (cn) {
						String bans = prevbans + "(Temp) " + sender.getName() + " - " + a.toString() + ";";
						MySQL.editRow(MySQL.con, "player_info", "bans='" + bans + "'", "player='" + args[0] + "'");
						sendMessage(sender, ChatColor.RED + args[0] + " has been banned " + bans.split(";").length + " times.");
					} else {
						String bans = "(Temp) " + sender.getName() + " - " + a.toString() + ";";
						MySQL.insertInfo(MySQL.con, "player_info", "`player`,`warnings`,`bans`,`kicks`", "'" + args[0] + "',' ','" + bans + "',' '");
					}
					
					
					Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Temp-Banning " + args[0] + ": " + ChatColor.WHITE + a.toString() + "- for " + args[1] + " " + args[2]);
					if (Bukkit.getServer().getOfflinePlayer(args[0]).isOnline()) {
						Bukkit.getServer().getPlayer(args[0]).kickPlayer(ChatColor.GOLD + "You were temp banned by " + ChatColor.RED + sender.getName() + ChatColor.GOLD+"\nLength" + ChatColor.WHITE + ": " + ChatColor.RED + args[1] + " " + args[2] + ChatColor.GOLD + "\nReason" + ChatColor.WHITE + ": " + ChatColor.RED + a.toString() + ChatColor.GOLD + "\nAppeal at palcraft.com\nWhen to come back" + ChatColor.WHITE + ": " + ChatColor.RED + (new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date(PalCraftListener.parseTimeSpec(args[1], args[2]) + when))));
					}
					return true;
				}
			} else {
				sendMessage(sender, ChatColor.RED + "Usage: /tempban <player> <value> <sec/min/hour/day/week/month> <reason>");
				return true;
			}
			
		} else {
			noPermission("tempban", sender);
			return true;
		}
		
	}

}
