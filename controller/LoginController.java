/*
 * Advait Borkar adb222
 * Nikhil Sachdeva ns1224
 */

package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;

/**
 * Handles Login Logic
 * 
 * @author Advait Borkar
 * @author Nikhil Sachdeva
 *
 */
public class LoginController {
	@FXML
	private Button login;
	@FXML
	private TextField username;
	ArrayList<User> listOfUsers;
	//private final String stockPath = "data/stock.dat";
	private final String dataPath = "data/data.dat";
	Boolean validUser = false;

	/**
	 * Initializes stock dataFile and user dataFile
	 * @param stage
	 */
	public void start(Stage stage) {
	}
	

	@SuppressWarnings("unchecked")
	public void onClickLogin(ActionEvent event) {

		String input = username.getText();

		// Check for valid file. If file doesn't exist, create it and add admin
		// and stock listOfUsers
		File dataFile  = new File(dataPath);
		//File stockFile = new File(stockPath);

		if (!dataFile.exists() || !dataFile.isFile()) {
			try {
				dataFile.createNewFile();
				Album stockAlbum = new Album("stock");
				String stockPhotoPath = "data/stock";
				File photoFile;
				int x = 1;
				while (x <= 5) {
					photoFile = new File(stockPhotoPath + "/img" + Integer.toString(x) + ".jpeg");

					if (photoFile != null) {
						Image image = new Image(photoFile.toURI().toString());
						String name = photoFile.getName();
						Calendar date = Calendar.getInstance();
						date.setTimeInMillis(photoFile.lastModified());
						Photo newPhoto = new Photo(name, image, date);

						stockAlbum.getPhotos().add(newPhoto);
					}
					x++;
				}

				User stock = new User("stock");
				stock.getAlbums().add(stockAlbum);
				listOfUsers = new ArrayList<User>();
				listOfUsers.add(stock);

				try {
					FileOutputStream fos = new FileOutputStream(dataPath);
					ObjectOutputStream oos = new ObjectOutputStream(fos);

					oos.writeObject(listOfUsers);

					oos.close();
					fos.close();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		// File exists, proceed to read it
		try {
			FileInputStream fis = new FileInputStream(dataPath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			listOfUsers = (ArrayList<User>) ois.readObject();
			ois.close();
			fis.close();

			User user = null;

			for (int i = 0; i < listOfUsers.size(); i++ ) {
				User curr = listOfUsers.get(i);
				if (curr.getUsername().equals(input)) {
					user = curr;

				}
			}

			if (input.equals("admin") || user != null) {
				FXMLLoader loader;
				Parent parent;
			
				if (input.equals("admin")) {
					loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
					parent = (Parent) loader.load();
					Scene scene = new Scene(parent);
					Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					AdminController ac = loader.<AdminController>getController();
					ac.start(listOfUsers);
					stage.setScene(scene);
					stage.show();
				} else {
					loader = new FXMLLoader(getClass().getResource("/view/user.fxml"));
					parent = (Parent) loader.load();
					Scene scene = new Scene(parent);
					Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					UserController uc = loader.<UserController>getController();
					if (input.equals("stock")) {
						//System.out.println("WAHOO " + user.getUsername());
						uc.start(user, listOfUsers);
					}
					
					stage.setScene(scene);
					stage.show();
				}
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("User Error");
				alert.setContentText("This User Cannot Be Found");
				alert.showAndWait();
			}
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
}