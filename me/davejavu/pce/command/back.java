package me.davejavu.pce.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.davejavu.pce.CustomConfig;
import me.davejavu.pce.PalCommand;

public class back extends PalCommand {
	
	public back(){}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("back")) {
			if (permissionCheck(sender,"PalCraftEssentials.command.back")) {
				if (sender instanceof Player) {
					Location loc = getBack(((Player)sender));
					if (loc != null) {
						((Player)sender).teleport(loc);
						sendMessage(sender, ChatColor.GOLD + "Teleported back.");
						return true;
					} else {
						sendMessage(sender, ChatColor.RED + "No back location set!");
						return true;
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Only players in game can use this commmand");
					return true;
				}
			} else {
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		
		return false;
	}
	
	public Location getBack(Player player) {
		CustomConfig config = getConfig(player);
		Location loc = new Location(Bukkit.getWorld(config.getFC().getString("back.world")), config.getFC().getDouble("back.x"), config.getFC().getDouble("back.y"), config.getFC().getDouble("back.z"));
		loc.setPitch(Float.parseFloat(config.getFC().getString("back.pitch")));
		loc.setYaw(Float.parseFloat(config.getFC().getString("back.yaw")));
		if (loc.getX() == 3 && loc.getY() == 3 && loc.getZ() == 3 && loc.getPitch() == 3 && loc.getYaw() == 3) {
			return null;
		} else {
			return loc;
		}
	}
	public static void setBack(Player player, Location loc) {
		CustomConfig config = getConfig(player);
		FileConfiguration con = config.getFC();
		con.set("back.world", player.getWorld().getName());
		con.set("back.x", loc.getX());
		con.set("back.y", loc.getY());
		con.set("back.z", loc.getZ());
		con.set("back.yaw", loc.getYaw());
		con.set("back.pitch", loc.getPitch());
		config.save();
	}
}
