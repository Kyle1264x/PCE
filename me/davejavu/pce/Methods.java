package me.davejavu.pce;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Methods {
	
	//MySQL methods, cba to explain them all.
	
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
	public static void deleteRow(Connection con, String tablename, int id, String name){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		}
		try{
			Statement st = con.createStatement();
			String sql = "DELETE FROM " + tablename + " WHERE id = " + id + " AND " + name + "";
			st.executeUpdate(sql);
		}catch (Exception e){
			e.printStackTrace();
		}
		try {
			con.close();
		} catch (SQLException e) {
		}
	}
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
