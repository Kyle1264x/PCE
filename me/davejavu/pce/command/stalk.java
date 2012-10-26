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

public class stalk extends PalCommand {
	Plugin plugin;
	public stalk(){}
	public stalk(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("stalk")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (permissionCheck(sender, "PalCraftEssentials.command.stalk")) {
					if (PalCraftListener.stalk.containsKey(sender.getName())) {
						sendMessage(sender, ChatColor.GOLD + "Stopped stalking.");
						PalCraftListener.stalk.remove(sender.getName());
						return true;
					} else {
						sendMessage(sender, ChatColor.GOLD + "Scrolling through all online players. Right click to go to next, and type /stalk again to stop stalking.");
						PalCraftListener.stalk.put(sender.getName(), 0);
						player.teleport(Bukkit.getServer().getPlayer(PalCraftListener.online.get(0)));
						sendMessage(sender,ChatColor.GOLD + "Now stalking: " + ChatColor.WHITE + PalCraftListener.online.get(0));
						return true;
					}
					
				} else {
					noPermission(cmd.getName(), sender);
					return true;
				}
			} else {
				sendMessage(sender, ChatColor.RED + "Only players in game can use this command.");
				return true;
			}
		}
		return false;
	}

}
