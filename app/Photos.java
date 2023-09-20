/*
 * Advait Borkar adb222
 * Nikhil Sachdeva ns1224
 */
package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Photos extends Application{

    @Override
    public void start(Stage primaryStage) 
    throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(
            getClass().getResource("/view/photo_login.fxml")); 
       Pane root = (Pane)loader.load();


        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Photos");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
