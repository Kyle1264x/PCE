package me.davejavu.pce;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Methods {
	public static Connection con;
	//MySQL methods, cba to explain them all.
	
	//This is the method that connects to the MySQL database,
	//All other methods need the connection to be able to get/add/change info etc
	//The most efficient way of doing so is to open a connection when the plugin is enabled, onEnable(),
	//then use it in all commands as to not create dozens upon dozens of connections,
	//Finally closing it in onDisable()
	//(thanks seru)
	public static Connection mysqlConnect(String host, String port, String database, String username, String password) {
		Connection con = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
		
	}
	
	//Inserts information into the table specified
	//here's an exampls:
	//insertInfo(con, "bans","`player`,`reason`","'davejavu','being too good at life'");
	//Please realise we use ` for columns and ' for info.
	public static void insertInfo(Connection con, String tablename, String columns, String info) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
		}
		try {
			Statement st = con.createStatement();
			@SuppressWarnings("unused")
			int val = st.executeUpdate("INSERT " + tablename + " (" + columns + ") VALUES (" + info + ")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			con.close();
		} catch (SQLException e) {
		}
	}
	
	//Returns all rows in the specified table by executing a simple
	//query
	public static ResultSet getRows(Connection con, String tablename) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		}
		ResultSet result = null;
		try{
			Statement st = con.createStatement();
			result = st.executeQuery("SELECT * FROM " + tablename);
			return result;
			
		}catch(Exception e){
		}
		try {
			con.close();
		} catch (SQLException e) {}
		return result;
	}
	
	//Deletes all rows in the specified table
	public static void deleteAllRows(Connection con, String tablename){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		}
		try{
			Statement st = con.createStatement();
			String sql = "DELETE FROM " + tablename;
			st.executeUpdate(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
		try {
			con.close();
		} catch (SQLException e) {
		}
	}
	
	//Deletes the row where id is the variable "id"
	//"other" just defines any other information, e.g could be player = 'derp'
	public static void deleteRow(Connection con, String tablename, int id, String other){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		}
		try{
			Statement st = con.createStatement();
			String sql = "DELETE FROM " + tablename + " WHERE id = " + id + " AND " + other + "";
			st.executeUpdate(sql);
		}catch (Exception e){
			e.printStackTrace();
		}
		try {
			con.close();
		} catch (SQLException e) {
		}
	}
	//Edits the specified row, I made it so "edit1" is the part that will change, and "edit2" is
	//the 'locator' as such; for example, if I wanted to change "player" to "davejavu" where "id" was "71", I would:
	//editRow(con, table, "player='davejavu'", "id=71");
	public static void editRow(Connection con, String tablename, String edit1, String edit2) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		}
		try{
			Statement st = con.createStatement();
			String sql = "UPDATE " + tablename + " SET " + edit1 + " WHERE (" + edit2 + ")";
			st.executeUpdate(sql);
		}catch (Exception e){
			e.printStackTrace();
		}
		try {
			con.close();
		} catch (SQLException e) {
		}
	}
	
	
}
