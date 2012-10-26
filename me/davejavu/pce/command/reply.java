package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class reply extends PalCommand {
	Plugin plugin;
	public reply(){}
	public reply(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("reply")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.msg")) {
				if (args.length > 0) {
					String pnameR;
					String pname;
					if (sender instanceof Player) {
						pnameR = ((Player) sender).getName();
						pname = ((Player) sender).getDisplayName();
					} else {
						pnameR = "Console";
						pname = "Console";
					}
					if (PalCraftEssentials.reply.containsKey(pnameR)) {
						String pr = PalCraftEssentials.reply.get(pnameR);
						StringBuilder sb = new StringBuilder();
						for (String s : args) {
							sb.append(s + " ");
						}
						String msg = sb.toString();
						if (permissionCheck(sender, "PalCraftEssentials.command.msg.colours")) {
							msg = msg.replaceAll("&([0-9a-rA-R])", "§$1");
						}
						if (pr.equals("Console")) {
							log.info("[MESSAGE] [" + pname + " -> console] " + msg);
							return true;
						} else {
							if (!Bukkit.getOfflinePlayer(pr).isOnline()) {
								sendMessage(sender, ChatColor.RED + "Noone to reply to!");
								return true;
							} else {
								Player p2 = Bukkit.getPlayer(pr);
								for (String s : socialspy.getSocialSpies()) {
									Player p = Bukkit.getPlayer(s);
									p.sendMessage(ChatColor.GRAY + "[" + pname + " -> " + p2.getDisplayName() + "] " + ChatColor.WHITE + msg);
								}
								log.info("[MESSAGE] " + ChatColor.GRAY + "[" + pname + " -> " + p2.getDisplayName() + "] " + ChatColor.WHITE + msg);
								p2.sendMessage(ChatColor.GRAY + "[" + pname + " -> you] " + ChatColor.WHITE + msg);
								sendMessage(sender, ChatColor.GRAY + "[you -> " + p2.getDisplayName() + "] " + ChatColor.WHITE + msg);
								return true;
							}
						}
					} else {
						sendMessage(sender, ChatColor.RED + "Noone to reply to!");
						return true;
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /reply <message>");
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
