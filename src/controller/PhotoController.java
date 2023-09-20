/*
 * Advait Borkar adb222
 * Nikhil Sachdeva ns1224
 */
package controller;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;

/**
 * Handles Photo Logic
 * 
 * @author Advait Borkar
 * @author Nikhil Sachdeva
 *
 */
public class PhotoController {
	ArrayList<User> users;
	ListView<Photo> photos;
	@FXML
	private ImageView imgView;
	@FXML
	private TextField picNameTxt, capTxt, dateTakenTxt;
	@FXML
	private Button nextBtn, prevBtn, backBtn, cancelBtn, confirmBtn, tagAddBtn, tagAddBtnPremade, tagDeleteBtn,
			LogoutBtn;
	@FXML
	private TextField tagTFld, tagVFld;
	@FXML
	private ListView<Tag> tags;
	@FXML
	private ChoiceBox<Tag> premadeTags;
	private Album selectedAlbum;
	private User user;
	SimpleDateFormat dateTime = new SimpleDateFormat("MM/dd/yyyy 'at' hh:mm a");

	/**
	 * Need to initialize the users, current user, selected album, list of photos. Photo attributes are also assigned.
	 * 
	 * @param users the list of users
	 * @param photos the list of photos
	 * @param user current user logged into the photo library
	 * @param selectedAlbum current album the user has selected
	 */
	public void start(ArrayList<User> users, ListView<Photo> photos, User user, Album selectedAlbum) {
		this.users = users;
		this.photos = photos;
		this.user = user;
		this.selectedAlbum = selectedAlbum;
		Photo selectedPhoto = photos.getSelectionModel().getSelectedItem();
		imgView.setImage(selectedPhoto.getImage());
		picNameTxt.setText(selectedPhoto.getName());
		capTxt.setText(selectedPhoto.getCaption());
		dateTakenTxt.setText(dateTime.format(selectedPhoto.getDate().getTime()));
		tags.setItems(FXCollections.observableArrayList(photos.getSelectionModel().getSelectedItem().getTags()));
		tags.getSelectionModel().select(0);
		premadeTags.setItems(FXCollections.observableArrayList(user.getTags()));
		
	}

	
	public void onClickSlideshow(ActionEvent event) {
		int currIdx = photos.getSelectionModel().getSelectedIndex();
		int size = photos.getItems().size();
		
		if (event.getSource() == nextBtn) {
			currIdx = (currIdx + 1) % size;
		} else {
			currIdx = (currIdx - 1 + size) % size;
		}

		photos.getSelectionModel().select(currIdx);
		Photo selectedPhoto = photos.getSelectionModel().getSelectedItem();
		imgView.setImage(selectedPhoto.getImage());
		picNameTxt.setText(selectedPhoto.getName());
		capTxt.setText(selectedPhoto.getCaption());
		dateTakenTxt.setText(dateTime.format(selectedPhoto.getDate().getTime()));
		tags.setItems(FXCollections.observableArrayList(photos.getSelectionModel().getSelectedItem().getTags()));
		tags.getSelectionModel().select(0);
		premadeTags.setItems(FXCollections.observableArrayList(user.getTags()));
	}


	/**
	 * Handles when the user clicks the Add Tag button
	 */
	public void onClickAddCustom() {
		Photo selectedPhoto = photos.getSelectionModel().getSelectedItem();
		ArrayList<Tag> tagList = selectedPhoto.getTags();
		if (tagTFld == null || tagTFld.getText().isBlank()) {
			showAlert("Error", "Please Enter Value for Tag Type");
			return;
		}
		
		if (tagVFld == null || tagVFld.getText().isBlank()) {
			showAlert("Error", "Please Enter Value for Tag Value");
			return;
		}
		Tag selectedTag = new Tag(tagTFld.getText(), tagVFld.getText());
		
		int i = 0;
		while (i < tagList.size()) {
			if (tagList.get(i).equals(selectedTag)) {
				showAlert("Error", "Tag Already Exists. Please Add Another Tag");
				return;
			}
			i++;
		}
		
		int j = 0;
		while (j < user.getTags().size()) {
			if (user.getTags().get(j).equals(selectedTag)) {
				showAlert("Error", "Tag Already Exists. Either Use Premade Tag Or Please Add Another Custom Tag");
				return;
			}
			j++;
		}
		
		
		Alert conf = new Alert(AlertType.CONFIRMATION);
		conf.setTitle("Confirmation");
		conf.setHeaderText("Would you like to set this as the tag for this photo?");
		conf.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				user.getTags().add(selectedTag);
				selectedPhoto.getTags().add(selectedTag);
				premadeTags.getItems().add(selectedTag);
				tags.getItems().add(selectedTag);
				tags.refresh();
				tags.getSelectionModel().select(0);
				saveData(users);
				return;
			} else if (response == ButtonType.CANCEL) {
				return;
			}	
		});		
		
		
		
	}
	
	/**
	 * Handles when the user adds Premade Tag
	 */
	public void onClickAddPremade() {
		Tag selectedTag = premadeTags.getSelectionModel().getSelectedItem();
		Photo selectedPhoto = photos.getSelectionModel().getSelectedItem();
		ArrayList<Tag> tagList = selectedPhoto.getTags();
		if (selectedTag == null) {
			showAlert("Error", "Please Select Value for Tag");
			return;
		}
		
		int i = 0;
		while (i < tagList.size()) {
			if (tagList.get(i).equals(selectedTag)) {
				showAlert("Error", "Tag Already Exists. Please Add Another Tag");
				return;
			}
			i++;
		}
		
		
		Alert conf = new Alert(AlertType.CONFIRMATION);
		conf.setTitle("Confirmation");
		conf.setHeaderText("Would you like to set this as the tag for this photo?");
		conf.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				selectedPhoto.getTags().add(selectedTag);
				tags.getItems().add(selectedTag);
				tags.refresh();
				tags.getSelectionModel().select(0);
				saveData(users);
				return;
			} else if (response == ButtonType.CANCEL) {
				return;
			}	
		});		
		
		
		
	}

	/**
	 * Handles when the user clicks the Delete tag button to remove the selected tag.
	 * 
	 * @param event the event caused by the user
	 */
	public void onClickDelete(ActionEvent event) {
		
		
		Alert conf = new Alert(AlertType.CONFIRMATION);
		conf.setTitle("Confirmation");
		conf.setHeaderText("Would you like to set this as the tag for this photo?");
		conf.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				photos.getSelectionModel().getSelectedItem().getTags().remove(tags.getSelectionModel().getSelectedItem());
				tags.getItems().remove(tags.getSelectionModel().getSelectedItem());
				tags.refresh();
				tags.getSelectionModel().select(0);	
				saveData(users);
				return;
			} else if (response == ButtonType.CANCEL) {
				return;
			}	
		});		
	}

	/**
	 * Handles when the user clicks the Back button. When selected, the user is taken back to the Album List page.
	 * 
	 * @param event the event caused by the user
	 */
	public void onClickBack(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/albumList.fxml"));
			Parent parent = (Parent) loader.load();
			AlbumController controller = loader.<AlbumController>getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			controller.start(users, user, selectedAlbum);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Handles when the user clicks the Logout button to return the user to the Photo Library login page
	 * 
	 * @param event the event caused by the user
	 */
	public void onClickLogout(ActionEvent event) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginScreen.fxml"));
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
	 * Handle Alerts
	 * @author Advait Borkar
	 * @param title of error
	 * @param error message
	 */
	private static void showAlert(String title, String message) {
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle(title);
	    alert.setContentText(message);
	    alert.show();
	}
	
	private static void saveData(ArrayList<User> users) {
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