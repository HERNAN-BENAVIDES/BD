package co.edu.uniquindio.bd.viewController;

import co.edu.uniquindio.bd.controller.EstudianteDashboardController;
import co.edu.uniquindio.bd.dto.CursoEstudianteDTO;
import co.edu.uniquindio.bd.dto.EstudianteDto;
import co.edu.uniquindio.bd.dto.ExamenDto;
import co.edu.uniquindio.bd.dto.TemaDTO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class EstudianteDashboardViewController implements Initializable {

    @Autowired
    private LoginViewController loginViewController;

    @Autowired
    private EstudianteDashboardController estudianteDashboardController;

    @FXML
    private TabPane tabPane;

    @FXML
    private Button btnVerTemas;

    @FXML
    private Label studentNameLabel;

    @FXML
    private Label studentIdLabel;

    @FXML
    private Label studentFullNameLabel;

    @FXML
    private Label studentEmailLabel;

    @FXML
    private TableView<CursoEstudianteDTO> coursesTableView;

    @FXML
    private TableColumn<CursoEstudianteDTO, Short> colIdCurso;

    @FXML
    private TableColumn<CursoEstudianteDTO, String> colNombreCurso;

    @FXML
    private TableColumn<CursoEstudianteDTO, String> colNombreProfesor;

    @FXML
    private TableColumn<CursoEstudianteDTO, String> colGrupo;

    @FXML
    private TableView<TemaDTO> temasTableView;

    @FXML
    private TableColumn<TemaDTO, Integer> colIdTema;

    @FXML
    private TableColumn<TemaDTO, String> colNombreTema;

    @FXML
    private TableColumn<TemaDTO, String> colUnidad;

    @FXML
    private TableColumn<TemaDTO, String> colDescripcion;

    @FXML
    private TableView<ExamenDto> examenesTableView;

    @FXML
    private TableColumn<ExamenDto, Integer> colIdExamen;

    @FXML
    private TableColumn<ExamenDto, String> colNombreExamen;

    @FXML
    private TableColumn<ExamenDto, String> colHoraExamen;

    @FXML
    private TableColumn<ExamenDto, String> colFechaExamen;

    @FXML
    private TableColumn<ExamenDto, String> colTiempoExamen;

    @FXML
    private TableColumn<ExamenDto, Integer> colPreguntasExamen;

    @FXML
    private TableColumn<ExamenDto, String> colTemaExamen;

    @FXML
    private TableColumn<ExamenDto, String> colGrupoExamen;

    @FXML
    private TableColumn<ExamenDto, String> colCursoExamen;

    @FXML
    private TableView<?> gradesTableView;

    @FXML
    private Button logoutButton;

    private EstudianteDto estudiante;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnVerTemas.disableProperty().bind(
                coursesTableView.getSelectionModel().selectedItemProperty().isNull()
        );

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab.getText().equals("Examenes")) {
                cargarExamenesEstudiante();
            }
        });

    }

    private void cargarExamenesEstudiante() {
        colIdExamen.setCellValueFactory(new PropertyValueFactory<>("idExamen"));
        colNombreExamen.setCellValueFactory(new PropertyValueFactory<>("nombreExamen"));
        colHoraExamen.setCellValueFactory(new PropertyValueFactory<>("horaExamen"));
        colFechaExamen.setCellValueFactory(new PropertyValueFactory<>("fechaExamen"));
        colTiempoExamen.setCellValueFactory(new PropertyValueFactory<>("duracionExamen"));
        colPreguntasExamen.setCellValueFactory(new PropertyValueFactory<>("preguntasExamen"));
        colTemaExamen.setCellValueFactory(new PropertyValueFactory<>("temaExamen"));
        colGrupoExamen.setCellValueFactory(new PropertyValueFactory<>("grupoExamen"));
        colCursoExamen.setCellValueFactory(new PropertyValueFactory<>("cursoExamen"));

        if (estudiante != null) {
            List<ExamenDto> examenes = estudianteDashboardController.obtenerExamenesPorEstudiante(estudiante.getIdestudiante());
            examenesTableView.setItems(FXCollections.observableArrayList(examenes));
        }

    }

    private void cargarTablaCursos() {
        colIdCurso.setCellValueFactory(new PropertyValueFactory<>("idCurso"));
        colNombreCurso.setCellValueFactory(new PropertyValueFactory<>("nombreCurso"));
        colNombreProfesor.setCellValueFactory(new PropertyValueFactory<>("nombreProfesor"));
        colGrupo.setCellValueFactory(new PropertyValueFactory<>("grupo"));

        // Llama al procedimiento que devuelve los cursos del estudiante
        if (estudiante != null) {
            List<CursoEstudianteDTO> cursos = estudianteDashboardController.obtenerCursosPorEstudiante(estudiante.getIdestudiante());
            coursesTableView.setItems(FXCollections.observableArrayList(cursos));
        }
        cargarTablaTemas();
    }

    private void cargarTablaTemas() {
        colIdTema.setCellValueFactory(new PropertyValueFactory<>("idTema"));
        colNombreTema.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUnidad.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }


    /**
     * Sets the student data and updates the UI
     * @param estudiante The authenticated student
     */
    public void setEstudiante(EstudianteDto estudiante) {
        this.estudiante = estudiante;

        // Update UI with student information
        if (estudiante != null) {
            studentNameLabel.setText(estudiante.getNombre());
            studentIdLabel.setText(String.valueOf(estudiante.getIdestudiante()));
            studentFullNameLabel.setText(estudiante.getNombre() + " " + estudiante.getApellido());
            studentEmailLabel.setText(estudiante.getEmail());
        }

        cargarTablaCursos();
    }

    /**
     * Handles the logout button click
     */
    @FXML
    public void handleLogout() {
        // Close the current window
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();

        // Optionally, you could reopen the login window here
        // But for simplicity, we'll just close this window
    }

    @FXML
    public void handleVerTemas(ActionEvent event) {
        // lógica: cambiar a la pestaña Temas, por ejemplo:
        tabPane.getSelectionModel().select(1);  // índice 1 = pestaña “Temas”
        // Obtener temas del curso seleccionado
        obtenerTemasCursoSeleccionado();
    }

    private void obtenerTemasCursoSeleccionado() {
        CursoEstudianteDTO cursoSeleccionado = coursesTableView.getSelectionModel().getSelectedItem();
        if (cursoSeleccionado != null) {
            List<TemaDTO> temas = estudianteDashboardController.obtenerTemasPorCurso(cursoSeleccionado.getIdCurso());

            temasTableView.setItems(FXCollections.observableArrayList(temas));}
    }

}
