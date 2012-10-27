package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftListener;

public class mute extends PalCommand {
	public mute(){}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("mute")){
			if (permissionCheck(sender, "PalCraftEssentials.command.mute")){
				if (args.length > 0 && args.length < 4){
					if (Bukkit.getServer().matchPlayer(args[0]).isEmpty()){
						sendMessage(sender,ChatColor.RED + "Player is not online!");
						return true;
					} else {
						Player p2 = getPlayer(args[0]);
						if (p2 == null) {
							sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
							return true;
						}
						if (args.length == 1){
							CustomConfig con = getConfig(p2);
							if (con.getFC().getBoolean("mute.boolean")){
								con.getFC().set("mute.boolean", false);
								con.getFC().set("mute.time", "");
								con.save();
								sendMessage(sender,ChatColor.GOLD + p2.getName() + " unmuted.");
								return true;
							}else{
								con.getFC().set("mute.boolean", true);
								con.getFC().set("mute.time", "forever");
								con.save();
								sendMessage(sender,ChatColor.GOLD + p2.getName() + " muted.");
								return true;
							}
						} else {
							if (args.length == 3){
								try{
									Integer.parseInt(args[1]);
								}catch (Exception e){
									sendMessage(sender, ChatColor.RED + "'" + args[1] + "' isn't a number!");
									return true;
								}
								if (args[2].equals("sec") || args[2].equals("min") || args[2].equals("hour")){
									long tempTime = PalCraftListener.parseTimeSpec(args[1],args[2]);
							  	    CustomConfig config = getConfig(p2);
									tempTime = System.currentTimeMillis()+tempTime;
							  	    config.getFC().set("mute.boolean", true);
									config.getFC().set("mute.time", tempTime);
									sendMessage(sender, ChatColor.GOLD + p2.getName() + " muted for " + args[1] + " " + args[2]);
									config.save();
									return true;
								}else{
									sendMessage(sender,ChatColor.RED + "Usage: /mute <player> [time] [sec/min/hour]");
									return true;
								}
							} else {
								sendMessage(sender, ChatColor.RED + "Usage: /mute <player> [time] [sec/min/hour]");
								return true;
							}
						}
					}
				}else{
					sendMessage(sender, ChatColor.RED + "Usage: /mute <player> [time] [sec/min/hour]");
					return true;
				}
			}else{
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		return false;
	}
	public static boolean isMuted(Player player) {
		return getConfig(player).getFC().getBoolean("mute.boolean");
	}
	
}
