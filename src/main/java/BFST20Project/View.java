package BFST20Project;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.nio.file.Watchable;
import java.util.EnumMap;
import java.util.List;
import javafx.scene.control.Tooltip;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.net.URL;

public class View{

    public View(Model model, Stage stage)throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View.fxml"));
        Scene scene = loader.load();
        scene.getStylesheets().add("stylesheet.css");
        Controller controller = loader.getController();
        stage.setScene(scene);
        stage.show();
        controller.init(model);


    }

}
