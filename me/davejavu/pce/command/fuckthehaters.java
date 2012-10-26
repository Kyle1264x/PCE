package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.*;

public class fuckthehaters extends PalCommand {
	Plugin plugin;
	public fuckthehaters() {}
	public fuckthehaters(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("fuckthehaters")) {
			if (sender.getName().equalsIgnoreCase("davejavu")) {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (!sender.getName().equalsIgnoreCase(p.getName())) {
						if (p.getName().equalsIgnoreCase("Mr_Botox") || p.getName().equalsIgnoreCase("KillaJabbs")) {
							p.sendMessage(ChatColor.RED + "FUCK YOU DAVE HATER!");
							p.setHealth(1);
							return true;
						}
					}
				}
			}
			return true;
		}
		return false;
	}

}
