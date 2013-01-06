package me.davejavu.pce;

import java.sql.ResultSet;

public class PalAPI {
	public static CustomConfig getPlayerConfig(String player) {
		return PalCommand.getConfig(player);
	}
	public static boolean checkBanStatus(String player) {
		ResultSet r = MySQL.getAllRows(MySQL.con, "perma_bans");
		try {
			while (r.next()) {
				if (r.getString("player").equalsIgnoreCase(player)) {
					return true;
				}
			}
		} catch (Exception e) {}
		
		ResultSet rr = MySQL.getAllRows(MySQL.con, "temp_bans");
		try {
			while (rr.next()) {
				if (rr.getString("player").equalsIgnoreCase(player)) {
					return true;
				}
			}
		} catch (Exception e) {}
		return false;
	}
	
}
