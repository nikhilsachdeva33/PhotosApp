/*
 * Advait Borkar adb222
 * Nikhil Sachdeva ns1224
 */

package controller;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import model.User;
import javafx.scene.Node;

/**
 * Handles Admin Logic
 * 
 * @author Advait Borkar
 * @author Nikhil Sachdeva
 *
 */
public class AdminController {
	@FXML
	private Button actionTypeButton, logOutBtn, cancelBtn, confirmBtn, userCreateBtn, userDeleteBtn,
			usersListBtn;
	@FXML
	private TextField userFld;
	@FXML
	private ListView<User> users;

	/**
	 * Initializes list of users
	 * @param users is the list of users that are in the system.
	 * 
	 */
	public void start(ArrayList<User> users) {
		this.users.setItems(FXCollections.observableArrayList(users));
		this.users.getSelectionModel().select(0);
		this.users.setVisible(false);
	}


	/**
	 * Handles Delete Logic
	 */
	public void onClickDelete() {
		User u = users.getSelectionModel().getSelectedItem();
		
		Alert conf = new Alert(AlertType.CONFIRMATION);
		conf.setTitle("Confirmation");
		conf.setHeaderText("Would you like to delete this account?");
		conf.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				users.getItems().remove(u);
				users.refresh();
				saveData();
			} else if (response == ButtonType.CANCEL) {
				return;
			}	
		});	
	}

	/**
	 * handles Add Logic
	 */
	public void onClickAdd() {
		if (userFld.getText().equals("")) {
			showAlert("Error", "Username Cannot Be Empty. Please Type A Username");
			return;
		}
		User newU = new User(userFld.getText());
		
		
		ObservableList<User> userList = users.getItems();
		
		
		// check if user exists
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getUsername().equals(newU.getUsername())) {
				// show error
				showAlert("Error", "User Already Exists");
				return;
			}
		}
		
		// user does not exist, so confirm if user wants to be added
		Alert conf = new Alert(AlertType.CONFIRMATION);
		conf.setTitle("Confirmation");
		conf.setHeaderText("Would you like to create this account?");
		conf.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				users.getItems().add(newU);
				users.refresh();
				saveData();
				userFld.clear();
			} else if (response == ButtonType.CANCEL) {
				return;
			}	
		});	
	}

	/**
	 * Changes visibility
	 */
	public void onClickList() {
		users.setVisible(true);
		users.refresh();
	}


	/**
	 * Converts list view to observable list
	 */
	private void saveData() {
		try {
			FileOutputStream fos = new FileOutputStream("data/data.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(new ArrayList<>(Arrays.asList(users.getItems().toArray())));
			 
			oos.close();
			fos.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Load login screen
	 * @param event
	 */
	public void onClickLogout(ActionEvent event) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photo_login.fxml"));
			Parent parent = (Parent) loader.load();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
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