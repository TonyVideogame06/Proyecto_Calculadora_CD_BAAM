package calculadora;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class CalculadoraCliente extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("VistaCalculadora2.fxml"));
            Scene mainscene = new Scene(root);
            primaryStage.setTitle("Cliente: Calculadora");
            primaryStage.setScene(mainscene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
