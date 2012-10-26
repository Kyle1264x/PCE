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

public class me extends PalCommand {
	Plugin plugin;
	public me(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	public me(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("me")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!PalCraftListener.gag.contains(player) && !mute.isMuted(player)) {
					if (args.length > 0) {
						StringBuilder msg = new StringBuilder();
						for (String a : args) {
							msg.append(a + " ");
						}
						Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "* " + ChatColor.WHITE + player.getDisplayName() + " " + ChatColor.WHITE + msg.toString());
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + "/me <message>");
						return true;
					}
				} else {
					player.sendMessage(ChatColor.RED + "No /me for you!");
					return true;
				}
			} else {
				sendMessage(sender, ChatColor.RED + "You have to be ingame to do this command");
				return true;
			}
		}
		return false;
	}

}
