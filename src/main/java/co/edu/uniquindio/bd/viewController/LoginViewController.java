package co.edu.uniquindio.bd.viewController;

import co.edu.uniquindio.bd.controller.LoginController;
import co.edu.uniquindio.bd.model.Estudiante;
import co.edu.uniquindio.bd.model.Profesor;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class LoginViewController implements Initializable {

    @Autowired
    private LoginController loginController;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button loginButton;

    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the role dropdown with Student and Teacher options
        roleComboBox.setItems(FXCollections.observableArrayList("Estudiante", "Profesor"));
        // Set default selection
        roleComboBox.getSelectionModel().selectFirst();

        // Clear any previous status messages
        statusLabel.setText("");

        // Add hover effect to login button
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#000000"));

        loginButton.setOnMouseEntered(e -> {
            loginButton.setStyle("-fx-background-color: #3f51b5; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 20; -fx-cursor: hand;");
            loginButton.setEffect(shadow);
        });

        loginButton.setOnMouseExited(e -> {
            loginButton.setStyle("-fx-background-color: #303f9f; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 20; -fx-cursor: hand;");
            loginButton.setEffect(new DropShadow(5, Color.web("#00000066")));
        });

        // Add focus styling for text fields
        setupFocusEffects(emailField);
        setupFocusEffects(passwordField);
    }

    /**
     * Sets up focus effects for text input controls
     */
    private void setupFocusEffects(Control field) {
        field.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // When focused
                field.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #3f51b5; -fx-border-width: 2; -fx-padding: 8;");
            } else {
                // When not focused
                field.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #c5cae9; -fx-border-width: 1; -fx-padding: 8;");
            }
        });
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        // Play button animation
        playButtonAnimation();

        // Get the values from the form
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String selectedRole = roleComboBox.getValue();

        // Validate inputs
        if (email.isEmpty()) {
            statusLabel.setText("Por favor, ingrese su correo electrónico");
            return;
        }

        if (password.isEmpty()) {
            statusLabel.setText("Por favor, ingrese su contraseña");
            return;
        }

        if (selectedRole == null) {
            statusLabel.setText("Por favor, seleccione un rol");
            return;
        }

        // Basic email validation
        if (!isValidEmail(email)) {
            statusLabel.setText("Por favor, ingrese un correo electrónico válido");
            return;
        }

        // Show authenticating message
        statusLabel.setText("Iniciando sesión como " + selectedRole + "...");

        Estudiante estudiante = null;
        Profesor profesor = null;

        if (roleComboBox.getValue().equals("Estudiante")) {
            estudiante = loginController.authenticateEstudiante(email, password);
        }else {
            profesor = loginController.authenticateProfesor(email, password);
        }


        if (roleComboBox.getValue().equals("Estudiante") && estudiante.getIdestudiante() != -1) {
            // Successful login for Estudiante
            statusLabel.setText("Bienvenido " + estudiante.getNombre() + " " + estudiante.getApellido());
        }else if (roleComboBox.getValue().equals("Profesor") && profesor.getIdprofesor() != -1){
            // Successful login for Profesor
            statusLabel.setText("Bienvenido " + profesor.getNombre() + " " + profesor.getApellido());
        }else {
            // Failed login
            statusLabel.setText("Credenciales incorrectas. Intente nuevamente.");
        }
    }

    /**
     * Basic email validation
     */
    private boolean isValidEmail(String email) {
        // Simple regex for email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    /**
     * Creates and plays a scale animation for the login button
     */
    private void playButtonAnimation() {
        // Create a scale transition
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), loginButton);

        // Set the scale factors
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);

        // Create the reverse animation
        ScaleTransition reverseTransition = new ScaleTransition(Duration.millis(100), loginButton);
        reverseTransition.setFromX(0.95);
        reverseTransition.setFromY(0.95);
        reverseTransition.setToX(1.0);
        reverseTransition.setToY(1.0);

        // Play the animations in sequence
        scaleTransition.setOnFinished(event -> reverseTransition.play());
        scaleTransition.play();
    }
}
