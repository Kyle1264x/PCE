package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.davejavu.pce.PalCommand;

public class spawnwither extends PalCommand {
	
	public spawnwither(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("spawnwither")) {
			if (sender instanceof Player) {
				int amt = 1;
				if (args.length > 0) {
					amt = Integer.parseInt(args[0]);
				}
				Player p = ((Player)sender);
				for (int i = 0; i < amt; i++) {
					p.getWorld().spawnEntity(p.getLocation(), EntityType.WITHER);
				}
				sendMessage(sender, ChatColor.GOLD + "" + amt + " wither(s) spawned.");
				return true;
			} else {
				sendMessage(sender, ChatColor.RED + "Only players in game can use this command");
				return true;
			}
		}
		return false;
	}

}
