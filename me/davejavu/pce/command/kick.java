package me.davejavu.pce.command;

import java.sql.ResultSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.MySQL;
import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class kick extends PalCommand {
	Plugin plugin;
	public kick(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	public kick(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("kick")) {
			kickPlayer(sender, args);
		}
		return false;
	}
	
	public static boolean kickPlayer(CommandSender sender, String[] args) {
		if (permissionCheck(sender, "PalCraftEssentials.command.kick")) {
			if (args.length > 1) {
				if (args[0].equalsIgnoreCase("list")) {
					ResultSet r = MySQL.getRow(MySQL.con, "player_info", "player='" + Bukkit.getOfflinePlayer(args[1]).getName() + "'");
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
					if (isInTable && !prevKicks.equalsIgnoreCase(" ")) {
						sendMessage(sender, ChatColor.RED + args[1] + " has been kicked " + (prevKicks.split(";").length > 4 ? ChatColor.DARK_RED : "") + prevKicks.split(";").length + " time" + (prevKicks.split(";").length > 1 ? "s" : ""));
						int counter = 1;
						for (String ra : prevKicks.split(";")) {
							sendMessage(sender, ChatColor.RED + String.valueOf(counter) + ChatColor.WHITE + ": " + ra);
							counter++;
						}
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + args[1] + " hasn't been kicked before!");
						return true;
					}
				} else {
					StringBuilder reason = new StringBuilder();
					for (int i = 1; i < args.length; i++) {
						reason.append(args[i] + " ");
					}
					Player p2 = getPlayer(args[0]);
					if (p2 == null) {
						sendMessage(sender, ChatColor.RED + args[0] + " is not online!");
						return true;
					}
					if (p2.hasPermission("PalCraftEssentials.exempt.kick")) {
						sendMessage(sender, ChatColor.RED + "You can't kick " + args[0]);
						return true;
					} else {
						ResultSet r = MySQL.getRow(MySQL.con, "player_info", "player='" + Bukkit.getOfflinePlayer(args[0]).getName() + "'");
						boolean isInTable = false;
						String prevKicks = "";
						try{
							while(r.next()) {
								if (args[0].equalsIgnoreCase(r.getString("player"))) {
									isInTable = true;
									prevKicks = r.getString("kicks");
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						String kick = sender.getName() + " - " + reason.toString() + ";";
						p2.kickPlayer(ChatColor.GOLD + "You were kicked by " + ChatColor.RED + sender.getName() + ChatColor.GOLD + "\nReason" + ChatColor.WHITE + ": " + ChatColor.RED + reason.toString());
						Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Kicking " + p2.getDisplayName() + ": " + ChatColor.WHITE + reason.toString());
						prevKicks += kick;
						if (isInTable) {
							MySQL.editRow(MySQL.con, "player_info", "player='" + p2.getName() + "'","kicks='" + prevKicks + kick + "'");
							sendMessage(sender, ChatColor.RED + args[0] + " has been kicked " + (prevKicks.split(";").length > 4 ? ChatColor.DARK_RED : "") + prevKicks.split(";").length + ChatColor.RED + " time" + (prevKicks.split(";").length > 1 ? "s" : "") + ".");
						} else {
							kick.replace("'", "");
							MySQL.insertInfo(MySQL.con, "player_info", "`player`,`warnings`,`bans`,`kicks`","'" + p2.getName() + "',' ',' ','" + kick + "'");
						}
						return true;
					}
				}
			} else {
				sendMessage(sender, ChatColor.RED + "Usage: /kick <player> <reason>");
				sendMessage(sender, ChatColor.RED + "Usage: /kick <list> <player>");
				return true;
			}
		} else {
			noPermission("kick", sender);
			return true;
		}
	}

}
