package co.edu.uniquindio.bd.viewController;

import co.edu.uniquindio.bd.BdApplication;
import co.edu.uniquindio.bd.controller.CrearExamenController;
import co.edu.uniquindio.bd.dto.Examen;
import co.edu.uniquindio.bd.dto.Pregunta;
import co.edu.uniquindio.bd.dto.ProfesorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class CrearExamenViewController {

    @Autowired
    private CrearExamenController examenController;

    @FXML
    private Label dificilLabel;

    @FXML
    private Label medioLabel;

    @FXML
    private Label facilLabel;

    @FXML
    private TextField preguntasManualesField;

    @FXML
    private TextField preguntasAleatoriasField;

    @FXML
    private TableView<Map<String, Object>> temasTable;

    @FXML
    private TableColumn<Map<String, Object>, String> nombreCursoColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> nombreUnidadColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> nombreTemaColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> descripcionTemaColumn;

    @FXML
    private Label descripcionTemasLabel;

    @FXML
    private TextField profesorAutorField;

    @FXML
    private TextField grupoField;

    @FXML
    private TextField cursoField;

    @FXML
    private VBox groupSelectionContainer;

    @FXML
    private ScrollPane examFormScrollPane;

    @FXML
    private VBox groupContainer;

    @FXML
    private Button crearExamenButton;

    @FXML
    private Button verExamenesButton;

    @FXML
    private Button crearPreguntaButton;

    @FXML
    private Button verEstadisticasButton;

    @FXML
    private TextField nombreExamenField;

    @FXML
    private TextArea descripcionExamenField;

    @FXML
    private DatePicker fechaInicioPicker;

    @FXML
    private TextField horaInicioField;

    @FXML
    private DatePicker fechaFinalPicker;

    @FXML
    private TextField horaFinField;

    @FXML
    private TextField tiempoMaximoField;

    @FXML
    private TextField porcentajeCursoField;

    @FXML
    private TextField notaAprobacionField;

    @FXML
    private TextField totalPreguntasField;

    @FXML
    private TextField preguntasPorEstudianteField;

    @FXML
    private TextField preguntasManualField;

    @FXML
    private TextField preguntasAleatorioField;

    @FXML
    private Button escogerPreguntasButton;

    @FXML
    private ScrollPane questionSelectionScrollPane;

    @FXML
    private VBox bancoPreguntasContainer;

    @FXML
    private Label bancoPreguntasLabel;

    private ProfesorDto profesor;
    private Map<String, Object> grupoSeleccionado;
    private Examen examenCreado;

    public void setProfesor(ProfesorDto profesor) {
        this.profesor = profesor;
        cargarDatos();
    }

    public void initialize() {

    }

    private void cargarDatos() {
        if (profesor == null) {
            System.out.println("El profesor no está configurado.");
            return;
        }

        // Obtener los grupos del profesor
        List<Map<String, Object>> grupos = examenController.obtenerGruposPorProfesor(profesor.getIdprofesor());

        // Crear dinámicamente las "cards" para cada grupo
        for (Map<String, Object> grupo : grupos) {
            HBox card = crearCardGrupo(grupo);
            groupContainer.getChildren().add(card);
        }

        // Configurar el botón activo al cargar la vista para el estilo
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
            grupoSeleccionado= grupo;
            handleCrearExamenButtonClick();
        });

        // Añadir elementos al card
        card.getChildren().addAll(info, crearExamenBtn);
        HBox.setHgrow(info, Priority.ALWAYS);

        return card;
    }

    @FXML
    private void handleCrearExamenButtonClick() {
        // Ocultar el contenedor de selección de grupo
        groupSelectionContainer.setVisible(false);
        groupSelectionContainer.setManaged(false);

        // Mostrar el formulario de examen
        examFormScrollPane.setVisible(true);
        examFormScrollPane.setManaged(true);
        inicializarValoresExamForm();
    }

    @FXML
    private void handleEscogerPreguntasButtonClick() {
        try{
            Examen nuevoExamen = examenController.crearExamen(
                    nombreExamenField.getText(),
                    descripcionExamenField.getText(),
                    fechaInicioPicker.getValue() != null ? fechaInicioPicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "",
                    horaInicioField.getText(),
                    fechaFinalPicker.getValue() != null ? fechaFinalPicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "",
                    horaFinField.getText(),
                    tiempoMaximoField.getText(),
                    profesor.getIdprofesor().toString(),
                    grupoSeleccionado.get("idgrupo").toString(),
                    porcentajeCursoField.getText(),
                    notaAprobacionField.getText(),
                    totalPreguntasField.getText(),
                    preguntasPorEstudianteField.getText(),
                    temasTable.getSelectionModel().getSelectedItem() != null
                            ? temasTable.getSelectionModel().getSelectedItem().get("idtema").toString()
                            : "",
                    preguntasManualField.getText(),
                    preguntasAleatorioField.getText()
            );

            // Guardar el examen creado
            examenCreado= nuevoExamen;
            preguntasManualesField.setText(preguntasManualField.getText());
            preguntasAleatoriasField.setText(preguntasAleatorioField.getText());

            // Mostrar mensaje de éxito
            mostrarAlerta("Éxito", "El examen ha sido creado correctamente, puede proceder a escoger las preguntas.");

            // Cerrar formulario examen
            examFormScrollPane.setVisible(false);
            examFormScrollPane.setManaged(false);

            // Mostrar el contenedor de selección de preguntas
            questionSelectionScrollPane.setVisible(true);
            questionSelectionScrollPane.setManaged(true);

            // Configurar el label del banco de preguntas
            bancoPreguntasLabel.setText("BANCO DE PREGUNTAS - TEMA: " + temasTable.getSelectionModel().getSelectedItem().get("nombre_tema").toString());


            cargarPreguntasPorTema(examenCreado.getTemaIdTema());

    } catch (Exception e) {
            // Extraer el mensaje de error del procedimiento
            String errorMessage = e.getMessage();
            String procedureMessage = "Ocurrió un error al crear el examen.";
            if (errorMessage != null && errorMessage.contains("ORA-200")) {
                // Buscar el primer ORA-200XX
                int startIndex = errorMessage.indexOf("ORA-200") + 11;
                if (startIndex > 10) {
                    int endIndex = errorMessage.indexOf("\n", startIndex);
                    if (endIndex == -1) {
                        endIndex = errorMessage.length();
                    }
                    procedureMessage = errorMessage.substring(startIndex, endIndex).trim();
                    // Si es ORA-20007, intentar extraer el error interno
                    if (procedureMessage.startsWith("Error inesperado al crear el examen: ORA-200")) {
                        int innerStart = procedureMessage.indexOf("ORA-200") + 11;
                        int innerEnd = procedureMessage.indexOf(".", innerStart);
                        if (innerEnd == -1) {
                            innerEnd = procedureMessage.length();
                        }
                        procedureMessage = procedureMessage.substring(innerStart, innerEnd).trim();
                    }
                }
            } else if (errorMessage != null && errorMessage.contains("ORA-06550")) {
                procedureMessage = "Error en la configuración del procedimiento: número o tipos de argumentos incorrectos.";
            }
            mostrarAlerta("Error", procedureMessage);
        }
    }

    public void cargarPreguntasPorTema(int idTema) {
        try {
            bancoPreguntasContainer.getChildren().clear();
            List<Pregunta> preguntas = examenController.obtenerPreguntasOpciones(idTema);
            Collections.shuffle(preguntas);
            ObjectMapper mapper = new ObjectMapper();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            for (Pregunta pregunta : preguntas) {
                // crear vbox para cada pregunta con estilo
                VBox preguntaBox = new VBox(10);
                preguntaBox.setStyle("-fx-background-color: #f9f9f9; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: #cccccc; -fx-border-radius: 10;");
                preguntaBox.setPrefWidth(600);

                // Crear un CheckBox para que el profesor pueda seleccionar esta pregunta
                CheckBox checkBoxSeleccion = new CheckBox();

                // Ajustar alineación del CheckBox al lado derecho
                HBox headerBox = new HBox();
                headerBox.setSpacing(10);
                headerBox.setAlignment(Pos.CENTER_LEFT);
                Label preguntaLabel = new Label("Pregunta: " + pregunta.getTextoPregunta());
                preguntaLabel.setStyle("-fx-font-weight: bold;");
                HBox.setHgrow(preguntaLabel, Priority.ALWAYS); // Empuja el CheckBox al extremo derecho
                headerBox.getChildren().addAll(preguntaLabel, checkBoxSeleccion);

                // Crear campos para "Porcentaje" y "Tiempo Máximo"
                HBox opcionesBox = new HBox(15);
                opcionesBox.setAlignment(Pos.CENTER_LEFT);
                Label porcentajeLabel = new Label("Porcentaje:");
                TextField porcentajeField = new TextField();
                porcentajeField.setPromptText("10");
                porcentajeField.setMaxWidth(100);

                Label tiempoMaximoLabel = new Label("Tiempo (m):");
                TextField tiempoMaximoField = new TextField();
                tiempoMaximoField.setPromptText("5");
                tiempoMaximoField.setMaxWidth(100);

                opcionesBox.getChildren().addAll(porcentajeLabel, porcentajeField, tiempoMaximoLabel, tiempoMaximoField);


                checkBoxSeleccion.setOnAction(event -> {
                    if (checkBoxSeleccion.isSelected()) {
                        String porcentaje = porcentajeField.getText();
                        String tiempoMaximo = tiempoMaximoField.getText();

                        // Llamar al método para insertar la pregunta seleccionada en la base de datos
                        if (verificarPreguntasManuales()){
                            insertarPreguntaSeleccionada(pregunta, porcentaje, tiempoMaximo);
                        }
                    } else {
                        // Informar que no se puede eliminar la pregunta
                        mostrarAlerta("Error", "La pregunta ya fue añadida al examen, no puede eliminarla.");

                    }
                });

                // Añadir toda la configuración al VBox principal de la pregunta
                preguntaBox.getChildren().add(headerBox); // Header con el título de la pregunta y el CheckBox

                // añadir los textfield de porcentaje y tiempo
                preguntaBox.getChildren().add(opcionesBox);

                preguntaBox.getChildren().add(new Label("Tipo: " + pregunta.getTipoPreguntaNombre()));
                preguntaBox.getChildren().add(new Label("Nivel: " + pregunta.getNivelNombre()));
                preguntaBox.getChildren().add(new Label("Estado: Pregunta Pública"));
                preguntaBox.getChildren().add(new Label("Profesor: " + pregunta.getProfesorNombre()));
                preguntaBox.getChildren().add(new Label("Creada: " +
                        pregunta.getFechaCreacion().toLocalDateTime().format(formatter)));

                // parsear json de opciones
                List<Map<String, Object>> opciones = mapper.readValue(
                        pregunta.getOpciones(),
                        mapper.getTypeFactory().constructCollectionType(List.class, Map.class)
                );

                // renderizar opciones segun el tipo de la pregunta
                switch (pregunta.getTipoPreguntaId()) {
                    case 1: // seleccion multiple unica
                        ToggleGroup group = new ToggleGroup();
                        Collections.shuffle(opciones, new Random());
                        for (Map<String, Object> opcion : opciones) {
                            RadioButton rb = new RadioButton((String) opcion.get("textoopcion"));
                            rb.setToggleGroup(group);
                            // verificar si es la respuesta correcta
                            Integer esCorrecta = (Integer) opcion.get("escorrecta"); // corregido a "escorrecta"
                            if (esCorrecta != null && esCorrecta == 1) {
                                rb.setSelected(true);
                            }
                            preguntaBox.getChildren().add(rb);
                        }
                        break;

                    case 2: // seleccion multiple multiples
                        Collections.shuffle(opciones, new Random());
                        for (Map<String, Object> opcion : opciones) {
                            CheckBox cb = new CheckBox((String) opcion.get("textoopcion"));
                            // verificar si es la respuesta correcta
                            Integer esCorrecta = (Integer) opcion.get("escorrecta"); // corregido a "escorrecta"
                            if (esCorrecta != null && esCorrecta == 1) {
                                cb.setSelected(true);
                            }
                            preguntaBox.getChildren().add(cb);
                        }
                        break;

                    case 3: // falso/verdadero
                        HBox fvContainer = new HBox(10);
                        RadioButton falso = new RadioButton("Falso");
                        RadioButton verdadero = new RadioButton("Verdadero");
                        ToggleGroup fvGroup = new ToggleGroup();
                        falso.setToggleGroup(fvGroup);
                        verdadero.setToggleGroup(fvGroup);
                        // verificar respuesta correcta
                        Map<String, Object> opcion = opciones.get(0); // solo una opcion
                        Integer esCorrecta = (Integer) opcion.get("escorrecta"); // corregido a "escorrecta"
                        if (esCorrecta != null && esCorrecta == 1) {
                            verdadero.setSelected(true);
                        } else {
                            falso.setSelected(true);
                        }
                        fvContainer.getChildren().addAll(falso, verdadero);
                        preguntaBox.getChildren().add(fvContainer);
                        break;

                    case 4: // ordenar conceptos
                        List<String> conceptos = (List<String>) opciones.get(0).get("conceptos"); // corregido a List<String>
                        ListView<HBox> ordenarListView = new ListView<>();
                        for (int i = 0; i < conceptos.size(); i++) {
                            HBox fila = new HBox(10);
                            fila.getChildren().add(new Label("Orden: " + (i + 1)));
                            fila.getChildren().add(new Label("Concepto: " + conceptos.get(i))); // usar string directamente
                            ordenarListView.getItems().add(fila);
                        }
                        ordenarListView.setPrefHeight(120);
                        preguntaBox.getChildren().add(ordenarListView);
                        break;

                    case 5: // emparejar conceptos
                        ListView<HBox> emparejarListView = new ListView<>();
                        for (Map<String, Object> pareja : opciones) {
                            HBox fila = new HBox(10);
                            fila.getChildren().add(new Label("Concepto: " + pareja.get("textoconcepto")));
                            fila.getChildren().add(new Label("—"));
                            fila.getChildren().add(new Label("Pareja: " + pareja.get("textoparejaconcepto")));
                            emparejarListView.getItems().add(fila);
                        }
                        emparejarListView.setPrefHeight(120);
                        preguntaBox.getChildren().add(emparejarListView);
                        break;

                    case 6: // completar espacios
                        String textoConBlancos = (String) opciones.get(0).get("texto_con_blancos");
                        List<String> respuestas = (List<String>) opciones.get(0).get("respuestas");
                        // dividir texto por "_" y ensamblar combobox
                        HBox completarContainer = new HBox(5);
                        String[] segmentos = textoConBlancos.split("_");
                        for (int i = 0; i < segmentos.length; i++) {
                            completarContainer.getChildren().add(new Label(segmentos[i]));
                            if (i < respuestas.size()) {
                                ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(respuestas));
                                comboBox.getSelectionModel().select(respuestas.get(i)); // seleccionar respuesta correcta
                                completarContainer.getChildren().add(comboBox);
                            }
                        }
                        preguntaBox.getChildren().add(completarContainer);
                        break;

                    default:
                        preguntaBox.getChildren().add(new Label("Tipo de pregunta desconocido: " + pregunta.getTipoPreguntaId()));
                }

                // agregar el vbox de la pregunta al contenedor principal
                bancoPreguntasContainer.getChildren().add(preguntaBox);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar el banco de preguntas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void insertarPreguntaSeleccionada(Pregunta pregunta, String porcentaje, String tiempoMaximo) {
        try {
            if (porcentaje == null || porcentaje.isEmpty() || porcentaje.trim().isEmpty()){
                porcentaje="0";
            }
            if (tiempoMaximo == null || tiempoMaximo.isEmpty() || tiempoMaximo.trim().isEmpty()){
                tiempoMaximo="0";
            }
            // Lógica para llamar al procedimiento almacenado e insertar la pregunta seleccionada
            examenController.insertarExamenPregunta(
                    Double.parseDouble(porcentaje),
                    Integer.parseInt(tiempoMaximo),
                    examenCreado.getIdExamen(),
                    pregunta.getIdPregunta());
            mostrarAlerta("Éxito", "Pregunta añadida correctamente al examen.");


            // Actualizar textfield tipo nivel seleccionado
            if (pregunta.getNivelId() != null) {
                switch (pregunta.getNivelId()) {
                    case 3:
                        System.out.println(facilLabel.getText());
                        int facilCount = Integer.parseInt(facilLabel.getText());
                        facilLabel.setText(String.valueOf(facilCount + 1));
                        break;
                    case 2:
                        System.out.println(medioLabel.getText());
                        int medioCount = Integer.parseInt(medioLabel.getText());
                        medioLabel.setText(String.valueOf(medioCount + 1));
                        break;
                    case 1:
                        System.out.println(dificilLabel.getText());
                        int dificilCount = Integer.parseInt(dificilLabel.getText());
                        dificilLabel.setText(String.valueOf(dificilCount + 1));
                        break;
                }
            }
        } catch (Exception e) {

            mostrarAlerta("Error", "No se pudo añadir la pregunta al examen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean verificarPreguntasManuales() {
        int preguntasManuales = Integer.valueOf(preguntasManualesField.getText());

        int pregFaciles = Integer.parseInt(facilLabel.getText());
        int pregMedias = Integer.parseInt(medioLabel.getText());
        int pregDificiles = Integer.parseInt(dificilLabel.getText());

        int totalPreguntas = pregFaciles + pregMedias + pregDificiles;

        if (totalPreguntas >= preguntasManuales) {
            mostrarAlerta("Error", "Ya selecciono el maximo de preguntas manuales. No puede seleccionar más.");
            return false;
        }else{
            return true;

        }
    }

    @FXML
    private void handleConfirmarPreguntasButtonClick() {
        try {
            // Lógica para confirmar las preguntas seleccionadas
            mostrarAlerta("Confirmación", "Las preguntas seleccionadas han sido confirmadas exitosamente. Las preguntas aleatorias serán añadidas al examen automaticamente.");
            examenController.completarExamen(examenCreado.getIdExamen());
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al confirmar las preguntas: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void inicializarValoresExamForm() {
        profesorAutorField.setText(profesor.getNombre() + " " + profesor.getApellido());
        grupoField.setText(grupoSeleccionado.get("nombre_grupo").toString());
        cursoField.setText(grupoSeleccionado.get("nombre_curso").toString());

        // Deshabilitar los campos de texto
        profesorAutorField.setEditable(false);
        grupoField.setEditable(false);
        cursoField.setEditable(false);

        // Configurar el label de descripción
        descripcionTemasLabel.setText("Elija uno de los temas del curso " + grupoSeleccionado.get("nombre_curso").toString() + " para poder seleccionar las preguntas.");

        // Configurar las columnas de la tabla
        nombreCursoColumn.setCellValueFactory(data -> new SimpleStringProperty(grupoSeleccionado.get("nombre_curso").toString()));
        nombreUnidadColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("nombre_unidad").toString()));
        nombreTemaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("nombre_tema").toString()));
        descripcionTemaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("descripcion_tema").toString()));

        // Llenar la tabla con los temas
        List<Map<String, Object>> temas = examenController.obtenerTemasPorGrupo((int) grupoSeleccionado.get("idgrupo"));
        temasTable.getItems().addAll(temas);
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