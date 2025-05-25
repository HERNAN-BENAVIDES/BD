package co.edu.uniquindio.bd.viewController;

import co.edu.uniquindio.bd.controller.EstudianteDashboardController;
import co.edu.uniquindio.bd.dto.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
public class EstudianteDashboardViewController implements Initializable {

    @Autowired
    private EstudianteDashboardController estudianteDashboardController;

    @FXML
    private TabPane tabPane;

    @FXML
    private Button btnVerTemas;

    @FXML
    private Button btnPresentarExamen;

    @FXML
    private Label studentNameLabel;

    @FXML
    private Label studentIdLabel;

    @FXML
    private Label studentFullNameLabel;

    @FXML
    private Label studentEmailLabel;

    private Button btnSiguiente;

    @FXML
    private TableView<CursoEstudianteDto> coursesTableView;

    @FXML
    private TableColumn<CursoEstudianteDto, Short> colIdCurso;

    @FXML
    private TableColumn<CursoEstudianteDto, String> colNombreCurso;

    @FXML
    private TableColumn<CursoEstudianteDto, String> colNombreProfesor;

    @FXML
    private TableColumn<CursoEstudianteDto, String> colGrupo;

    @FXML
    private TableView<TemaDto> temasTableView;

    @FXML
    private TableColumn<TemaDto, Integer> colIdTema;

    @FXML
    private TableColumn<TemaDto, String> colNombreTema;

    @FXML
    private TableColumn<TemaDto, String> colUnidad;

    @FXML
    private TableColumn<TemaDto, String> colDescripcion;

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
    private VBox contenedorPreguntas;

    @FXML
    private TableView<?> gradesTableView;

    @FXML
    private Button logoutButton;

    private EstudianteDto estudiante;
    
    private Timeline examTimeline;
    
    private Timeline questionTimeline;

    private Integer idPresentado;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnVerTemas.disableProperty().bind(
                coursesTableView.getSelectionModel().selectedItemProperty().isNull()
        );

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab.getText().equals("Examenes")) {
                cargarExamenesEstudiante();

                examenesTableView.getSelectionModel().selectedItemProperty().addListener((obsExamen, oldExamen, newExamen) -> {
                    if (newExamen != null) {
                        verificarDisponibilidadExamen(newExamen);
                    }
                });

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
            List<CursoEstudianteDto> cursos = estudianteDashboardController.obtenerCursosPorEstudiante(estudiante.getIdestudiante());
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

    @FXML
    public void handlePresentarExamen(ActionEvent event) {

        // 1. Registrar el examen presentado
        idPresentado = estudianteDashboardController.crearExamenPresentado(
                examenesTableView.getSelectionModel().getSelectedItem().getIdExamen(),
                estudiante.getIdestudiante(),
                "127.0.0.1" // Reemplaza por la IP real si la tienes
        );


        // 2. Recuperar preguntas
        List<PreguntaDto> preguntas = estudianteDashboardController
                .obtenerPreguntasExamen(examenesTableView.getSelectionModel().getSelectedItem().getIdExamen());
        if (preguntas.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Examen vacío", "No hay preguntas para este examen.");
            return;
        }

        // 3. Crear y configurar el Tab de examen
        Tab tabExamen = crearTabExamen();

        // 4. Crear el contenedor principal dentro del Tab
        VBox contenido = crearContenedorExamen(tabExamen);

        // 5. Añadir la primera pregunta y el botón "Siguiente"
        iniciarNavegacionPreguntas(contenido, preguntas, tabExamen);
    }

    private Tab crearTabExamen() {
        Tab tab = new Tab("Examen en Curso");
        tab.setClosable(false);        // no se puede cerrar
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        logoutButton.setDisable(true); // deshabilitar logout
        // impedir que cambie de pestaña
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != tab) {
                tabPane.getSelectionModel().select(tab);
            }
        });
        return tab;
    }

    private VBox crearContenedorExamen(Tab tabExamen) {
        VBox box = new VBox(10);
        box.setStyle("-fx-padding: 10;");
        tabExamen.setContent(box);
        return box;
    }

    private void iniciarNavegacionPreguntas(VBox contenido, List<PreguntaDto> preguntas, Tab tabExamen) {
        AtomicInteger indice = new AtomicInteger(0);

        // Label del temporizador de examen
        Label examenLabel = new Label();
        examenLabel.setStyle("-fx-font-size: 16px; -fx-font-weight:bold;");
        contenido.getChildren().add(examenLabel);

        // Label del temporizador pregunta
        Label timerLabel = new Label();
        timerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight:bold;");
        contenido.getChildren().add(timerLabel);

        // Label de progreso
        Label progresoLabel = new Label();
        progresoLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        contenido.getChildren().add(progresoLabel);

        // Vista inicial de la pregunta
        contenido.getChildren().add(new Label()); // Placeholder, será reemplazado

        // Botón Siguiente
        btnSiguiente = new Button("Siguiente");
        HBox hBoxBtn = new HBox(btnSiguiente);
        hBoxBtn.setAlignment(Pos.CENTER_RIGHT);
        contenido.getChildren().add(hBoxBtn);

        // 1) Arrancar temporizador de examen (solo finaliza el examen, NO dispara siguiente)
        int durExamen = examenesTableView.getSelectionModel().getSelectedItem().getDuracionExamen() * 60;
        examTimeline = startTimer(
                durExamen,
                examenLabel,
                "Tiempo restante examen",
                this::formatHHMMSS,
                () -> {
                    examTimeline.stop();
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Se acabó el tiempo del examen");
                    a.showAndWait();
                    terminarExamen(tabExamen);
                }
        );

        // 2) Función para mostrar cada pregunta y reiniciar su temporizador
        Consumer<Integer> mostrarPregunta = idx -> {
            // detener el viejo timeline de pregunta (si existía)
            if (questionTimeline != null) questionTimeline.stop();

            progresoLabel.setText("Pregunta " + (idx + 1) + " de " + preguntas.size());
            contenido.getChildren().set(3, construirVistaPregunta(preguntas.get(idx)));

            // arrancar nuevo timeline de pregunta (este SÍ dispara siguiente)
            questionTimeline = startTimer(
                    preguntas.get(idx).getTiempo(),
                    timerLabel,
                    "Tiempo restante pregunta",
                    this::formatMMSS,
                    btnSiguiente::fire
            );
        };

        // 3) Mostrar la primera
        mostrarPregunta.accept(0);

        btnSiguiente.setOnAction(e -> {

            int actual = indice.get();
            guardarRespuesta(idPresentado, preguntas.get(actual), contenido.getChildren().get(3));

            int i = indice.incrementAndGet();
            if (i < preguntas.size()) {
                mostrarPregunta.accept(i);
                if (i == preguntas.size()-1) btnSiguiente.setText("Finalizar");
            } else {
                btnSiguiente.setDisable(true);
                terminarExamen(tabExamen);
            }
        });
    }

    /**
     * Extrae la respuesta del Node (puede ser VBox, TextFlow, GridPane, etc.),
     * detecta el tipo de pregunta y llama al SP correspondiente.
     */
    private void guardarRespuesta(int idExamPres, PreguntaDto pregunta, Node vista) {
        int idExamenPregunta = estudianteDashboardController.getIdExamenPregunta(pregunta.getIdPregunta(), pregunta.getIdExamen()); // tenlo en tu DTO
        switch (pregunta.getIdTipoPregunta()) {
            case 1:  // única
                // 1) Localizar ToggleGroup
                ToggleGroup tg = ((VBox) vista).getChildren().stream()
                        .filter(n -> n instanceof RadioButton)
                        .map(n -> (RadioButton) n)
                        .findFirst()
                        .map(rb -> rb.getToggleGroup())
                        .orElse(null);

                if (tg != null && tg.getSelectedToggle() != null) {
                    // 2) Obtener texto de la opción seleccionada
                    String texto = ((RadioButton) tg.getSelectedToggle()).getText();
                    // 3) Invocar el SP con una sola opción (sin ';')
                    estudianteDashboardController
                            .registrarRespuestaSeleccionUnica(idExamPres, idExamenPregunta, texto);
                }
                break;


            case 2:  // selección múltiple
                // 1) Recolectar todos los CheckBoxes marcados
                List<String> seleccionadas = ((VBox) vista).getChildren().stream()
                        .filter(n -> n instanceof CheckBox)
                        .map(n -> (CheckBox) n)
                        .filter(CheckBox::isSelected)
                        .map(cb -> ((OpcionDto) cb.getUserData()).getTextoOpcion().trim())
                        .collect(Collectors.toList());

                if (!seleccionadas.isEmpty()) {
                    // 1) Únelas con ';' SIN espacios
                    String todas = String.join(";", seleccionadas);
                    // 2) Llama al SP
                    System.out.println(todas);
                    estudianteDashboardController
                            .registrarRespuestaMultiple(idExamPres, idExamenPregunta, todas);
                }

                break;


            case 3: // V/F
                // 1) Localiza el ToggleGroup de Verdadero/Falso
                ToggleGroup tg1 = ((VBox) vista).getChildren().stream()
                        .filter(n -> n instanceof RadioButton)
                        .map(n -> (RadioButton) n)
                        .findFirst()
                        .map(rb -> rb.getToggleGroup())
                        .orElse(null);

                if (tg1 != null && tg1.getSelectedToggle() != null) {
                    // 2) Determina si seleccionó Verdadero (1) o Falso (0)
                    String texto = ((RadioButton) tg1.getSelectedToggle()).getText();
                    int flag = "Verdadero".equalsIgnoreCase(texto) ? 1 : 0;

                    // 3) Llama al SP pasándole idExamenPresentado, idPregunta y flag
                    estudianteDashboardController
                            .registrarRespuestaVF(idExamPres, pregunta.getIdPregunta(), flag);
                }
                break;

            case 4: // ordenar conceptos

                break;

            case 5: // Ordenar Conceptos

                break;

            case 6: // completar espacios

                break;
        }
    }

    private void terminarExamen(Tab tabExamen) {

        BigDecimal nota = estudianteDashboardController.finalizarExamen(idPresentado);

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Fin de examen");
        alerta.setHeaderText(null);
        alerta.setContentText("Has completado el examen. Gracias por participar." + "\nNota: " + nota);
        alerta.showAndWait();
        // parar ambos timelines
        if (examTimeline    != null) examTimeline.stop();
        if (questionTimeline!= null) questionTimeline.stop();
        // cerrar tab y re-habilitar logout
        tabPane.getTabs().remove(tabExamen);
        logoutButton.setDisable(false);


    }


    /** Formatea a mm:ss */
    private String formatMMSS(int totalSegundos) {
        int minutos = totalSegundos / 60;
        int segundos = totalSegundos % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    /** Formatea a HH:mm:ss */
    private String formatHHMMSS(int totalSegundos) {
        int horas = totalSegundos / 3600;
        int minutos = (totalSegundos % 3600) / 60;
        int segundos = totalSegundos % 60;
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }


    // Modificamos startTimer para que DEVUELVA el Timeline
    private Timeline startTimer(
            int totalSeconds,
            Label tiempoLabel,
            String labelPrefix,
            Function<Integer, String> timeFormatter,
            Runnable onFinished
    ) {
        IntegerProperty remaining = new SimpleIntegerProperty(totalSeconds);
        tiempoLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> labelPrefix + ": " + timeFormatter.apply(remaining.get()),
                        remaining
                )
        );

        Timeline timeline = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.seconds(1), evt -> {
            int secs = remaining.get() - 1;
            remaining.set(secs);
            if (secs <= 0) {
                timeline.stop();
                onFinished.run();
            }
        });
        timeline.getKeyFrames().add(kf);
        timeline.setCycleCount(totalSeconds);
        timeline.play();

        return timeline;
    }






    private void obtenerTemasCursoSeleccionado() {
        CursoEstudianteDto cursoSeleccionado = coursesTableView.getSelectionModel().getSelectedItem();
        if (cursoSeleccionado != null) {
            List<TemaDto> temas = estudianteDashboardController.obtenerTemasPorCurso(cursoSeleccionado.getIdCurso());

            temasTableView.setItems(FXCollections.observableArrayList(temas));}
    }


    private void verificarDisponibilidadExamen(ExamenDto examenSeleccionado) {
        try {
            // Parsear la fecha
            DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate fecha = LocalDate.parse(examenSeleccionado.getFechaExamen(), formatterFecha);

            // Parsear la hora
            String[] horas = examenSeleccionado.getHoraExamen().split("-");
            DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");

            LocalTime horaInicio = LocalTime.parse(horas[0].trim(), formatterHora);
            LocalTime horaFin = LocalTime.parse(horas[1].trim(), formatterHora);

            // Construir rangos de fecha y hora
            LocalDateTime inicioExamen = LocalDateTime.of(fecha, horaInicio);
            LocalDateTime finExamen = LocalDateTime.of(fecha, horaFin);
            LocalDateTime ahora = LocalDateTime.now();

            if (ahora.isAfter(inicioExamen) && ahora.isBefore(finExamen)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Acceso concedido", "Puedes presentar el examen.");
                // Aquí puedes habilitar el botón o lanzar la vista del examen
                btnPresentarExamen.setDisable(false);
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Acceso denegado", "Este examen solo se puede presentar entre " + horas[0] + " y " + horas[1] + " del " + examenSeleccionado.getFechaExamen());
                btnPresentarExamen.setDisable(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo procesar la fecha u hora del examen.");
        }
    }


    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private Node construirVistaPregunta(PreguntaDto pregunta) {
        List<OpcionDto> opciones = null;


        if (pregunta.getIdTipoPregunta() == 1 || pregunta.getIdTipoPregunta() == 2 || pregunta.getIdTipoPregunta() == 3){
            // Selección única, múltiple o verdadero/falso, las opciones se mezclan
            opciones = estudianteDashboardController.obtenerOpcionesPorPregunta(pregunta.getIdPregunta());
            Collections.shuffle(opciones);
        }

        return switch (pregunta.getIdTipoPregunta()) {
            case 1 -> crearVistaSeleccionUnica(pregunta, opciones);
            case 2 -> crearVistaSeleccionMultiple(pregunta, opciones);
            case 3 -> crearVistaVerdaderoFalso(pregunta, opciones);
            case 4 -> crearVistaOrdenarConceptos(pregunta);
            case 5 -> crearVistaEmparejarConceptos(pregunta);
            case 6 -> crearVistaCompletarEspacios(pregunta);
            default -> new Label("Tipo de pregunta no soportado");
        };
    }

    private Node crearVistaCompletarEspacios(PreguntaDto pregunta) {
        // 1) Recuperar la única OpcionConcepto
        ConceptoDto opcionConcepto = estudianteDashboardController
                .obtenerConceptosPorPregunta(pregunta.getIdPregunta())
                .get(0);
        String plantilla = opcionConcepto.getTextoConcepto();
        // términos separados por ';'
        List<String> terminosList = new ArrayList<>(
                Arrays.asList(opcionConcepto.getTextoParejaConcepto().split(";"))
        );
        Collections.shuffle(terminosList); // Mezcla los términos
        String[] terminos = terminosList.toArray(new String[0]);

        // 2) Contenedor principal
        VBox contenedor = crearContenedorPregunta(pregunta.getPregunta());
        contenedor.setSpacing(15);

        // 3) Preparar TextFlow
        TextFlow flujo = new TextFlow();
        flujo.setLineSpacing(5);
        flujo.setPadding(new Insets(5));

        // 4) Construir comboboxes diná­micamente
        Pattern huecoPattern = Pattern.compile("_{2,}");
        Matcher matcher     = huecoPattern.matcher(plantilla);

        int ultimo   = 0;
        int idxHueco = 0;

        // Guardamos todos los combos en una lista para gestionar sus items
        List<ComboBox<String>> combos = new ArrayList<>();


        while (matcher.find() && idxHueco < terminos.length) {
            // a) texto previo al hueco
            flujo.getChildren().add(new Text(plantilla.substring(ultimo, matcher.start())));

            // b) calcular ancho basado en longitud máxima de TODOS los términos
            int maxLen = Arrays.stream(terminos)
                    .mapToInt(String::length)
                    .max().orElse(5);
            double ancho = maxLen * 20;  // 10px por carácter

            // c) crear ComboBox vacío inicialmente
            ComboBox<String> combo = new ComboBox<>();
            combo.setId("comboHueco" + idxHueco);
            combo.setPromptText("Selecciona…");
            combo.setPrefWidth(ancho);
            combo.getItems().add("— Ninguno —");
            combo.getItems().addAll(terminos);
            combo.setValue("— Ninguno —");  // valor inicial


            // d) añadir a la lista para control mutuo
            combos.add(combo);
            flujo.getChildren().add(combo);

            ultimo   = matcher.end();
            idxHueco++;
        }

        // e) texto tras el último hueco
        flujo.getChildren().add(new Text(plantilla.substring(ultimo)));
        contenedor.getChildren().add(flujo);

        // 5) Listener para cada combo: al seleccionar, eliminamos el término de los siguientes,
        //    y al cambiar, reincorporamos el anterior en los demás.
        for (int i = 0; i < combos.size(); i++) {
            ComboBox<String> combo = combos.get(i);
            combo.valueProperty().addListener((obs, viejo, nuevo) -> {
                // Si no seleccionó nada útil, salir
                if ("— Ninguno —".equals(nuevo)) {
                    nuevo = null;
                }
                if ("— Ninguno —".equals(viejo)) {
                    viejo = null;
                }

                // Reincorporar el valor anterior en los demás combos
                if (viejo != null) {
                    for (ComboBox<String> c : combos) {
                        if (c != combo && !c.getItems().contains(viejo)) {
                            c.getItems().add(viejo);
                            FXCollections.sort(c.getItems()); // opcional: mantener orden
                        }
                    }
                }

                // Eliminar la nueva selección en otros combos
                if (nuevo != null) {
                    for (ComboBox<String> c : combos) {
                        if (c != combo) {
                            c.getItems().remove(nuevo);
                        }
                    }
                }
            });

        }

        return contenedor;
    }



    private Node crearVistaOrdenarConceptos(PreguntaDto pregunta) {
        // 1) Contenedor principal con el texto de la pregunta
        VBox contenedor = crearContenedorPregunta(pregunta.getPregunta());
        contenedor.setSpacing(15);

        // 2) Recuperar la única OpcionConcepto y partir en ítems
        ConceptoDto opcionConcepto = estudianteDashboardController
                .obtenerConceptosPorPregunta(pregunta.getIdPregunta()).get(0);
        List<String> pasos = new ArrayList<>(List.of(opcionConcepto.getTextoConcepto().split("\\s*;\\s*")));

        // 3) Mezclar el orden para que no salga ya ordenado
        Collections.shuffle(pasos);

        // 4) VBox que contendrá cada paso en un HBox con controles
        VBox lista = new VBox(8);

        // 5) Creamos un Runnable auto-referenciado para (re)construir la lista
        Runnable rebuild = new Runnable() {
            @Override
            public void run() {
                lista.getChildren().clear();
                for (int i = 0; i < pasos.size(); i++) {
                    String texto = pasos.get(i);
                    HBox row = new HBox(10);
                    row.setAlignment(Pos.CENTER_LEFT);

                    Label lbl = new Label((i + 1) + ". " + texto);
                    lbl.setWrapText(true);
                    lbl.setMaxWidth(400);

                    Button up   = new Button("↑");
                    Button down = new Button("↓");

                    final int pos = i;
                    up.setOnAction(e -> {
                        if (pos > 0) {
                            Collections.swap(pasos, pos, pos - 1);
                            this.run();    // aquí llamo de nuevo al rebuild
                        }
                    });
                    down.setOnAction(e -> {
                        if (pos < pasos.size() - 1) {
                            Collections.swap(pasos, pos, pos + 1);
                            this.run();    // y aquí también
                        }
                    });

                    // Desactivar cuando no procede
                    up.setDisable(i == 0);
                    down.setDisable(i == pasos.size() - 1);

                    row.getChildren().addAll(lbl, up, down);
                    lista.getChildren().add(row);
                }
            }
        };

        // 6) Construir inicialmente
        rebuild.run();

        // 7) Meter en un ScrollPane por si excede
        ScrollPane scroll = new ScrollPane(lista);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(200);

        contenedor.getChildren().add(scroll);
        return contenedor;
    }



    private Node crearVistaEmparejarConceptos(PreguntaDto pregunta) {
        // --- 1) Contenedor y datos ---
        VBox contenedor = crearContenedorPregunta(pregunta.getPregunta());
        List<ConceptoDto> conceptos = estudianteDashboardController.obtenerConceptosPorPregunta(pregunta.getIdPregunta());

        // --- 2) Grid destino (4×2) ---
        GridPane gridDestino = new GridPane();
        gridDestino.setHgap(20); gridDestino.setVgap(15);
        gridDestino.setPrefWidth(600);
        gridDestino.getColumnConstraints().addAll(
                new ColumnConstraints(250), new ColumnConstraints(250)
        );
        Set<StackPane> allSlots = new HashSet<>();
        Map<StackPane, StackPane> ocupado = new HashMap<>();

        for (int i = 0; i < conceptos.size(); i++) {
            // Columna A: textoConcepto
            Label lblA = new Label(conceptos.get(i).getTextoConcepto());
            lblA.setWrapText(true);
            lblA.setStyle("-fx-font-size:13; -fx-font-weight:bold;");
            gridDestino.add(lblA, 0, i);

            // Columna B: slot vacío
            StackPane slot = new StackPane();
            slot.setPrefSize(200, 40);
            slot.setStyle("-fx-border-color:#aaa; -fx-background-color:#fff;");
            gridDestino.add(slot, 1, i);
            allSlots.add(slot);
        }

        // --- 3) Grid origen (4×1 tarjetas) ---
        GridPane gridOrigen = new GridPane();
        gridOrigen.setHgap(10); gridOrigen.setVgap(10);
        gridOrigen.setPadding(new Insets(20,0,0,0));

        // Guardamos índice original de cada tarjeta
        Map<StackPane, Pair<Integer,Integer>> originalIndex = new HashMap<>();
        AtomicReference<StackPane> picked = new AtomicReference<>();

        Collections.shuffle(conceptos);

        for (int i = 0; i < conceptos.size(); i++) {
            ConceptoDto dto = conceptos.get(i);
            StackPane card = new StackPane(new Label(dto.getTextoParejaConcepto()));
            card.setStyle("-fx-border-color:#333; -fx-background-color:#e0e0e0; -fx-padding:8;");
            gridOrigen.add(card, i % 4, i / 4);
            originalIndex.put(card, new Pair<>(i % 4, i / 4));

            // click sobre tarjeta
            card.addEventFilter(MouseEvent.MOUSE_CLICKED, ev -> {
                if (picked.get() == card) {
                    // segunda vez: desmarcar y devolver origen
                    unpickCard(card, picked, gridOrigen, originalIndex.get(card));
                } else {
                    // si otra estaba pickeada, devuelve esa primero
                    if (picked.get() != null) {
                        unpickCard(picked.get(), picked, gridOrigen, originalIndex.get(picked.get()));
                    }
                    pickCard(card, allSlots, ocupado, gridOrigen, picked);
                }
                ev.consume();
            });
        }

        // --- 4) click sobre destino o fuera ---
        contenedor.addEventFilter(MouseEvent.MOUSE_CLICKED, ev -> {
            Node tgt = ev.getPickResult().getIntersectedNode();
            // ¿es slot?
            StackPane slot = null;
            if (tgt instanceof StackPane sp && allSlots.contains(sp)) {
                slot = sp;
            } else if (tgt.getParent() instanceof StackPane sp2 && allSlots.contains(sp2)) {
                slot = sp2;
            }
            if (slot != null && picked.get() != null) {
                // colocar tarjeta pickeada en el slot
                StackPane card = picked.get();
                // expulsar previa
                if (ocupado.containsKey(slot)) {
                    StackPane prev = ocupado.remove(slot);
                    unpickCard(prev, new AtomicReference<>(prev), gridOrigen, originalIndex.get(prev));
                }
                slot.getChildren().add(card);
                ocupado.put(slot, card);
                picked.set(null);
                card.setStyle(card.getStyle().replace("-fx-border-color:red;", ""));
            } else if (picked.get() != null) {
                // clic fuera de slot: devolver tarjeta pickeada
                StackPane card = picked.get();
                unpickCard(card, picked, gridOrigen, originalIndex.get(card));
            }
        });

        contenedor.getChildren().addAll(gridDestino, gridOrigen);
        return contenedor;
    }

    // Marca la tarjeta como “en mano”
    private void pickCard(StackPane card,
                          Set<StackPane> allSlots,
                          Map<StackPane, StackPane> ocupado,
                          GridPane origen,
                          AtomicReference<StackPane> picked) {
        // si venía de un slot, limpiarlo
        Parent p = card.getParent();
        if (p instanceof StackPane slot && allSlots.contains(slot)) {
            slot.getChildren().clear();
            ocupado.remove(slot);
        }
        // destacar
        picked.set(card);
        card.setStyle(card.getStyle() + "-fx-border-color:red;");
    }

    // Desmarca y devuelve al origen en la celda indicada
    private void unpickCard(StackPane card,
                            AtomicReference<StackPane> picked,
                            GridPane origen,
                            Pair<Integer,Integer> idx) {
        picked.set(null);
        card.setStyle(card.getStyle().replace("-fx-border-color:red;", ""));
        origen.getChildren().remove(card);
        origen.add(card, idx.getKey(), idx.getValue());
    }





    // Falso / Verdadero
    private Node crearVistaVerdaderoFalso(PreguntaDto pregunta, List<OpcionDto> opciones) {
        VBox box = crearContenedorPregunta(opciones.get(0).getTextoOpcion());

        ToggleGroup grupo = new ToggleGroup();
        RadioButton rbV = new RadioButton("Verdadero");
        RadioButton rbF = new RadioButton("Falso");
        rbV.setToggleGroup(grupo);
        rbF.setToggleGroup(grupo);

        box.getChildren().addAll(rbV, rbF);
        return box;
    }

    // Selección Única
    private Node crearVistaSeleccionUnica(PreguntaDto pregunta, List<OpcionDto> opciones) {
        VBox box = crearContenedorPregunta(pregunta.getPregunta());

        ToggleGroup grupo = new ToggleGroup();
        for (OpcionDto opcion : opciones) {
            RadioButton rb = new RadioButton(opcion.getTextoOpcion());
            rb.setToggleGroup(grupo);
            rb.setUserData(opcion);
            box.getChildren().add(rb);
        }
        return box;
    }

    // Selección Múltiple (usa CheckBox para permitir varias)
    private Node crearVistaSeleccionMultiple(PreguntaDto pregunta, List<OpcionDto> opciones) {
        VBox box = crearContenedorPregunta(pregunta.getPregunta());

        for (OpcionDto opcion : opciones) {
            CheckBox cb = new CheckBox(opcion.getTextoOpcion());
            cb.setUserData(opcion);
            box.getChildren().add(cb);
        }
        return box;
    }

    private VBox crearContenedorPregunta(String textoPregunta) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #ddd; " +
                "-fx-border-radius: 5; -fx-background-radius: 5;");
        Label lbl = new Label(textoPregunta);
        lbl.setWrapText(true);
        lbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        box.getChildren().add(lbl);
        return box;
    }

}
