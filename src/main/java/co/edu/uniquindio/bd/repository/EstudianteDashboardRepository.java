package co.edu.uniquindio.bd.repository;

import co.edu.uniquindio.bd.dto.CursoEstudianteDTO;
import co.edu.uniquindio.bd.dto.ExamenDto;
import co.edu.uniquindio.bd.dto.TemaDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import java.sql.Timestamp;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class EstudianteDashboardRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * Retrieves the courses for a student using the obtener_cursos_estudiante stored procedure
     * @param idEstudiante The student ID
     * @return A list of CursoEstudianteDTO objects representing the student's courses
     */
    public List<CursoEstudianteDTO> obtenerCursosEstudiante(Integer idEstudiante) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("obtener_cursos_estudiante");

        // Register parameters
        sp.registerStoredProcedureParameter("p_idestudiante", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);

        // Set input parameter
        sp.setParameter("p_idestudiante", idEstudiante);

        // Execute the stored procedure
        sp.execute();

        // Process the results
        List<CursoEstudianteDTO> cursos = new ArrayList<>();
        List<Object[]> resultList = sp.getResultList();

        for (Object[] row : resultList) {
            CursoEstudianteDTO curso = new CursoEstudianteDTO();
            curso.setIdCurso((Integer) row[0]);
            curso.setNombreCurso((String) row[1]);
            curso.setNombreProfesor((String) row[2]);
            curso.setGrupo((String) row[3]);
            cursos.add(curso);
        }

        return cursos;
    }

    public List<TemaDTO> obtenerTemasPorCurso(Integer idCurso) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("obtener_temas_por_curso");

        // Register parameters
        sp.registerStoredProcedureParameter("p_idcurso", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);

        // Set input parameter
        sp.setParameter("p_idcurso", idCurso);

        // Execute the stored procedure
        sp.execute();

        // Process the results
        List<TemaDTO> temas = new ArrayList<>();
        List<Object[]> resultList = sp.getResultList();

        for (Object[] row : resultList) {
            TemaDTO tema = new TemaDTO();
            tema.setIdTema((Integer) row[0]);
            tema.setNombre((String) row[1]);
            tema.setUnidad((String) row[2]);
            tema.setDescripcion((String) row[3]);
            temas.add(tema);
        }

        return temas;
    }

    public List<ExamenDto> obtenerExamenesPorEstudiante(int idestudiante) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("obtener_examenes_por_estudiante");

        // Register parameters
        sp.registerStoredProcedureParameter("p_idestudiante", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);

        // Set input parameter
        sp.setParameter("p_idestudiante", idestudiante);

        // Execute the stored procedure
        sp.execute();

        // Process the results
        List<ExamenDto> examenes = new ArrayList<>();
        List<Object[]> resultList = sp.getResultList();

        for (Object[] row : resultList) {
            ExamenDto examen = new ExamenDto();
            examen.setIdExamen((Integer) row[0]);
            examen.setNombreExamen((String) row[1]);
            Timestamp inicio = (Timestamp) row[2];
            Timestamp fin = (Timestamp) row[3];

            DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            String horaInicio = inicio.toLocalDateTime().format(formatterHora);
            String horaFin = fin.toLocalDateTime().format(formatterHora);
            String fecha = inicio.toLocalDateTime().format(formatterFecha);

            examen.setHoraExamen(horaInicio + " - " + horaFin);
            examen.setFechaExamen(fecha);

            examen.setDuracionExamen(String.valueOf(java.time.Duration.between(inicio.toLocalDateTime(), fin.toLocalDateTime()).toMinutes()));
            examen.setPreguntasExamen((Integer) row[5]);
            examen.setTemaExamen((String) row[6]);
            examen.setGrupoExamen(((String) row[7]));
            examen.setCursoExamen(((String) row[8]));
            examenes.add(examen);
        }

        return examenes;
    }
}