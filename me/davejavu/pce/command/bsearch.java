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

public class bsearch extends PalCommand {
	
	public bsearch(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("bsearch")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.bsearch")) {
				if (args.length == 1) {
					
					String player = "";
					String reason = "";
					String staff = "";
					String length = "";
					boolean banned = false;
					String type = "";
					long whenUnbanned = 0L;
					
					ResultSet rr = MySQL.getRow(MySQL.con, "temp_bans", "player='" + Bukkit.getOfflinePlayer(args[0]).getName() + "'");
					try {
						while (rr.next()) {
							if (rr.getString("player").equalsIgnoreCase(args[0])) {
								player = rr.getString("player");
								type = "temp";
								reason = rr.getString("reason");
								staff = rr.getString("staff");
								banned = true;
								length = rr.getString("length");
								whenUnbanned = rr.getLong("when") + PalCraftListener.parseTimeSpec(length.split(" ")[0], length.split(" ")[1]);
							}
						}
					} catch (Exception e) {}
					
					if (!banned) {
						try {
							ResultSet r = MySQL.getRow(MySQL.con, "perma_bans", "player='" + Bukkit.getOfflinePlayer(args[0]).getName() + "'");
							while (r.next()) {
								if (r.getString("player").equalsIgnoreCase(args[0])) {
									player = r.getString("player");
									type = "permanent";
									reason = r.getString("reason");
									staff = r.getString("staff");
									banned = true;
								}
							}
						} catch (Exception e) {}
					}
					
					if (banned) {
						sendMessage(sender, ChatColor.GOLD + "Ban info for " + ChatColor.RED + player + ChatColor.WHITE + ":");
						sendMessage(sender, ChatColor.GOLD + "- Type" + ChatColor.WHITE + ": " + type);
						sendMessage(sender, ChatColor.GOLD + "- Reason" + ChatColor.WHITE + ": " + reason);
						sendMessage(sender, ChatColor.GOLD + "- Staff" + ChatColor.WHITE + ": " + staff);
						if (type.equalsIgnoreCase("temp")) {
							sendMessage(sender, ChatColor.GOLD + "- Length" + ChatColor.WHITE + ": " + length);
							sendMessage(sender, ChatColor.GOLD + "- Time to be unbanned" + ChatColor.WHITE + ": " + (new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date(whenUnbanned))));
						}
					} else {
						sendMessage(sender, ChatColor.RED + args[0] + " is not banned! Use /ban list <player> to list previous bans!");
						return true;
					}
					
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /bsearch <player>");
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
