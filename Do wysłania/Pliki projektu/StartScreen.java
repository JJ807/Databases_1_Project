package Filmoholik;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Klasa odpowiadajaca za uruchomienie programu
 */
public class StartScreen extends Application {

    /**
     * Otwiera glowna scene programu z pliku StartScreen.fxml
     * @param primaryStage obiekty typu Stage bedacy opakowaniem na zmieniajace sie sceny typu Scene
     * @throws Exception wyjatek wyrzucany gdy ktoras z wewnetrznych funkcji nie zadziala poprawnie
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("StartScreen.fxml"));
        Controller controller = new Controller();
        controller.setUser(new User("", false));
        loader.setController(controller);
        Parent root = loader.load();
        primaryStage.setTitle("Filmoholik");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.getIcons().add(new Image(getClass().getResource("resources/favicon.jpg").toExternalForm()));
        primaryStage.show();
    }

    /**
     * Metoda uruchamiajaca program
     * @param args przykladowe argumenty dla programu
     */
    public static void main(String[] args) {
        launch(args);
    }
}
