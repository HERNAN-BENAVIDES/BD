package co.edu.uniquindio.bd.viewController;

import co.edu.uniquindio.bd.BdApplication;
import co.edu.uniquindio.bd.model.Profesor;
import co.edu.uniquindio.bd.service.GrupoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class CrearExamenViewController {

    @FXML
    private VBox groupContainer;

    @Autowired
    private GrupoService grupoService;

    @FXML
    private Button crearExamenButton;

    @FXML
    private Button verExamenesButton;

    @FXML
    private Button crearPreguntaButton;

    @FXML
    private Button verEstadisticasButton;

    private Profesor profesor;

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
        cargarDatos(); // Llamar al método que carga los datos después de configurar el profesor
    }

    public void initialize() {
        // Dejar vacío o agregar lógica que no dependa de profesor
    }

    private void cargarDatos() {
        if (profesor == null) {
            System.out.println("El profesor no está configurado.");
            return;
        }

        // Obtener los grupos del profesor
        List<Map<String, Object>> grupos = grupoService.obtenerGruposPorProfesor(profesor.getIdprofesor());

        // Crear dinámicamente las "cards" para cada grupo
        for (Map<String, Object> grupo : grupos) {
            HBox card = crearCardGrupo(grupo);
            groupContainer.getChildren().add(card);
        }

        // Configurar el botón activo al cargar la vista
        resetButtonStyles();
        crearExamenButton.getStyleClass().add("active");
    }

    private HBox crearCardGrupo(Map<String, Object> grupo) {
        HBox card = new HBox(10);
        card.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10; -fx-border-color: #c5cae9; -fx-border-radius: 5;");
        card.setSpacing(15);

        // Información del grupo
        VBox info = new VBox(5);
        Label nombreGrupo = new Label("Grupo: " + grupo.get("nombre_grupo"));
        nombreGrupo.setFont(Font.font("System", FontWeight.BOLD, 14));
        Label detalles = new Label("Año: " + grupo.get("anio") + " | Período: " + grupo.get("periodo") +
                " | Estudiantes: " + grupo.get("numero_estudiantes") +
                " | Curso: " + grupo.get("nombre_curso"));
        info.getChildren().addAll(nombreGrupo, detalles);

        // Botón para crear examen
        Button crearExamenBtn = new Button("Crear Examen");
        crearExamenBtn.getStyleClass().add("card-button");
        crearExamenBtn.setOnAction(e -> {
            // Lógica para crear examen
            System.out.println("Crear examen para el grupo: " + grupo.get("nombre_grupo"));
        });

        // Añadir elementos al card
        card.getChildren().addAll(info, crearExamenBtn);
        HBox.setHgrow(info, Priority.ALWAYS);

        return card;
    }



    @FXML
    public void handleDashboardNavigation(ActionEvent event) {
        // Restablecer el estilo de todos los botones
        resetButtonStyles();

        // Identificar el botón que se hizo clic
        Button clickedButton = (Button) event.getSource();
        clickedButton.getStyleClass().add("active");

        // Lógica de navegación según el botón
        if (clickedButton == crearExamenButton) {
            System.out.println("Navegando a Crear Examen");
        } else if (clickedButton == verExamenesButton) {
            navigateTo("/co/edu/uniquindio/bd/VerExamenes.fxml");
        } else if (clickedButton == crearPreguntaButton) {
            navigateTo("/co/edu/uniquindio/bd/CrearPregunta.fxml");
        } else if (clickedButton == verEstadisticasButton) {
            navigateTo("/co/edu/uniquindio/bd/VerEstadisticas.fxml");
        }
    }

    private void resetButtonStyles() {
        // Eliminar la clase "active" de todos los botones
        crearExamenButton.getStyleClass().remove("active");
        verExamenesButton.getStyleClass().remove("active");
        crearPreguntaButton.getStyleClass().remove("active");
        verEstadisticasButton.getStyleClass().remove("active");
    }

    private void navigateTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(BdApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) crearExamenButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}