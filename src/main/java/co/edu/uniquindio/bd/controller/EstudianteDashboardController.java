package co.edu.uniquindio.bd.controller;

import co.edu.uniquindio.bd.dto.*;
import co.edu.uniquindio.bd.service.EstudianteDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
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

    public Integer crearExamenPresentado(Integer idExamen, int idestudiante, String ip) {
        return estudianteDashboardService.crearExamenPresentado(idExamen, idestudiante, ip);
    }


    public void registrarRespuestaConcepto(int idExamPres, int idExamenPregunta, String orden, String pareja) {
        estudianteDashboardService.registrarRespuestaConcepto(idExamPres, idExamenPregunta, orden);
    }

    public int getIdExamenPregunta(Integer idPregunta, Integer idExamen) {
        return estudianteDashboardService.getIdExamenPregunta(idPregunta, idExamen);
    }

    public void registrarRespuestaSeleccionUnica(int idExamPres, int idExamenPregunta, String texto) {
        estudianteDashboardService.registrarRespuestaSeleccionUnica(idExamPres, idExamenPregunta, texto);
    }

    public BigDecimal finalizarExamen(Integer idPresentado) {
        return estudianteDashboardService.finalizarExamen(idPresentado);
    }

    public void registrarRespuestaVF(int idExamPres, Integer idPregunta, int flag) {
        estudianteDashboardService.registrarRespuestaVF(idExamPres,idPregunta,flag);
    }

    public void registrarRespuestaMultiple(int idExamPres, int idExamenPregunta, String todas) {
        estudianteDashboardService.registrarRespuestaMultiple(idExamPres, idExamenPregunta, todas);
    }
}