/*
 * Advait Borkar adb222
 * Nikhil Sachdeva ns1224
 */
package misc;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import model.User;

/**
 * Helper Method
 * 
 * @author Advait Borkar
 * @author Nikhil Sachdeva
 */
public class CommonFunctions {
	/**
	 * Serializes User Data
	 * 
	 * @param users ArrayList of Users
	 */
	public static void saveData(ArrayList<User> users) {
		try {
			FileOutputStream fos = new FileOutputStream("data/data.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(users);

			oos.close();
			fos.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}