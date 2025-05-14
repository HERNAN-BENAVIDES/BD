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
		// Iniciamos Spring Boot antes de JavaFX
		springContext = SpringApplication.run(BdApplication.class);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Cargamos el FXML de login
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/bd/login.fxml"));

		// Opcional: Si quieres inyectar dependencias de Spring en el controlador
		loader.setControllerFactory(springContext::getBean);

		root = loader.load();

		// Configuramos la escena
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Inicio de Sesión");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		// Cerramos el contexto de Spring al salir
		springContext.close();
	}

	public static void main(String[] args) {
		launch(args); // Método de JavaFX
	}
}