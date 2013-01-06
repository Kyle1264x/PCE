package me.davejavu.pce.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import net.minecraft.server.v1_4_6.MinecraftServer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.davejavu.pce.PalCommand;

public class setmotd extends PalCommand {
	public setmotd(){}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setmotd")) {
			if (permissionCheck(sender, "PalCraftEssentials.command.setmotd")) {
				String motd = "";
				StringBuilder s = new StringBuilder();
				for (String a : args) {
					s.append(a + " ");
				}
				motd = s.toString().replaceAll("&([0-9a-rA-R])", "§$1");
				MinecraftServer.getServer().setMotd(motd);
				Properties props = new Properties();
				File file = new File("./server.properties");
				try {
					props.load(new FileInputStream(file));
				} catch (Exception e) {
					e.printStackTrace();
				}
				props.setProperty("motd", motd);
				try {
					props.store(new FileOutputStream(file), "##PCE was here lol");
				} catch (Exception e) {
					e.printStackTrace();
				}
				sendMessage(sender, ChatColor.GOLD + "Set motd to" + ChatColor.WHITE + ": " + motd);
				return true;
			} else {
				noPermission(cmd.getName(), sender);
				return true;
			}
		}
		return false;
	}
	
}
