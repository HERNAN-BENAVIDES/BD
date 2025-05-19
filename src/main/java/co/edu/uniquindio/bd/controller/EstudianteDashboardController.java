package co.edu.uniquindio.bd.controller;

import co.edu.uniquindio.bd.dto.*;
import co.edu.uniquindio.bd.service.EstudianteDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EstudianteDashboardController {

    @Autowired
    private EstudianteDashboardService estudianteDashboardService;

    /**
     * Retrieves the courses for a student
     * @param idEstudiante The student ID
     * @return A list of CursoEstudianteDTO objects representing the student's courses
     */
    public List<CursoEstudianteDto> obtenerCursosPorEstudiante(int idEstudiante) {
        return estudianteDashboardService.obtenerCursosEstudiante(idEstudiante);
    }


    public List<TemaDto> obtenerTemasPorCurso(Integer idCurso) {
        return estudianteDashboardService.obtenerTemasPorCurso(idCurso);
    }

    public List<ExamenDto> obtenerExamenesPorEstudiante(int idestudiante) {
        return estudianteDashboardService.obtenerExamenesPorEstudiante(idestudiante);
    }

    public List<PreguntaDto> obtenerPreguntasExamen(Integer idExamen) {
        return estudianteDashboardService.obtenerPreguntasExamen(idExamen);
    }

    public List<OpcionDto> obtenerOpcionesPorPregunta(Integer idPregunta) {
        return estudianteDashboardService.obtenerOpcionesPorPregunta(idPregunta);
    }

    public List<ConceptoDto> obtenerConceptosPorPregunta(Integer idPregunta) {
        return estudianteDashboardService.obtenerConceptosPorPregunta(idPregunta);
    }
}