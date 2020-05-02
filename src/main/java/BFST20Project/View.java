package BFST20Project;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
