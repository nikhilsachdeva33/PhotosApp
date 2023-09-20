/*
 * Advait Borkar adb222
 * Nikhil Sachdeva ns1224
 */
package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import model.Album;
import model.Photo;
import model.User;
import misc.PhotoCell;

/**
 * Handles Album Logic
 * 
 * @author Advait Borkar
 * @author Nikhil Sachdeva
 *
 */

public class AlbumController {
	@FXML
	private Button photoAddBtn, photoDeleteBtn, albumMoveBtn, albumCopyBtn, recapBtn,
			cancelBtn, confirmBtn, btnType, LogoutBtn;
	@FXML
	private TextField capFld;
	@FXML
	private Label albumNameLabel, capLabel, AlbumName;
	@FXML
	private ChoiceBox<String> albumNameFld;
	@FXML
	private ListView<Photo> photos;
	private ArrayList<User> users;
	private User u;
	private Album selectedAlbum;

	/**
	 * Need to initialize the users, current user, selected album, and all photos from the selected album
	 * 
	 * @param users the list of users
	 * @param user the user that is currently logged in
	 * @param selectedAlbum the album that is currently selected.
	 */
	public void start(ArrayList<User> users, User user, Album selectedAlbum) {
		this.users = users;
		this.u = user;
		this.selectedAlbum = selectedAlbum;
		AlbumName.setText(selectedAlbum.getName());

		photos.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>() {
			@Override
			public ListCell<Photo> call(ListView<Photo> photoList) {
				return new PhotoCell();
			}
		});

		photos.setItems(FXCollections.observableArrayList(selectedAlbum.getPhotos()));
		photos.getSelectionModel().select(0);


		ArrayList<String> anames = new ArrayList<String>();
		ArrayList<Album> allAlbums = user.getAlbums();
		
		int i = 0;
		while (i < allAlbums.size()) {
			anames.add(allAlbums.get(i).getName());
			i++;
		}

		albumNameFld.setItems(FXCollections.observableArrayList(anames));
	}

	/**
	 * Handles when the user clicks the Add photo button. This is accomplished by loading the file
	 * chooser. The photo is then stored in album.
	 */
	public void onClickAdd(ActionEvent event) {
		FileChooser choose = new FileChooser();
		choose.setTitle("Choose Image");
		choose.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files", "*.bmp", "*.BMP", "*.gif", "*.GIF", "*.jpg", "*.JPG", "*.jpeg", "*.JPEG", "*.png",
						"*.PNG"),
				new ExtensionFilter("Bitmap Files", "*.bmp", "*.BMP"),
				new ExtensionFilter("GIF Files", "*.gif", "*.GIF"), new ExtensionFilter("JPEG Files", "*.jpg", "*.JPG", "*.jpeg", "*.JPEG"),
				new ExtensionFilter("PNG Files", "*.png", "*.PNG"));
		File selectedFile = choose.showOpenDialog(null);

		if (selectedFile != null) {
			Image image = new Image(selectedFile.toURI().toString());
			String name = selectedFile.getName();
			Calendar date = Calendar.getInstance();
			date.setTimeInMillis(selectedFile.lastModified());
			Photo newPhoto = new Photo(name, image, date);

			int i = 0;
			while ( i < selectedAlbum.getPhotos().size()) {
				if (selectedAlbum.getPhotos().get(i).equals(newPhoto)) {
					showAlert("Error", "Photo Name Already Exists. Please Try Again");
					return;
				}
				i++;
			}
			
			
			Alert conf = new Alert(AlertType.CONFIRMATION);
			conf.setTitle("Confirmation");
			conf.setHeaderText("Would you like to add this photo to this album?");
			conf.showAndWait().ifPresent(response -> {
				if (response == ButtonType.OK) {
					photos.getItems().add(newPhoto);
					selectedAlbum.getPhotos().add(newPhoto);
					photos.refresh();
					saveData(users);
					return;
				} else if (response == ButtonType.CANCEL) {
					return;
				}	
			});		
			
		}
	}


	/**
	 * Handles when the user clicks the delete button to delete a selected photo from current album.
	 */
	public void onClickMovePhoto(ActionEvent event) {
		String destinationAlbum = albumNameFld.getSelectionModel().getSelectedItem();
		if (destinationAlbum == null) {
			showAlert("Error", "Please Select Destination Album Before Pressing 'Move To Album. Please Try Again");
			return;
		}
		Photo toBeMoved = photos.getSelectionModel().getSelectedItem();
		
		// find album
		int i = 0;
		while (i < u.getAlbums().size()) {
			
			
			// found album
			if (u.getAlbums().get(i).getName().equals(destinationAlbum)) {
				int j = 0;
				// check if album is valid
				while (j < u.getAlbums().get(i).getPhotos().size()) {
					if (u.getAlbums().get(i).getPhotos().get(j).equals(toBeMoved)) {
						showAlert("Error", "Selected Photo Already Exists At Destination Album. Please Try Again.");
						return;
					}
					j++;
				}
				
				final int n = i;
				Alert conf = new Alert(AlertType.CONFIRMATION);
				conf.setTitle("Confirmation");
				conf.setHeaderText("Would you like to move this photo to the new album?");
				conf.showAndWait().ifPresent(response -> {
					if (response == ButtonType.OK) {
						// album is valid, so copy photo.
						u.getAlbums().get(n).getPhotos().add(toBeMoved);
						// then delete photo
						selectedAlbum.getPhotos().remove(toBeMoved);
						photos.getItems().remove(toBeMoved);
						photos.getSelectionModel().select(0);
						photos.refresh();
						saveData(users);
						return;
					} else if (response == ButtonType.CANCEL) {
						return;
					}	
				});		
				
			}
			i++;
		}
		
	}
		
	public void onClickCopyPhoto(ActionEvent event) {
		String destinationAlbum = albumNameFld.getSelectionModel().getSelectedItem();
		if (destinationAlbum == null) {
			showAlert("Error", "Please Select Destination Album Before Pressing 'Copy To Album. Please Try Again");
			return;
		}
		Photo toBeMoved = photos.getSelectionModel().getSelectedItem();
		
		
		// find album
		int i = 0;
		while (i < u.getAlbums().size()) {
			
			// found album
			if (u.getAlbums().get(i).getName().equals(destinationAlbum)) {
				int j = 0;
				// check if album is valid
				while (j < u.getAlbums().get(i).getPhotos().size()) {
					if (u.getAlbums().get(i).getPhotos().get(j).equals(toBeMoved)) {
						showAlert("Error", "Selected Photo Already Exists At Destination Album. Please Try Again.");
						return;
					}
					j++;
				}
				
				// album is valid, so copy photo.
				final int n = i;
				Alert conf = new Alert(AlertType.CONFIRMATION);
				conf.setTitle("Confirmation");
				conf.setHeaderText("Would you like to copy this photo to the new album?");
				conf.showAndWait().ifPresent(response -> {
					if (response == ButtonType.OK) {
						u.getAlbums().get(n).getPhotos().add(toBeMoved);
						saveData(users);
						return;
					} else if (response == ButtonType.CANCEL) {
						return;
					}	
				});		
				
			}
			i++;
		}
	}
	
	
	public void onClickDelete(ActionEvent event) {
		Alert conf = new Alert(AlertType.CONFIRMATION);
		conf.setTitle("Confirmation");
		conf.setHeaderText("Would you like to delete this photo from this album?");
		conf.showAndWait().ifPresent(response -> {
			
			if (response == ButtonType.OK) {
				selectedAlbum.getPhotos().remove(photos.getSelectionModel().getSelectedItem());
				photos.getItems().remove(photos.getSelectionModel().getSelectedItem());
				photos.refresh();
				photos.getSelectionModel().select(0);
				saveData(users);
				return;
			} else if (response == ButtonType.CANCEL) {
				return;
			}	
		});		
		
		
	}
	
	public void onClickEditCaption(ActionEvent event) {
		if (capFld.getText().isBlank()) {
			showAlert("Error", "Caption is Blank. Please Try Again.");
			return;
		}
		
		Alert conf = new Alert(AlertType.CONFIRMATION);
		conf.setTitle("Confirmation");
		conf.setHeaderText("Would you like to set this as the caption to this photo?");
		conf.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				photos.getSelectionModel().getSelectedItem().setCaption(capFld.getText());
				photos.refresh();
				capFld.clear();
				saveData(users);
				return;
			} else if (response == ButtonType.CANCEL) {
				return;
			}	
		});		
	}

	/**
	 * Handles when the user clicks the Open Photo button to load the selected photo to the photo viewer.
	 * 
	 * @param event the event caused by the user
	 */
	public void onClickOpen(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/pictureView.fxml"));
			Parent parent = (Parent) loader.load();
			PhotoController controller = loader.<PhotoController>getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			controller.start(users, photos, u, selectedAlbum);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Handles when the user clicks the Back button to return the user to the User home page
	 * 
	 * @param event the event caused by the user
	 */
	public void onClickBack(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user.fxml"));
			Parent parent = (Parent) loader.load();
			UserController controller = loader.<UserController>getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			saveData(users);
			controller.start(u, users);
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photo_login.fxml"));
			Parent parent = (Parent) loader.load();
			LoginController controller = loader.<LoginController>getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			saveData(users);
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