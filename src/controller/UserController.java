/*
 * Advait Borkar adb222
 * Nikhil Sachdeva ns1224
 */
package controller;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Album;
import model.User;

/**
 * Handles User Dashboard Logic
 * @author Advait Borkar
 * @author Nikhil Sachdeva
 *
 */
public class UserController {
	@FXML
	private Button albumOpenBtn, albumAddBtn, albumDeleteBtn, albumRenameBtn, photosSearchBtn,
			cancelBtn, confirmBtn, logOutBtn, actionBtn;
	@FXML
	private TextField albumFld;
	@FXML
	private ListView<Album> albums;
	private ArrayList<User> users;
	private User u;

	@FXML
	private Label Username;

	public boolean rename = false;
	public String aName;

	/**
	 * Initialize User 
	 * 
	 * @param user
	 *            takes the current user.
	 * @param users
	 *            takes the user list.
	 */
	public void start(User user, ArrayList<User> users) {
		this.u = user;
		this.users = users;
		albums.setItems(FXCollections.observableArrayList(user.getAlbums()));
		albums.getSelectionModel().select(0);
		Username.setText("Welcome " + user.getUsername().toString().toUpperCase());
	}


	
	/**
	 * Handle Log Out
	 * 
	 * @param event
	 */
	public void onClickLogout(ActionEvent event) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photo_login.fxml"));
			Parent parent = (Parent) loader.load();
			LoginController controller = loader.<LoginController>getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			controller.start(stage);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * Handles Add
	 */
	public void onClickAdd() {
		
		if (albumFld.getText().isBlank()) {
			showAlert("Error", "No Username Detected. Please Try Again With A New Username");
			return;
		}
		
		ObservableList<Album> albumList = albums.getItems();
		
		// check if name already exists
				int i = 0;
				while (i < albumList.size()) {
					if (albumList.get(i).getName().equals(albumFld.getText())) {
						showAlert("Error", "Name Already Exists. Please Try Again With A New Name");
						return;
					}
					i++;
				}
		
		Alert conf = new Alert(AlertType.CONFIRMATION);
		conf.setTitle("Confirmation");
		conf.setHeaderText("Would you like to add the album?");
		conf.showAndWait().ifPresent(response -> {
			
			if (response == ButtonType.OK) {
				Album newAlbum = new Album(albumFld.getText());
				u.getAlbums().add(newAlbum);
				albums.getItems().add(newAlbum);
				albums.getSelectionModel().select(newAlbum);
				albums.refresh();
				saveData(users);
				albumFld.clear();
			} else if (response == ButtonType.CANCEL) {
				return;
			}	
		});		
	}

	/**
	 * Handles the Open album button 
	 * @param event
	 */
	public void onClickOpen(ActionEvent event) {
		Album selectedAlbum = albums.getSelectionModel().getSelectedItem();

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/albumList.fxml"));
			Parent parent = (Parent) loader.load();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			AlbumController ac = loader.<AlbumController>getController();
			ac.start(users, u, selectedAlbum);
			stage.setScene(scene);
			stage.show();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Handles Delete
	 */
	public void onClickDelete() {
		Album album = albums.getSelectionModel().getSelectedItem();


		Alert conf = new Alert(AlertType.CONFIRMATION);
		conf.setTitle("Confirmation");
		conf.setHeaderText("Would you like to delete the album?");
		conf.showAndWait().ifPresent(response -> {
			
			if (response == ButtonType.OK) {
				u.getAlbums().remove(album);
				albums.getItems().remove(album);
				albums.refresh();
				saveData(users);
				return;
			} else if (response == ButtonType.CANCEL) {
				return;
			}	
		});	

	}

	/**
	 * Handles rename button
	 */
	public void onClickRename() {
		
		if (albumFld.getText().isBlank()) {
			showAlert("Error", "No Username Detected. Please Try Again With A New Username");
			return;
		}
		
		// check if there are any albums
		if (albums.getItems().size() == 0) {
			showAlert("Error", "No Albums Detected. Please Try Adding An Album Instead");
			return;
		}
		
		ObservableList<Album> albumList = albums.getItems();
		
		// check if name already exists
		int i = 0;
		while (i < albumList.size()) {
			if (albumList.get(i).getName().equals(albumFld.getText())) {
				showAlert("Error", "Name Already Exists. Please Try Again With A New Name");
				return;
			}
			i++;
		}
		
		Alert conf = new Alert(AlertType.CONFIRMATION);
		conf.setTitle("Confirmation");
		conf.setHeaderText("Would you like to rename the album?");
		conf.showAndWait().ifPresent(response -> {
			
			if (response == ButtonType.OK) {
				// name does not exist
				Album currentAlbum = albums.getSelectionModel().getSelectedItem();
				currentAlbum.setName(albumFld.getText());
				albums.refresh();
				saveData(users);
				return;
			} else if (response == ButtonType.CANCEL) {
				return;
			}	
		});		
	}

	/**
	 * Handles search button
	 * @param event
	 */
	public void onClickSearch(ActionEvent event) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/search.fxml"));
			Parent parent = (Parent) loader.load();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println(e);		}

	}
	
	private static void saveData(ArrayList<User> users) {
		try {
			FileOutputStream fos = new FileOutputStream("data/data.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(users);

			oos.close();
			fos.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private static void showAlert(String title, String message) {
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle(title);
	    alert.setContentText(message);
	    alert.show();
	}
}