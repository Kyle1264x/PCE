package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftListener;

public class felloutworld extends PalCommand {
	
	public felloutworld(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("felloutworld")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (PalCraftListener.fOW.containsKey(player.getName().toLowerCase())) {
					PalCraftListener.stringSetInv(PalCraftListener.fOW.get(player.getName().toLowerCase()), player);
					PalCraftListener.fOW.remove(player.getName().toLowerCase());
					sendMessage(sender, ChatColor.GOLD + "Retrieved inventory!");
					return true;
				} else {
					sendMessage(sender, ChatColor.RED + "You didn't fall out the world!");
					return true;
				}
			} else {
				sendMessage(sender, ChatColor.RED + "Only players in game can use this command!");
				return true;
			}
		}
		return false;
	}

}
