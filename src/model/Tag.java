/*
 * Advait Borkar adb222
 * Nikhil Sachdeva ns1224
 */

package model;

import java.io.Serializable;

/**
 * Makes Tag class
 * @author Advait Borkar
 * @author Nikhil Sachdeva
 */
public class Tag implements Serializable {
	
	
	
	private static final long serialVersionUID = 1L;
	private String name, value;
	
	/**
	 * Creates a new tag
	 * @param name
	 * @param value
	 */
	public Tag(String name, String value) {
		this.name = name; this.value = value;
	}
	
	/**
	 * Returns name
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns value
	 * @return value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Compares if two tags have the same name,value
	 * @param tag2
	 * @return true if the two tags have the same (name, value), else false
	 */
	public boolean equals(Tag tag2) {
		return this.name.equals(tag2.getName()) && this.value.equals(tag2.getValue());
	}
	
	/**
	 * toString method
	 * @return name,value
	 */
	public String toString() {
		return name + "," + value;
	}
}