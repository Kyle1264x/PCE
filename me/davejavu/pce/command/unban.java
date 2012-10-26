package me.davejavu.pce.command;

import java.sql.ResultSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.*;

public class unban extends PalCommand {

	
	Plugin plugin;
	public unban() {}
	public unban(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("unban")) {
			if (permissionCheck(sender, "command.unban")) {
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("player")) {
						boolean banned = false;
						ResultSet ra = Methods.getRows(Methods.con, "perma_bans");
						
						int id = -1;
						try {
							while (ra.next()) {
								if (args[1].equalsIgnoreCase(ra.getString("player"))) {
									id = ra.getInt("id");
									banned = true;
								}
							}
						} catch (Exception e) {
							
						}
						if (banned) {
							Bukkit.getServer().getOfflinePlayer(args[1]).setBanned(false);
							Methods.deleteRow(Methods.con, "perma_bans", id, "player = '" + args[1] + "'");
							Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Unbanning " + args[1]);
							return true;
						} else {
							if (Bukkit.getServer().getOfflinePlayer(args[1]).isBanned()) {
								Bukkit.getServer().getOfflinePlayer(args[1]).setBanned(false);
								Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Unbanning " + args[1]);
								CustomConfig config = reloadConfig();
								config.getFC().set("local.bans." + args[1].toLowerCase(), null);
								saveConfig(config);
							} else {
								sendMessage(sender, ChatColor.RED + args[1] + " wasn't banned!");
								return true;
							}
						}
					} else if (args[0].equalsIgnoreCase("ip")) {
						boolean banned = false;
						ResultSet r = Methods.getRows(Methods.con, "ip_bans");
						
						try {
							while (r.next()) {
								if (r.getString("ip").equalsIgnoreCase(args[1])) {
									banned = true;
								}
							}
						} catch (Exception e) {
							
						}
						
						banned = Bukkit.getServer().getIPBans().contains(args[1]);
						if (banned) {
							Bukkit.getServer().unbanIP(args[1]);
							int id = -1;
							try{
								while(r.next()) {
									if (r.getString("ip").equalsIgnoreCase(args[1])) {
										id = r.getInt("id");
									}
								}
							} catch (Exception e) {
								
							}
							if (id != -1){
								Methods.deleteRow(Methods.con, "ip_bans", id, "ip = '" + args[1] + "'");
							}
							Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Unbanning " + args[1]);
						} else {
							sendMessage(sender, ChatColor.RED + args[1] + " wasn't banned!");
							return true;
						}
					} else if (args[0].equalsIgnoreCase("temp")) {
						int id = -1;
						ResultSet r = Methods.getRows(Methods.con, "temp_bans");
						
						try {
							while (r.next()) {
								if (args[1].equalsIgnoreCase(r.getString("player"))) {
									id = r.getInt("id");
								}
							}
						} catch (Exception e) {
							
						}
						if (id != -1) {
							Methods.deleteRow(Methods.con, "temp_bans", id, "player = '" + args[1] + "'");
							Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Un temp-banning " + args[1]);
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "Player is not temp banned!");
						}
					} else {
						sendMessage(sender, ChatColor.RED + "Usage: /unban ip <ip>");
						sendMessage(sender, ChatColor.RED + "Usage: /unban <player/temp> <player>");
						return true;
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /unban ip <ip>");
					sendMessage(sender, ChatColor.RED + "Usage: /unban <player/temp> <player>");
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
