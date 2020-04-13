package BFST20Project;

import javafx.application.Application;
import javafx.stage.Stage;
import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;



public class Launcher extends Application {
    public void start(Stage primaryStage) throws Exception {
        Model model = new Model();
        View view = new View(model, primaryStage);
    }

    public static void main(String[] args){
        launch(args);
    }

}
