/*
 * Advait Borkar adb222
 * Nikhil Sachdeva ns1224
 */

package model;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * Make the java Image class Serializable
 * 
 * @author Advait Borkar
 * @author Nikhil Sachdeva
 */
public class NewImage implements Serializable {
	
	
	
	private static final long serialVersionUID = 1L;
	private int width, height;
	private int[][] pixels;
	
	/**
	 * Makes image serializable
	 * @param image the image to be converted
	 */
	public NewImage(Image image) {
		width = (int)image.getWidth();
		height = (int)image.getHeight();
		pixels = new int[width][height];
		
		PixelReader reader = image.getPixelReader();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pixels[x][y] = reader.getArgb(x, y);
			}
		}
			
				
	}
	
	/**
	 * Makes serialized version back to Java Image
	 * @return Image 
	 */
	public Image getImage() {
		WritableImage image = new WritableImage(width, height);
		
		PixelWriter w = image.getPixelWriter();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				w.setArgb(i, j, pixels[i][j]);
			}
		}
				
		
		return image;
	}
	
	/**
	 * Gets width
	 * @return width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets height
	 * @return  height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Gets pixels
	 * @return pixels
	 */
	public int[][] getPixels() {
		return pixels;
	}
	
	/**
	 * Check if two images are equal to each other
	 * @param image2
	 * @return true if they're equal to each other, else false
	 */
	public boolean equals(NewImage image2) {
		if (this.width != image2.getWidth() || this.height != image2.getHeight())
			return false;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (this.pixels[x][y] != image2.getPixels()[x][y]) {
					return false;
				}
					
			}
		}
		return true;
	}
	
}