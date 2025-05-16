package co.edu.uniquindio.bd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BdApplication extends Application {

    private static ConfigurableApplicationContext springContext;
    private Parent root;

    @Override
    public void init() throws Exception {
        // Iniciar Spring Boot antes de JavaFX
        springContext = SpringApplication.run(BdApplication.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/bd/login.fxml"));
        loader.setControllerFactory(springContext::getBean);
        root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Inicio de Sesión");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }

    public static void main(String[] args) {
        launch(args);
    }
}