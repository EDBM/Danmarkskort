package BFST20Project;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;

public class Launcher extends Application {
    public void start(Stage primaryStage) throws FileNotFoundException, XMLStreamException {
        Model model = new Model();
        View view = new View(model,primaryStage);
        new Controller(model, view);
    }

    public static void main(String[] args){
        launch(args);
    }

}
