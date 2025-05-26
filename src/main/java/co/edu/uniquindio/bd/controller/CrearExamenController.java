package co.edu.uniquindio.bd.controller;

import co.edu.uniquindio.bd.dto.Examen;
import co.edu.uniquindio.bd.model.ExamenPregunta;
import co.edu.uniquindio.bd.dto.Pregunta;
import co.edu.uniquindio.bd.service.ExamenService;
import co.edu.uniquindio.bd.service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
public class CrearExamenController {

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private ExamenService examenService;

    /**
     * Obtiene los grupos asociados a un profesor.
     * @param idProfesor El profesor cuyos grupos se desean obtener.
     * @return Lista de grupos en formato de mapa.
     */
    public List<Map<String, Object>> obtenerGruposPorProfesor(int idProfesor) {
        return grupoService.obtenerGruposPorProfesor(idProfesor);
    }

    /**
     * Obtiene los temas asociados a un grupo.
     * @param idGrupo El grupo cuyos temas se desean obtener.
     * @return Lista de temas en formato de mapa.
     */
    public List<Map<String, Object>> obtenerTemasPorGrupo(int idGrupo) {
        return grupoService.obtenerTemasPorGrupo(idGrupo);
    }


    public Examen crearExamen(
            String nombre, String descripcion, String fechaInicio, String horaInicio,
            String fechaFin, String horaFin, String tiempoMaximo, String idProfesor,
            String idGrupo, String porcentajeCurso, String notaAprobacion,
            String totalPreguntas, String preguntasPorEstudiante, String idTema,
            String preguntasManuales, String preguntasAleatorias) {
        return examenService.crearExamen(
                nombre, descripcion, fechaInicio, horaInicio, fechaFin, horaFin,
                tiempoMaximo, idProfesor, idGrupo, porcentajeCurso, notaAprobacion,
                totalPreguntas, preguntasPorEstudiante, idTema, preguntasManuales,
                preguntasAleatorias);
    }

    public List<Pregunta> obtenerPreguntasOpciones(int idTema) throws SQLException {
        return examenService.obtenerPreguntasOpciones(idTema);
    }

    public ExamenPregunta insertarExamenPregunta(Double porcentaje, Integer tiempoMaximo, Integer examenIdExamen, Integer preguntaIdPregunta) throws SQLException {
        return examenService.insertarExamenPregunta(
                porcentaje, tiempoMaximo, examenIdExamen, preguntaIdPregunta);
    }

    public void completarExamen(int examenId) throws SQLException {
        examenService.completarExamen(examenId);
    }


}