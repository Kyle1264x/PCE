package me.davejavu.pce.command;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class whois extends PalCommand {
	Plugin plugin;
	public whois(){}
	public whois(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("whois")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.whois")) {
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("-getWorldDir")) {
						try {
							sendMessage(sender, "World dir: (absolute) " + Bukkit.getWorlds().get(0).getWorldFolder().getAbsolutePath() + " (canonical) " + Bukkit.getWorlds().get(0).getWorldFolder().getCanonicalPath());
							return true;
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						Player p2 = getPlayer(args[0]);
						if (p2 == null) {
							sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
							return true;
						}
						CustomConfig con = getConfig(p2);
						sendMessage(sender, ChatColor.GOLD + " WhoIs: " + ChatColor.WHITE + p2.getName());
						sendMessage(sender, ChatColor.GOLD + " - Name: " + ChatColor.WHITE + p2.getDisplayName());
						sendMessage(sender, ChatColor.GOLD + " - Health: " + ChatColor.WHITE + p2.getHealth() + "/20");
						sendMessage(sender, ChatColor.GOLD + " - IP: " + ChatColor.WHITE + p2.getAddress().getHostString());
						sendMessage(sender, ChatColor.GOLD + " - Location: " + ChatColor.WHITE + p2.getWorld().getName() + ", " + String.valueOf(p2.getLocation().getX()) + ", " + String.valueOf(p2.getLocation().getY()) + ", " + String.valueOf(p2.getLocation().getZ()));
						sendMessage(sender, ChatColor.GOLD + " - EXP: " + ChatColor.WHITE + p2.getExp() + " (Level " + p2.getLevel() + ")");
						sendMessage(sender, ChatColor.GOLD + " - God: " + (con.getFC().getBoolean("god") ? ChatColor.GREEN + "true" : ChatColor.RED + "false"));
						sendMessage(sender, ChatColor.GOLD + " - Muted: " + (con.getFC().getBoolean("mute.boolean") ? ChatColor.GREEN + "true" : ChatColor.RED + "false"));
						sendMessage(sender, ChatColor.GOLD + " - Flying: " + (p2.isFlying() ? ChatColor.GREEN + "true" : ChatColor.RED + "false"));
						sendMessage(sender, ChatColor.GOLD + " - Op: " + (p2.isOp() ? ChatColor.GREEN + "true" : ChatColor.RED + "false"));
						return true;
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /whois <player>");
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
