package co.edu.uniquindio.bd.service;

import co.edu.uniquindio.bd.dto.Examen;
import co.edu.uniquindio.bd.model.ExamenPregunta;
import co.edu.uniquindio.bd.dto.Pregunta;
import co.edu.uniquindio.bd.repository.ExamenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ExamenService {

    @Autowired
    private ExamenRepository examenRepository;

    public Examen crearExamen(
            String nombre, String descripcion, String fechaInicio, String horaInicio,
            String fechaFin, String horaFin, String tiempoMaximo, String idProfesor,
            String idGrupo, String porcentajeCurso, String notaAprobacion,
            String totalPreguntas, String preguntasPorEstudiante, String idTema,
            String preguntasManuales, String preguntasAleatorias) {

        return examenRepository.crearExamen(
                nombre, descripcion, fechaInicio, horaInicio, fechaFin, horaFin,
                tiempoMaximo, idProfesor, idGrupo, porcentajeCurso, notaAprobacion,
                totalPreguntas, preguntasPorEstudiante, idTema, preguntasManuales,
                preguntasAleatorias);
    }

    public List<Pregunta> obtenerPreguntasOpciones(int idTema) throws SQLException {
        return examenRepository.obtenerPreguntasOpciones(idTema);
    }

    public ExamenPregunta insertarExamenPregunta(Double porcentaje, Integer tiempoMaximo, Integer examenIdExamen, Integer preguntaIdPregunta) throws SQLException {
        return examenRepository.insertarExamenPregunta(
                porcentaje, tiempoMaximo, examenIdExamen, preguntaIdPregunta);
    }

    public void completarExamen(int examenId) throws SQLException {
        examenRepository.completarExamen(examenId);
    }
}