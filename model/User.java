/*
 * Advait Borkar
 * Nikhil Sachdeva
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User class with serializable functionality
 * @author Advait Borkar
 * @author Nikhil Sachdeva
 */
public class User implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private String username;
	private ArrayList<Album> albums;
	private ArrayList<Tag> tags;
	
	/**
	 * Creates a new user based off of username
	 * @param username
	 */
	public User(String username) {
		this.username = username;
		this.albums = new ArrayList<Album>();
		this.tags = new ArrayList<Tag>();
	}
	
	/**
	 * Get Username
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Get Albums
	 * @return albums
	 */
	public ArrayList<Album> getAlbums() {
		return albums;
	}
	
	/**
	 * Get Tags
	 * @return tags
	 */
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	/**
	 * toString method
	 * @return return username
	 */
	public String toString() {
		return this.username;
	}
	
	/**
	 * Checks if 2 usernames are the equal
	 * @param user2
	 * @return true if the usernames are the same, else false
	 */
	public boolean equals(User user2) {
		return this.username.equals(user2.username);
	}
}