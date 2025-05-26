package co.edu.uniquindio.bd.service;

import co.edu.uniquindio.bd.dto.*;
import co.edu.uniquindio.bd.repository.EstudianteDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EstudianteDashboardService {

    @Autowired
    private EstudianteDashboardRepository estudianteDashboardRepository;

    /**
     * Retrieves the courses for a student
     * @param idEstudiante The student ID
     * @return A list of CursoEstudianteDTO objects representing the student's courses
     */
    public List<CursoEstudianteDto> obtenerCursosEstudiante(Integer idEstudiante) {
        return estudianteDashboardRepository.obtenerCursosEstudiante(idEstudiante);
    }

    public List<TemaDto> obtenerTemasPorCurso(Integer idCurso) {
        return estudianteDashboardRepository.obtenerTemasPorCurso(idCurso);
    }

    public List<ExamenDto> obtenerExamenesPorEstudiante(int idestudiante) {
        return estudianteDashboardRepository.obtenerExamenesPorEstudiante(idestudiante);
    }

    public List<PreguntaDto> obtenerPreguntasExamen(Integer idExamen) {
        return estudianteDashboardRepository.obtenerPreguntasExamen(idExamen);
    }

    public List<OpcionDto> obtenerOpcionesPorPregunta(Integer idPregunta) {
        return estudianteDashboardRepository.obtenerOpcionesPorPregunta(idPregunta);
    }

    public List<ConceptoDto> obtenerConceptosPorPregunta(Integer idPregunta) {
        return estudianteDashboardRepository.obtenerConceptosPorPregunta(idPregunta);
    }

    public Integer crearExamenPresentado(Integer idExamen, int idestudiante, String ip) {
        return estudianteDashboardRepository.crearExamenPresentado(idExamen, idestudiante, ip);
    }

    public int getIdExamenPregunta(Integer idPregunta, Integer idExamen) {
        return estudianteDashboardRepository.getIdExamenPregunta(idPregunta, idExamen);
    }

    public void registrarRespuestaSeleccion(int idExamPres, int idExamenPregunta, String texto) {
        estudianteDashboardRepository.registrarRespuestaSeleccion(idExamPres, idExamenPregunta, texto);
    }

    public void registrarRespuestaConcepto(int idExamPres, int idExamenPregunta, String orden) {
        estudianteDashboardRepository.registrarRespuestaConcepto(idExamPres, idExamenPregunta, orden);
    }

    public void registrarRespuestaSeleccionUnica(int idExamPres, int idExamenPregunta, String texto) {
        estudianteDashboardRepository.registrarRespuestaSeleccionUnica(idExamPres,idExamenPregunta,texto);
    }

    public BigDecimal finalizarExamen(Integer idPresentado) {
        return estudianteDashboardRepository.finalizarExamen(idPresentado);
    }

    public void registrarRespuestaVF(int idExamPres, Integer idPregunta, int flag) {
        estudianteDashboardRepository.registrarRespuestaVF(idExamPres,idPregunta,flag);
    }

    public void registrarRespuestaMultiple(int idExamPres, int idExamenPregunta, String todas) {
        estudianteDashboardRepository.registrarRespuestaMultiple(idExamPres, idExamenPregunta, todas);
    }

    public void registrarRespuestaOrden(int idExamPres, int idExamenPregunta, String ordenStr) {
        estudianteDashboardRepository.registrarRespuestaOrden(idExamPres, idExamenPregunta, ordenStr);
    }
}