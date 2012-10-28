package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;

public class afk extends PalCommand {
	public afk(){}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("afk")) {
			if (sender instanceof Player) {
				toggleAFK(((Player)sender));
				return true;
			} else {
				sendMessage(sender, "Only players in game can use this command");
				return true;
			}
		}
		return false;
	}
	
	public static void toggleAFK(Player player) {
		CustomConfig config = getConfig(player);
		boolean afk = config.getFC().getBoolean("afk");
		setAFK(player, !afk, true);
	}
	public static void updateActivity(Player player) {
		CustomConfig config = getConfig(player);
		boolean afk = config.getFC().getBoolean("afk");
		if (afk) {
			setAFK(player, false, true);
		}
	}
	public static void setAFK(Player player, boolean afk, boolean broadcast) {
		CustomConfig conf = getConfig(player);
		String name = getGMName(player);
		if (conf.getFC().getBoolean("afk") != afk) {
			conf.getFC().set("afk", afk);
			conf.save();
			if (broadcast) {
				Bukkit.broadcastMessage(name + ChatColor.WHITE + (afk ? " is now AFK" : " is no longer AFK"));
			}
		}
	}
}
