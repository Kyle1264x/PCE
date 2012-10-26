package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;
import me.davejavu.pce.PalCraftListener;

public class force extends PalCommand {
	Plugin plugin;
	public force(){}
	public force(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("force")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.force")) {
				if (args.length > 1) {
					StringBuilder command = new StringBuilder();
					command.append(args[1]);
					if (args.length > 2) {
						for (int i = 2; i < args.length; i++) {
							command.append(" " + args[i]);
						}
					}
					Player p2 = getPlayer(args[0]);
					if (p2 == null) {
						sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
						return true;
					}
					String perm = "PalCraftEssentials.command." + command.toString();
					String[] cmds = PalCraftListener.commands;
					String hp = "noInput";
					for (String c : cmds) {
						if (c.equalsIgnoreCase(command.toString())) {
							if (!permissionCheck(sender, perm)) {
								hp = "false";
							}
						}
					}
					if (hp.equalsIgnoreCase("noInput")) {
						p2.performCommand(command.toString());
						sendMessage(sender, ChatColor.GOLD + "Making " + ChatColor.WHITE + p2.getDisplayName() + ChatColor.GOLD + " run" + ChatColor.WHITE + ": /" + command.toString());
						for (Player op : Bukkit.getOnlinePlayers()) {
							if (op.hasPermission("PalCraftEssentials.command.force")) {
								op.sendMessage(ChatColor.YELLOW + "[" + ChatColor.GOLD + "PCE" + ChatColor.YELLOW + "] " + ChatColor.WHITE + ((Player)sender).getDisplayName() + ChatColor.RED + " making " + ChatColor.WHITE + p2.getDisplayName() + ChatColor.RED + " run " + ChatColor.WHITE + "/" + command.toString());
							}
						}
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + "You don't have permission to run this command yourself!");
						return true;
					}
					
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /force <player> <command> [args]");
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
