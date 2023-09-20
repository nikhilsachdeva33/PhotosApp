/*
 * Advait Borkar adb222
 * Nikhil Sachdeva ns1224
 */

package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.scene.image.Image;

/**
 * Abstracts a Photo
 * @author Advait Borkar
 * @author Nikhil Sachdeva
 */
public class Photo implements Serializable {

	
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Tag> tags;
	private String name, caption;
	private NewImage image;
	private Calendar date;

	/**
	 * Constructor
	 * @param photoID
	 * @param image that can be serialized
	 * @param date Last Modified Date, however will be date the photo was taken
	 */
	public Photo(String name, NewImage image, Calendar date) {
		this.name = name;
		this.caption = "";
		this.image = image;
		this.date = date;
		this.tags = new ArrayList<Tag>();
		this.date.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * 2nd constructor
	 * @param name the name of the photo
	 * @param image java image that will be serialized
	 * @param date Last Modified Date, however will be date the photo was taken
	 */
	public Photo(String name, Image image, Calendar date) {
		this.name = name;
		this.caption = "";
		this.image = new NewImage(image);
		this.date = date;
		this.tags = new ArrayList<Tag>();
		this.date.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * Returns name
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns caption
	 * @return caption
	 */
	public String getCaption() {
		return this.caption;
	}

	/**
	 * Returns image
	 * @return image
	 */
	public Image getImage() {
		return this.image.getImage();
	}

	/**
	 * Returns ArrayList of tags
	 * @return an ArrayList of tags
	 */
	public ArrayList<Tag> getTags() {
		return this.tags;
	}

	/**
	 * Returns last modified date
	 * @return  last modified date
	 */
	public Calendar getDate() {
		return this.date;
	}

	/**
	 * Set caption
	 * @param caption new caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * Compares two photos
	 * @param photo2
	 * @return true if the photo names are equal to each other, else false
	 */
	public boolean equals(Photo photo2) {
		return this.name.equals(photo2.name);
	}
}