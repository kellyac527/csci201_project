package ClassStructure; 

import java.sql.*;
import java.util.ArrayList;

public class DatabaseDriver {
	
	String serverConnection;
	String user = "root";
	String pwd = "root";
	
	public DatabaseDriver() {
		serverConnection = "jdbc:mysql://localhost/beacon_db";
		user = "root";
		pwd = "root";
	}
	
	// gets the userID that corresponds with that specific user 
	public Integer getUserId(String username) {
		return 0; 
	}
	
	// GET functions
	public User getUser(String username, String password) {
		try(Connection connection = DriverManager.getConnection(db, user, pwd)){
		}
		catch(SQLIntegrityConstraintViolationException e) {
		}
		catch(SQLException e)
		{

		}

	}
	
	public boolean isValidUser(String username, String password) {
		try(Connection connection = DriverManager.getConnection(db, user, pwd)){
		}
		catch(SQLIntegrityConstraintViolationException e) {
		}
		catch(SQLException e)
		{
			
		}
	}
	
	public BeaconSignal getSubBeacon(Integer disasterID) {
		try(Connection connection = DriverManager.getConnection(db, user, pwd)){
		}
		catch(SQLIntegrityConstraintViolationException e) {
		}
		catch(SQLException e)
		{
			
		}
	}
	
	public BeaconSignal getBeaconSignal(Integer postID) {
		try(Connection connection = DriverManager.getConnection(db, user, pwd)){
		}
		catch(SQLIntegrityConstraintViolationException e) {
		}
		catch(SQLException e)
		{
			
		}
		return null; 
	}
	
	// checks to see if the username exists in the db table already  
	public User isValidUser(String username) {
		return null;  
	}
	
	public SubBeacon getSubBeacon(String disasterTitle) {
		return null; 
	}
	
	// get all the subBeacons that are affiliated with said tag: ie. flood, hurricane 
	public ArrayList<SubBeacon> getSubBeaconbyTag(String tag) {
		return null; 
	}
	
	// returns all posts(BeaconSignals) affiliated with that user
	public ArrayList<BeaconSignal> getMyBeaconSignals(String username) {
		return null;
	}
	
	// array list of comments should be sorted by timestamp -- so SORT BY when retrieving comments from db
	public BeaconSignal getBeaconSignal(Integer postID) {
		return null; 
	}
		
	// ADD functions - timeStamps should be created for each object like this: LocalDateTime time = LocalDateTime.now();
	
	public void addSubBeacon(SubBeacon subBeacon) { // new forum, like "Hurricane Sandy"
		try(Connection connection = DriverManager.getConnection(serverConnection, user, pwd)){
			
			String sql = "INSERT INTO Disasters (disasterName, disasterType) VALUES ('" 
					+ subBeacon.get_disaster() + "', '" + subBeacon.get_tag() + "')";
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
		}
		catch(SQLIntegrityConstraintViolationException e) {
			// disaster has already been created with that name
			if(e.getMessage().contains("Duplicate entry")) {
				if(e.getMessage().contains("for key 'disasterName'")) {
					System.out.println("That disaster name is already taken. Please try again with a "
							+ "different disaster name.");
				}
			}
		}
		catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}
	
	public void addBeaconSignal(BeaconSignal beaconSignal) { // new post, like "Yo, I need a place to stay with my family tonight"
		try(Connection connection = DriverManager.getConnection(serverConnection, user, pwd)){
			
			// get the disasterID for the Forum (SubBeacon) where the post is being made
			SubBeacon beacon = beaconSignal.get_subBeacon();
			String sql1 = "SELECT disasterID FROM Disasters WHERE disasterName = '" + beacon.get_disaster() + "'";
			PreparedStatement ps = connection.prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();
			Integer disasterID = -1;
			rs.next();
			disasterID = rs.getInt("disasterID"); 

			// initial post score
			Integer postScore = 0;
			
			// insert new post into Posts table
			String sql2 = "INSERT INTO Posts (disasterID, userID, postScore, timeStamps, postContent) VALUES ('" 
					+ disasterID + "', '" + beaconSignal.get_userId() + "', '" + postScore + "', '" + beaconSignal.get_timestamp() + "', '" + beaconSignal.get_postBody() + "')";
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql2);
			
		}
		catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
		
	}
	
	public void addComment(Comment comment) {
		
		try(Connection connection = DriverManager.getConnection(serverConnection, user, pwd)){
			// get the postID for the post where the comment is being placed
			BeaconSignal post = comment.get_post();
			String sql1 = "SELECT postID FROM Posts WHERE postTitle = '" + post.get_postTitle() + "'";
			PreparedStatement ps = connection.prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();
			Integer post_id = -1;
			rs.next();
			post_id = rs.getInt("postID"); 
			
			// insert new comment into Comments table
			String sql2 = "INSERT INTO Comments (userID, postID, commentContent, timeStamps) VALUES (" 
					+ comment.get_author() + ", " + post_id + ", '" + comment.get_body() + "', '" + comment.get_time() + "')";
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql2);
			
		}
		catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}
	
	public void addUser(User usr) {
		
		System.out.println("username: " + usr.get_username()); 
		System.out.println("pass: " + usr.get_password()); 
		
		try(Connection connection = DriverManager.getConnection(serverConnection, user, pwd)){
			
			String sql = "INSERT INTO Users (username, password, userPoints) VALUES ('" 
						+ usr.get_username() + "', '" + usr.get_password() + "', 0)";
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
		}
		catch(SQLIntegrityConstraintViolationException e) {
			// username has already been taken
			if(e.getMessage().contains("Duplicate entry")) {
				if(e.getMessage().contains("for key 'username'")) {
					System.out.println("That username is already taken. Please try again with a "
							+ "different username.");
				}
			}
		}
		catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
		
	}	

}
