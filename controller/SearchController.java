package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import misc.CommonFunctions;
import misc.PhotoCell;
import java.util.Calendar;
import java.util.Locale;

/**
 * Handles Photo Search Logic
 * 
 * @author Advait Borkar
 * @author Nikhil Sachdeva
 *
 */
public class SearchController {

	private User user;
	boolean Checked, go;
	@FXML
	private Button albumCreateBtn, LogoutBtn;
	@FXML
	private ChoiceBox<String> tagTChoiceBox, tagVChoiceBox;
	@FXML
	private DatePicker fromDate, toDate;
	@FXML
	private ListView<Tag> tags;
	ArrayList<User> users;
	@FXML
	ListView<Photo> photoListView;
	
	/**
	 * Need to initialize the current user, list of users, tag type, and tag value. 
	 * This is done by loading the unique tags and values from the user's photos
	 * 
	 * @param user the current user.
	 * @param users the list of users.
	 */
	
	public void start(User user, ArrayList<User> users) {
		this.user = user;
		this.users = users;

		// Set the cell factory for the photo list view
		photoListView.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>() {
			@Override
			public ListCell<Photo> call(ListView<Photo> photoList) {
				return new PhotoCell();
			}
		});

		ArrayList<String> tagTypes = new ArrayList<>();
		ArrayList<String> tagValues = new ArrayList<>();
		
		ArrayList<Tag> tempTags = user.getTags();
		for (Tag tag : tempTags) {
			tagTypes.add(tag.getName());
			tagValues.add(tag.getValue());
		}
		
		

		// Set tag type and tag value choice boxes
		tagTChoiceBox.setItems(FXCollections.observableArrayList(tagTypes));
		tagTChoiceBox.setValue("Tag Type");

		tagVChoiceBox.setItems(FXCollections.observableArrayList(tagValues));
		tagVChoiceBox.setValue("Tag Value");
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
			controller.start(stage);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * Handles when the user clicks the Back button. When selected, the user is taken back to the Album List page.
	 * 
	 * @param event the event caused by the user
	 */
	public void onClickBack(Event event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/albumList.fxml"));
			Parent parent = (Parent) loader.load();
			UserController controller = loader.<UserController>getController();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			controller.start(user, users);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * Handles when the user clicks the Add tag button. When clicked, the tags are added to the search list view.
	 * The tag is added if it does not already exist
	 * 
	 * @param event the event caused by the user
	 */
	
	public void onClickAdd(ActionEvent event) {
		ObservableList<Tag> tagList = tags.getItems();
		Tag newTag = new Tag(tagTChoiceBox.getSelectionModel().getSelectedItem().toString(),
				tagVChoiceBox.getSelectionModel().getSelectedItem().toString());
		
		if(tagList.contains(newTag)) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error in Search View");
			alert.setHeaderText("Error Adding Tag.");
			alert.setContentText("The name and value is associated with a tag that already exists. Please enter different values.");
			alert.showAndWait();
			return;
		}
		
		tagList.add(newTag);
		tags.refresh();
		tags.getSelectionModel().select(0);
		tagTChoiceBox.getSelectionModel().select(0);
		tagVChoiceBox.getSelectionModel().select(0);
	} 


	/**
	 * Handles when the user clicks the Remove tag button. When clicked, the currently selected tag is removed.
	 * 
	 * @param event the event caused by the user
	 */
	public void onClickRemove(ActionEvent event) {
		tags.getItems().remove(tags.getSelectionModel().getSelectedItem());
		tags.refresh();
		tags.getSelectionModel().select(0);

	}

	/**
	 * Handles when the user clicks Search photo button. When clicked, it searches through the date ranges and/or tags 
	 * in every photo in every albumm the user has.
	 * 
	 * @param event the event caused by the user
	 */
	
	public void onClickSearch(ActionEvent event) {

	    photoListView.getItems().clear();
	    Checked = false;
	    go = false;

	    ArrayList<Album> aList = user.getAlbums();
	    for (int i = 0; i < aList.size(); i++) {
	        ArrayList<Photo> plist = aList.get(i).getPhotos();
	        for (int j = 0; j < plist.size(); j++) {
	            boolean added = false;
	            ArrayList<Tag> phototag = plist.get(j).getTags();
	            String[] photodate = plist.get(j).getDate().getTime().toString().split(" ");

	            DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM").withLocale(Locale.ENGLISH);
	            TemporalAccessor accessor = parser.parse(photodate[1]);
	            int month = accessor.get(ChronoField.MONTH_OF_YEAR);

	            LocalDate photoDate = LocalDate.of(Integer.parseInt(photodate[5]), month, Integer.parseInt(photodate[2]));

	            if (checkdatefields() && go) {

	                // FromDate
	                String frdate = fromDate.getValue().toString();
	                String[] fromdate = frdate.split("-");
	                LocalDate f_date = LocalDate.of(Integer.parseInt(fromdate[0]), Integer.parseInt(fromdate[1]), Integer.parseInt(fromdate[2]));

	                // ToDate
	                String todate = toDate.getValue().toString();
	                String[] tdate = todate.split("-");
	                LocalDate t_date = LocalDate.of(Integer.parseInt(tdate[0]), Integer.parseInt(tdate[1]), Integer.parseInt(tdate[2]));

	                if (photoDate.isAfter(f_date) && photoDate.isBefore(t_date)) {

	                    if (photoListView.getItems().contains(plist.get(j))) {
	                        continue;
	                    } else {
	                        added = true;
	                        photoListView.getItems().add(plist.get(j));
	                        photoListView.refresh();
	                    }

	                }
	            }

	            if (tags.getItems() != null && phototag != null && added == false) {
	                for (int k = 0; k < tags.getItems().size(); k++) {
	                    Tag currTag = tags.getItems().get(k);
	                    for (int l = 0; l < phototag.size(); l++) {
	                        Tag pTag = phototag.get(l);
	                        if (currTag.getName().matches(pTag.getName()) && currTag.getValue().matches(pTag.getValue())) {
	                            if (photoListView.getItems().contains(plist.get(j))) {
	                                continue;
	                            } else {
	                                photoListView.getItems().add(plist.get(j));
	                                photoListView.refresh();
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    }
	}



	/**
	 * Checks to make sure the date ranges are correct.
	 * 
	 * @return boolean of boolean type
	 */
	public boolean checkdatefields() {

		if (Checked == false) {

			Checked = true;

			if (toDate.getValue() != null && fromDate.getValue() == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error in Search View");
				alert.setHeaderText("Error in From Date.");
				alert.setContentText("Please select a valid From Date.");

				alert.showAndWait();
				return false;
			}
			if (toDate.getValue() == null && fromDate.getValue() != null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error in Search View");
				alert.setHeaderText("Error in To Date.");
				alert.setContentText("Please select a valid To Date.");

				alert.showAndWait();
				return false;
			}

		}

		if (toDate.getValue() != null && fromDate.getValue() != null) {
			go = true;
			return true;
		}
		return false;
	}

	/**
	 * Handles when the user clicks the Create Album button from the search results. A new album will then be created.
	 */
	public void onClickCreateResult() {
		if (photoListView.getItems().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error in Search View");
			alert.setHeaderText("No Photos Error.");
			alert.setContentText("To Create an Album, Please Search for Photos.");
			alert.showAndWait();

		} else {

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			Album newAlbum = new Album("Search Results " + dateFormat.format(cal.getTime()).toString());
			user.getAlbums().add(newAlbum);

			for (Photo currphoto : photoListView.getItems()) {
				newAlbum.getPhotos().add(currphoto);
			}
			CommonFunctions.saveData(users);
		}
	}
}