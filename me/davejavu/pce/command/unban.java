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
			return unbanPlayer(sender, args);
		}
		return false;
	}

	public static boolean unbanPlayer(CommandSender sender, String[] args) {
		if (permissionCheck(sender, "PalCraftEssentials.command.unban")) {
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("player")) {
					playerUnban(sender, args[1]);
					return true;
				} else if (args[0].equalsIgnoreCase("ip")) {
					boolean banned = false;
					ResultSet r = MySQL.getRow(MySQL.con, "ip_bans", "ip='" + args[1] + "'");
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
							MySQL.deleteRow(MySQL.con, "ip_bans", id, "ip = '" + args[1] + "'");
						}
						Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Unbanning " + args[1]);
					} else {
						sendMessage(sender, ChatColor.RED + args[1] + " wasn't banned!");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("temp")) {
					sendMessage(sender, ChatColor.RED + "Please use /unban <player>");
					return true;
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /unban ip <ip>");
					sendMessage(sender, ChatColor.RED + "Usage: /unban <player>");
					return true;
				}
			} else if (args.length == 1 && (!args[0].equalsIgnoreCase("ip"))) {
				playerUnban(sender, args[0]);
				return true;
			} else {
				sendMessage(sender, ChatColor.RED + "Usage: /unban ip <ip>");
				sendMessage(sender, ChatColor.RED + "Usage: /unban <player>");
				return true;
			}
		} else {
			noPermission("unban", sender);
			return true;
		}
		return true;
	}

	public static void playerUnban(CommandSender sender, String pl) {
		if (!PalCraftListener.isBanned(Bukkit.getOfflinePlayer(pl)).equalsIgnoreCase(" ")) {
			boolean g = false;
			int id = -1;
			ResultSet raf = MySQL.getRow(MySQL.con, "temp_bans", "player='" + Bukkit.getOfflinePlayer(pl).getName() + "'");
			try {
				while (raf.next()) {
					Bukkit.broadcastMessage("w1");
					if (pl.equalsIgnoreCase(raf.getString("player"))) {
						id = raf.getInt("id");
						g = true;
						MySQL.deleteRow(MySQL.con, "temp_bans", id, "player = '" + Bukkit.getOfflinePlayer(pl).getName() + "'");
					}
				}
			} catch (Exception e) {}
			Bukkit.getServer().getOfflinePlayer(pl).setBanned(false);
			
			if (!g) {
				ResultSet ra = MySQL.getRow(MySQL.con, "perma_bans", "player='" + Bukkit.getOfflinePlayer(pl).getName() + "'");
				try {
					while (ra.next()) {
						if (pl.equalsIgnoreCase(ra.getString("player"))) {
							id = ra.getInt("id");
							MySQL.deleteRow(MySQL.con, "perma_bans", id, "player = '" + Bukkit.getOfflinePlayer(pl).getName() + "'");
						}
					}
				} catch (Exception e) {}
				Bukkit.getServer().getOfflinePlayer(pl).setBanned(false);
				
				
			}
			Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender.getName() + ChatColor.DARK_RED + "] " + ChatColor.RED + "Unbanning " + pl);
			return;
		} else {
			sendMessage(sender, ChatColor.RED + Bukkit.getOfflinePlayer(pl).getName() + " wasn't banned!");
			return;
		}
	}

	public static void banUnban(CommandSender sender, String pl) {
		if (!PalCraftListener.isBanned(Bukkit.getOfflinePlayer(pl)).equalsIgnoreCase(" ")) {
			boolean g = false;
			int id = -1;
			ResultSet raf = MySQL.getRow(MySQL.con, "temp_bans", "player='" + Bukkit.getOfflinePlayer(pl).getName() + "'");
			sendMessage(sender, "raf");
			try {
				while (raf.next()) {
					if (pl.equalsIgnoreCase(raf.getString("player"))) {
						sendMessage(sender, "pl");
						id = raf.getInt("id");
						g = true;
						sendMessage(sender, id + " " + Bukkit.getOfflinePlayer(pl).getName() + " ");
						MySQL.deleteRow(MySQL.con, "temp_bans", id, "player='" + Bukkit.getOfflinePlayer(pl).getName() + "'");
					}
				}
			} catch (Exception e) {}
			Bukkit.getServer().getOfflinePlayer(pl).setBanned(false);
			
			if (!g) {
				sendMessage(sender, "perma");
				ResultSet ra = MySQL.getRow(MySQL.con, "perma_bans", "player='" + Bukkit.getOfflinePlayer(pl).getName() + "'");
				try {
					while (ra.next()) {
						if (pl.equalsIgnoreCase(ra.getString("player"))) {
							sendMessage(sender, "ppl");
							id = ra.getInt("id");
							sendMessage(sender, ra.getInt("id") + "   " + Bukkit.getOfflinePlayer(pl).getName());
							MySQL.deleteRow(MySQL.con, "perma_bans", ra.getInt("id"), "player = '" + Bukkit.getOfflinePlayer(pl).getName() + "'");
						}
					}
				} catch (Exception e) {}
				Bukkit.getServer().getOfflinePlayer(pl).setBanned(false);
			}
			Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + sender + ChatColor.DARK_RED + "] " + ChatColor.RED + "Unbanning " + pl);
			return;
		} else {
			sendMessage(sender, ChatColor.RED + pl + " wasn't banned!");
			return;
		}
	}

}
