/*
 * Advait Borkar adb222
 * Nikhil Sachdeva ns1224
 */

package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Advait Borkar
 * @author Nikhil Sachedeva
 */
public class Album implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<Photo> photos;
	
	/**
	 * Makes Album
	 * @param name
	 */
	public Album(String name) {
		this.name = name;
		photos = new ArrayList<Photo>();
	}
	
	/**
	 * Gets name
	 * @return name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets ArrayList of Photos
	 * @return ArrayList of photos
	 */
	public ArrayList<Photo> getPhotos() {
		return this.photos;
	}
	
	/**
	 * Sets name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * Compares this albums name to another albums name
	 * @param album2 
	 * @return true if the album names equal each other, else false 
	 */
	public boolean equals(Album album2) {
		return name.equals(album2.name);
	}
	
	/**
	 * toString method
	 * @return name
	 */
	public String toString() {
		String display = "Name: " + name + "\nTotal Photos: " + photos.size(); 
		
		return display;
	}
}