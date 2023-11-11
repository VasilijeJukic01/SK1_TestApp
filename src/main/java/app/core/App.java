package app.core;

import app.view.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainView.getInstance().show();
    }
}
