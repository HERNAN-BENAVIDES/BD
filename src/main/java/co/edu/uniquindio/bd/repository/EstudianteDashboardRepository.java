package co.edu.uniquindio.bd.repository;

import co.edu.uniquindio.bd.dto.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;

import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Timestamp;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
    public List<CursoEstudianteDto> obtenerCursosEstudiante(Integer idEstudiante) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("obtener_cursos_estudiante");

        // Register parameters
        sp.registerStoredProcedureParameter("p_idestudiante", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);

        // Set input parameter
        sp.setParameter("p_idestudiante", idEstudiante);

        // Execute the stored procedure
        sp.execute();

        // Process the results
        List<CursoEstudianteDto> cursos = new ArrayList<>();
        List<Object[]> resultList = sp.getResultList();

        for (Object[] row : resultList) {
            CursoEstudianteDto curso = new CursoEstudianteDto();
            curso.setIdCurso((Integer) row[0]);
            curso.setNombreCurso((String) row[1]);
            curso.setNombreProfesor((String) row[2]);
            curso.setGrupo((String) row[3]);
            cursos.add(curso);
        }

        return cursos;
    }

    public List<TemaDto> obtenerTemasPorCurso(Integer idCurso) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("obtener_temas_por_curso");

        // Register parameters
        sp.registerStoredProcedureParameter("p_idcurso", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);

        // Set input parameter
        sp.setParameter("p_idcurso", idCurso);

        // Execute the stored procedure
        sp.execute();

        // Process the results
        List<TemaDto> temas = new ArrayList<>();
        List<Object[]> resultList = sp.getResultList();

        for (Object[] row : resultList) {
            TemaDto tema = new TemaDto();
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

            examen.setDuracionExamen(((Integer) row[4]));
            examen.setPreguntasExamen((Integer) row[5]);
            examen.setTemaExamen((String) row[6]);
            examen.setGrupoExamen(((String) row[7]));
            examen.setCursoExamen(((String) row[8]));
            examenes.add(examen);
        }

        return examenes;
    }

    public List<PreguntaDto> obtenerPreguntasExamen(Integer idExamen) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("obtener_preguntas_examen");

        // Register parameters
        sp.registerStoredProcedureParameter("p_idexamen", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);

        // Set input parameter
        sp.setParameter("p_idexamen", idExamen);

        // Execute the stored procedure
        sp.execute();

        // Process the results
        List<PreguntaDto> preguntas = new ArrayList<>();
        List<Object[]> resultList = sp.getResultList();

        for (Object[] row : resultList) {
            PreguntaDto pregunta = new PreguntaDto();
            pregunta.setIdPregunta((Integer) row[0]);
            pregunta.setPregunta(clobToString((Clob) row[1]));
            pregunta.setTiempo((Integer) row[2]);
            pregunta.setPorcentaje(((BigDecimal) row[3]).intValue());
            pregunta.setIdTipoPregunta((Integer) row[4]);
            pregunta.setIdExamen((Integer) row[5]);
            preguntas.add(pregunta);
        }

        return preguntas;
    }

    public static String clobToString(Clob clob) {
        if (clob == null) return null;
        StringBuilder sb = new StringBuilder();
        try (Reader reader = clob.getCharacterStream()) {
            char[] buffer = new char[2048];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace(); // o lanza una RuntimeException
        }
        return sb.toString();
    }


    public List<OpcionDto> obtenerOpcionesPorPregunta(Integer idPregunta) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("obtener_opciones_por_pregunta");

        // Register parameters
        sp.registerStoredProcedureParameter("p_idpregunta", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);

        // Set input parameter
        sp.setParameter("p_idpregunta", idPregunta);

        // Execute the stored procedure
        sp.execute();

        // Process the results
        List<OpcionDto> opciones = new ArrayList<>();
        List<Object[]> resultList = sp.getResultList();

        for (Object[] row : resultList) {
            OpcionDto opcion = new OpcionDto();
            opcion.setIdOpcionSeleccion((Integer) row[0]);
            opcion.setTextoOpcion(clobToString((Clob) row[1]));
            opcion.setEscorrecta(((Number) row[2]).intValue());
            opciones.add(opcion);
        }

        return opciones;
    }

    public List<ConceptoDto> obtenerConceptosPorPregunta(Integer idPregunta) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("obtener_conceptos_por_pregunta");

        // Register parameters
        sp.registerStoredProcedureParameter("p_idpregunta", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);

        // Set input parameter
        sp.setParameter("p_idpregunta", idPregunta);

        // Execute the stored procedure
        sp.execute();

        // Process the results
        List<ConceptoDto> conceptos = new ArrayList<>();
        List<Object[]> resultList = sp.getResultList();

        for (Object[] row : resultList) {
            ConceptoDto concepto = new ConceptoDto();
            concepto.setIdConcepto((Integer) row[0]);
            concepto.setTextoConcepto(clobToString((Clob) row[1]));
            concepto.setTextoParejaConcepto(clobToString((Clob) row[2]));
            concepto.setIdPregunta((Integer) row[3]);
            conceptos.add(concepto);
        }

        return conceptos;
    }

    public Integer crearExamenPresentado(Integer idExamen, Integer idEstudiante, String ipDireccion) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("CREAR_EXAMENPRESENTADO");
        sp.registerStoredProcedureParameter("p_idExamen", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_idEstudiante", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_ipDireccion", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_idPresentado", Integer.class, ParameterMode.OUT);

        sp.setParameter("p_idExamen", idExamen);
        sp.setParameter("p_idEstudiante", idEstudiante);
        sp.setParameter("p_ipDireccion", ipDireccion);

        sp.execute();
        return (Integer) sp.getOutputParameterValue("p_idPresentado");
    }


    public Double obtenerNotaExamen(Integer idPresentado) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("OBTENER_NOTA_EXAMEN");
        sp.registerStoredProcedureParameter("p_idPresentado", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_nota", Double.class, ParameterMode.OUT);

        sp.setParameter("p_idPresentado", idPresentado);
        sp.execute();
        return (Double) sp.getOutputParameterValue("p_nota");
    }

    public int getIdExamenPregunta(Integer idPregunta, Integer idExamen) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("OBTENER_ID_EXAMEN_PREGUNTA");
        sp.registerStoredProcedureParameter("p_idpregunta", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_idexamen", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_idexamenpregunta", Integer.class, ParameterMode.OUT);

        sp.setParameter("p_idpregunta", idPregunta);
        sp.setParameter("p_idexamen", idExamen);

        sp.execute();
        return (Integer) sp.getOutputParameterValue("p_idexamenpregunta");
    }

    public void registrarRespuestaSeleccion(int idExamPres, int idExamenPregunta, String texto) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("REGISTRAR_RESPUESTA_SELECCION");
        sp.registerStoredProcedureParameter("p_idExamPres", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_idExamenPregunta", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_texto", String.class, ParameterMode.IN);

        sp.setParameter("p_idExamPres", idExamPres);
        sp.setParameter("p_idExamenPregunta", idExamenPregunta);
        sp.setParameter("p_texto", texto);

        sp.execute();
    }

    public void registrarRespuestaConcepto(int idExamPres, int idExamenPregunta, String orden) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("REGISTRAR_RESPUESTA_CONCEPTO");
        sp.registerStoredProcedureParameter("p_idExamPres", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_idExamenPregunta", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_orden", String.class, ParameterMode.IN);

        sp.setParameter("p_idExamPres", idExamPres);
        sp.setParameter("p_idExamenPregunta", idExamenPregunta);
        sp.setParameter("p_orden", orden);

        sp.execute();
    }

    public void registrarRespuestaSeleccionUnica(int idExamPres, int idExamenPregunta, String texto) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("REGISTRAR_RESPUESTA_UNICA");
        sp.registerStoredProcedureParameter("p_idExamPres", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_idExamenPregunta", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_texto", String.class, ParameterMode.IN);

        sp.setParameter("p_idExamPres", idExamPres);
        sp.setParameter("p_idExamenPregunta", idExamenPregunta);
        sp.setParameter("p_texto", texto);

        sp.execute();
    }

    public BigDecimal finalizarExamen(Integer idPresentado) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("PKG_EXAMEN.FINALIZAR_EXAMEN");

        sp.registerStoredProcedureParameter("p_idExamPres", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_nota", BigDecimal.class, ParameterMode.OUT);

        sp.setParameter("p_idExamPres", idPresentado);

        sp.execute();

        return (BigDecimal) sp.getOutputParameterValue("p_nota");
    }

    public void registrarRespuestaVF(int idExamPres, Integer idPregunta, int flag) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("REGISTRAR_RESPUESTA_VF");

        sp.registerStoredProcedureParameter(1,Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter(2,Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter(3,Integer.class, ParameterMode.IN);

        sp.setParameter(1,idExamPres);
        sp.setParameter(2,idPregunta);
        sp.setParameter(3,flag);

        sp.execute();
    }

    public void registrarRespuestaMultiple(int idExamPres, int idExamenPregunta, String todas) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("REGISTRAR_RESPUESTA_MULTIPLE");

        sp.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);

        sp.setParameter(1,idExamPres);
        sp.setParameter(2,idExamenPregunta);
        sp.setParameter(3,todas);

        sp.execute();
    }

    public void registrarRespuestaOrden(int idExamPres, int idExamenPregunta, String ordenStr) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("REGISTRAR_RESPUESTA_ORDEN");
        sp.registerStoredProcedureParameter("p_idExamPres", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_idExamenPregunta", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_orden", String.class, ParameterMode.IN);  // la lista “;”
        sp.setParameter("p_idExamPres", idExamPres);
        sp.setParameter("p_idExamenPregunta", idExamenPregunta);
        sp.setParameter("p_orden", ordenStr);
        sp.execute();
    }

    public void registrarRespuestaEmparejar(int idExamPres, int idExamenPregunta, String textoPares) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("REGISTRAR_RESPUESTA_EMPAREJAR");
        sp.registerStoredProcedureParameter("p_idExamPres", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_idExamenPregunta", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_orden", String.class, ParameterMode.IN);  // la lista “;”
        sp.setParameter("p_idExamPres", idExamPres);
        sp.setParameter("p_idExamenPregunta", idExamenPregunta);
        sp.setParameter("p_orden", textoPares);
        sp.execute();
    }

    public void registrarRespuestaCompletar(int idExamPres, Integer idPregunta, String respuesta) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("REGISTRAR_RESPUESTA_COMPLETAR");
        sp.registerStoredProcedureParameter("p_idExamPres", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_idExamenPregunta", Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_orden", String.class, ParameterMode.IN);  // la lista “;”
        sp.setParameter("p_idExamPres", idExamPres);
        sp.setParameter("p_idExamenPregunta", idPregunta);
        sp.setParameter("p_orden", respuesta);
        sp.execute();
    }

    public List<ExamenPresentadoDto> obtenerExamenesPresentados(int idestudiante) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("OBTENER_EXAMENES_PRESENTADOS_ESTUDIANTE");

        sp.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter(2, void.class, ParameterMode.REF_CURSOR);

        sp.setParameter(1, idestudiante);

        sp.execute();

        DateTimeFormatter formatterSalida = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        List<ExamenPresentadoDto> examenes = new ArrayList<>();
        List<Object[]> resultList = sp.getResultList();

        for (Object[] row : resultList) {
            ExamenPresentadoDto examen = new ExamenPresentadoDto();
            examen.setIdExamen((Integer) row[0]);

            // Obtén la fecha como java.sql.Timestamp y conviértela a LocalDateTime
            if (row[1] instanceof Timestamp) {
                Timestamp timestamp = (Timestamp) row[1];
                LocalDateTime fecha = timestamp.toLocalDateTime();
                String fechaFormateada = fecha.format(formatterSalida);
                examen.setFecha(fechaFormateada);
            } else {
                examen.setFecha("Fecha inválida");
            }

            examen.setCalificacion((BigDecimal) row[2]);
            examen.setNombreExamen((String) row[3]);

            examenes.add(examen);
        }

        return examenes;
    }
}