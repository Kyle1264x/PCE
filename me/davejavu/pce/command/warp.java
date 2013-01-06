package me.davejavu.pce.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class warp extends PalCommand {
	Plugin plugin;
	public warp(){}
	public warp(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("warp")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.warp")) {
				if (sender instanceof Player) {
					if (args.length > 0 && !args[0].startsWith("-page")) {
						Location loc = PalCraftEssentials.getWarp(args[0]);
						if (loc != null) {
							Player toTp = (Player) sender;
							if (args.length == 2) {
								if (permissionCheck(sender, "PalCraftEssentials.command.warp.others")) {
									toTp = getPlayer(args[1]);
									if (toTp != null) {
										sendMessage(sender, ChatColor.GOLD + "Teleported " + ChatColor.WHITE + toTp.getDisplayName() + ChatColor.GOLD + " to " + ChatColor.WHITE + args[0]);
									} else {
										sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
										return true;
									}
								} else {
									noPermission(cmd.getName() + " - others", sender);
									return true;
								}
								
							}
							toTp.teleport(loc);
							sendMessage(sender, ChatColor.GOLD + "Teleported to warp " + ChatColor.WHITE + args[0]);
							return true;
						} else {
							sendMessage(sender, ChatColor.RED + "'" + args[0] + "' does not exist!");
							return true;
						}
					} else {
						if (permissionCheck(sender, "PalCraftEssentials.command.warp.list")) {
							int page = 1;
							if (args.length > 0) {
								String pg = args[0].replace("-page", "");
								page = Integer.parseInt(pg);
							}
							List<String> warps = this.listWarps(page);
							StringBuilder list = null;
							for (String w : warps) {
								if (list == null) {
									list = new StringBuilder();
									list.append(ChatColor.YELLOW + w);
								} else {
									list.append(ChatColor.WHITE + ", " + ChatColor.YELLOW + w);
								}
							}
							sendMessage(sender, ChatColor.GOLD + "Warp list page " + page + "/" + ((PalCraftEssentials.listWarps().size() / 50) + 1) + ChatColor.WHITE + ": (" + PalCraftEssentials.listWarps().size() + " warps)");
							sendMessage(sender, (list == null ? ChatColor.RED + "Page does not exist" : list.toString()));
							sendMessage(sender, ChatColor.GOLD + "/warp -page# " + ChatColor.WHITE + "to display warps - # = number");
							return true;
						} else {
							noPermission(cmd.getName() + " (list)", sender);
							return true;
						}
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Only players in game can use this command!");
					return true;
				}
			} else {
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		return false;
	}
	public List<String> listWarps(int page) {
		List<String> ret = new ArrayList<String>();
		List<String> wL = PalCraftEssentials.listWarps();
		Collections.sort(wL);
		int end = (page * 50);
		int start = end - 50 + 1;
		for (int s = start; s < (end - 1); s++) {
			if (s > wL.size()) {
				break;
			}
			String w = wL.get((s - 1));
			ret.add(w);
		}
		return ret;
	}
}
