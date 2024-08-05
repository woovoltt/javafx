package com.itgroup.application;

import com.itgroup.utility.Utility;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KaraokeTest extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = Utility.FXML_PATH + "KaraokeTest.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));

        Parent container = fxmlLoader.load();
        Scene scene = new Scene(container);
        stage.setTitle("인기 노래 차트^^~♪");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
