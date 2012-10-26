package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class msg extends PalCommand {
	Plugin plugin;
	public msg(){}
	public msg(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("msg")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.msg")) {
				if (args.length > 1) {
					Player pl = getPlayer(args[0]);
					if (pl == null) {
						sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
						return true;
					}
					String pname;
					String pnameRaw;
					StringBuilder ms = new StringBuilder();
					for (int i = 1; i < args.length; i++) {
						ms.append(args[i] + " ");
					}
					if (sender instanceof Player) {
						pname = ((Player) sender).getDisplayName();
						pnameRaw = ((Player) sender).getName();
					} else {
						pname = "Console";
						pnameRaw = "Console";
					}
					String msg = ms.toString();
					if (permissionCheck(sender, "PalCraftEssentials.command.msg.colours")) {
						msg = msg.replaceAll("&([0-9a-rA-R])", "§$1");
					}
					if (!getConfig(pl).getFC().getStringList("block-list").contains(((Player)sender).getName())) {
						pl.sendMessage(ChatColor.GRAY + "[" + pname + " -> you] " + ChatColor.WHITE + msg);
						sendMessage(sender, ChatColor.GRAY + "[You -> " + pl.getDisplayName() + "] " + ChatColor.WHITE + msg);
						for (String s : socialspy.getSocialSpies()) {
							Player p = Bukkit.getPlayer(s);
							p.sendMessage(ChatColor.GRAY + "[" + pname + " -> " + pl.getDisplayName() + "] " + ChatColor.WHITE + msg);
						}
						PalCraftEssentials.reply.put(pl.getName(), pnameRaw);
						log.info("[MESSAGE] " + ChatColor.GRAY + "[" + pname + " -> " + pl.getDisplayName() + "] " + ChatColor.WHITE + msg);
						PalCraftEssentials.reply.put(pnameRaw, pl.getName());
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + "You can't message " + pl.getDisplayName());
						return true;
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /msg <player> <message>");
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
