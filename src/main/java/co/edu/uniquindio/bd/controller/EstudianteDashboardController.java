package co.edu.uniquindio.bd.controller;

import co.edu.uniquindio.bd.dto.CursoEstudianteDTO;
import co.edu.uniquindio.bd.dto.TemaDTO;
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
    public List<CursoEstudianteDTO> obtenerCursosPorEstudiante(int idEstudiante) {
        return estudianteDashboardService.obtenerCursosEstudiante(idEstudiante);
    }


    public List<TemaDTO> obtenerTemasPorCurso(Integer idCurso) {
        return estudianteDashboardService.obtenerTemasPorCurso(idCurso);
    }
}