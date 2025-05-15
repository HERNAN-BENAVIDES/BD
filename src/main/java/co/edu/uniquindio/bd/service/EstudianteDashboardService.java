package co.edu.uniquindio.bd.service;

import co.edu.uniquindio.bd.dto.CursoEstudianteDTO;
import co.edu.uniquindio.bd.dto.ExamenDto;
import co.edu.uniquindio.bd.dto.TemaDTO;
import co.edu.uniquindio.bd.repository.EstudianteDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<CursoEstudianteDTO> obtenerCursosEstudiante(Integer idEstudiante) {
        return estudianteDashboardRepository.obtenerCursosEstudiante(idEstudiante);
    }

    public List<TemaDTO> obtenerTemasPorCurso(Integer idCurso) {
        return estudianteDashboardRepository.obtenerTemasPorCurso(idCurso);
    }

    public List<ExamenDto> obtenerExamenesPorEstudiante(int idestudiante) {
        return estudianteDashboardRepository.obtenerExamenesPorEstudiante(idestudiante);
    }
}