package com.scoreview;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class ConnectDB {
	private Connection conn;
	
	public ConnectDB() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		String connectionUrl = "jdbc:mysql://localhost:3306/ScoreView";
		String connectionUser = "root";
		String connectionPassword = "";
		this.conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
	}
	
	public Connection getConnection()
	{
		return this.conn;
	}
}
