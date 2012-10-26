package me.davejavu.pce.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.davejavu.pce.PalCommand;
import me.davejavu.pce.PalCraftEssentials;

public class speed extends PalCommand {
	Plugin plugin;
	public speed(){}
	public speed(PalCraftEssentials plugin) {
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("speed")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.speed")) {
				if (args.length > 0) {
					if (!(sender instanceof Player)) {
						if (args.length == 2) {
							Player p2 = getPlayer(args[0]);
							if (p2 == null) {
								sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
								return true;
							}
							try{
								Float.parseFloat(args[0]);
							} catch (Exception e) {
								sendMessage(sender, ChatColor.RED + "'" + args[0] + "' isn't a number!");
								return true;
							}
							setSpeed(Float.parseFloat(args[0]), p2);
						} else {
							sendMessage(sender, ChatColor.RED + "You can only use /speed <player> <speed>");
							return true;
						}
					} else {
						if (args.length > 0) {
							Player p2 = (Player)sender;
							int fi = 0;
							if (args.length == 2) {
								p2 = getPlayer(args[0]);
								fi = 1;
								if (p2 == null) {
									sendMessage(sender, ChatColor.RED + "'" + args[0] + "' is not online!");
									return true;
								}
							}
							try{
								Float.parseFloat(args[fi]);
							} catch (Exception e) {
								sendMessage(sender, ChatColor.RED + "'" + args[0] + "' isn't a number!");
								return true;
							}
							setSpeed(Float.parseFloat(args[fi]), p2);
						} else {
							sendMessage(sender, ChatColor.RED + "Usage: /speed [player] <speed>");
							return true;
						}
						
					}
				}
			} else {
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		return false;
	}
	
	public void setSpeed(float newSpeed, Player toChange) {
		toChange.setFlySpeed(getMoveSpeed(newSpeed, toChange.isFlying()));
	}
	
	private float getMoveSpeed(float userSpeed, boolean isFlying)
	{
		float speed = isFlying ? 0.1f : 0.2f;
		float maxSpeed = 1f;

		if (userSpeed < 1f)
		{
			return speed * userSpeed;
		}
		else
		{
			float ro = ((userSpeed - 1) / 9) * (maxSpeed - speed);
			return ro + speed;
		}
}
}
