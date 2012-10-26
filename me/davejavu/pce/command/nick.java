package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.PalCommand;

public class nick extends PalCommand {
	
	public nick(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("nick")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.nick")) {
				if (args.length > 0) {
					Player setNick = null;
					String nick = args[0].replaceAll("&([0-9a-rA-R])", "§$1");
					if (args.length == 1) {
						if (sender instanceof Player) {
							setNick = ((Player)sender);
						} else {
							sendMessage(sender, ChatColor.RED + "You can only /nick <player> <nickname/off>");
							return true;
						}
					} else {
						if (permissionCheck(sender, "PalCraftEssentials.command.nick.others")) {
							setNick = getPlayer(args[0]);
							nick = args[1].replaceAll("&([0-9a-rA-R])", "§$1");
							if (setNick == null) {
								sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
								return true;
							}
						} else {
							noPermission(cmd.getName() + " - others", sender);
							return true;
						}
					}
					if (nick.equals("off")) {
						setNick.setDisplayName(setNick.getName());
						setNick.setPlayerListName(setNick.getName());
					} else {
						setNick.setDisplayName(nick);
						setNick.setPlayerListName(nick);
					}
					if (sender instanceof Player) {
						if (((Player)sender) != setNick) {
							sendMessage(sender, ChatColor.GOLD + setNick.getName() + "'s nick set to "  + ChatColor.WHITE + setNick.getDisplayName());
						}
					} else {
						sendMessage(sender, ChatColor.GOLD + setNick.getName() + "'s nick set to "  + ChatColor.WHITE + setNick.getDisplayName());
					}
					setNick.sendMessage(ChatColor.GOLD + "Nick set to " + ChatColor.WHITE + setNick.getDisplayName());
					return true;
				} else {
					sendMessage(sender, ChatColor.RED + "Usage: /nick [player] <nick/off>");
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
