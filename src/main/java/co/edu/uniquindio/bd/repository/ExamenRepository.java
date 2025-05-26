package co.edu.uniquindio.bd.repository;

import co.edu.uniquindio.bd.model.Examen;
import co.edu.uniquindio.bd.model.ExamenPregunta;
import co.edu.uniquindio.bd.model.Pregunta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class ExamenRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager em;

    public Examen crearExamen(
            String nombre, String descripcion, String fechaInicio, String horaInicio,
            String fechaFin, String horaFin, String tiempoMaximo, String idProfesor,
            String idGrupo, String porcentajeCurso, String notaAprobacion,
            String totalPreguntas, String preguntasPorEstudiante, String idTema,
            String preguntasManuales, String preguntasAleatorias) {

        StoredProcedureQuery sp = em.createStoredProcedureQuery("crear_examen");

        // Registrar parámetros de entrada (16, all VARCHAR2)
        sp.registerStoredProcedureParameter("p_nombre_examen", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_descripcion_examen", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_fecha_inicio", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_hora_inicio", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_fecha_final", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_hora_fin", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_tiempo_maximo", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_id_profesor", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_id_grupo", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_porcentaje_curso", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_nota_aprobacion", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_total_preguntas", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_preguntas_por_estudiante", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_id_tema", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_preguntas_manuales", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_preguntas_aleatorias", String.class, ParameterMode.IN);

        // Registrar parámetros de salida (13)
        sp.registerStoredProcedureParameter("p_idexamen", Long.class, ParameterMode.OUT); // NUMBER
        sp.registerStoredProcedureParameter("p_nombre_out", String.class, ParameterMode.OUT); // VARCHAR2
        sp.registerStoredProcedureParameter("p_descripcion_out", String.class, ParameterMode.OUT); // VARCHAR2
        sp.registerStoredProcedureParameter("p_totalpreguntas_out", Long.class, ParameterMode.OUT); // NUMBER
        sp.registerStoredProcedureParameter("p_preguntasporestudiante_out", Long.class, ParameterMode.OUT); // NUMBER
        sp.registerStoredProcedureParameter("p_tiempomaximo_out", Long.class, ParameterMode.OUT); // NUMBER
        sp.registerStoredProcedureParameter("p_fechadisponibleinicio_out", String.class, ParameterMode.OUT); // VARCHAR2
        sp.registerStoredProcedureParameter("p_fechadisponiblefin_out", String.class, ParameterMode.OUT); // VARCHAR2
        sp.registerStoredProcedureParameter("p_porcentajecurso_out", Double.class, ParameterMode.OUT); // NUMBER
        sp.registerStoredProcedureParameter("p_notaaprobacion_out", Double.class, ParameterMode.OUT); // NUMBER
        sp.registerStoredProcedureParameter("p_grupo_idgrupo_out", Long.class, ParameterMode.OUT); // NUMBER
        sp.registerStoredProcedureParameter("p_tema_idtema_out", Long.class, ParameterMode.OUT); // NUMBER
        sp.registerStoredProcedureParameter("p_profesor_idprofesor_out", Long.class, ParameterMode.OUT); // NUMBER

        // Establecer parámetros de entrada
        sp.setParameter("p_nombre_examen", nombre);
        sp.setParameter("p_descripcion_examen", descripcion);
        sp.setParameter("p_fecha_inicio", fechaInicio);
        sp.setParameter("p_hora_inicio", horaInicio);
        sp.setParameter("p_fecha_final", fechaFin);
        sp.setParameter("p_hora_fin", horaFin);
        sp.setParameter("p_tiempo_maximo", tiempoMaximo);
        sp.setParameter("p_id_profesor", idProfesor);
        sp.setParameter("p_id_grupo", idGrupo);
        sp.setParameter("p_porcentaje_curso", porcentajeCurso);
        sp.setParameter("p_nota_aprobacion", notaAprobacion);
        sp.setParameter("p_total_preguntas", totalPreguntas);
        sp.setParameter("p_preguntas_por_estudiante", preguntasPorEstudiante);
        sp.setParameter("p_id_tema", idTema);
        sp.setParameter("p_preguntas_manuales", preguntasManuales);
        sp.setParameter("p_preguntas_aleatorias", preguntasAleatorias);

        // Ejecutar el procedimiento
        sp.execute();

        // Crear y retornar ExamenDto con los parámetros de salida
        return new Examen(
                ((Long) sp.getOutputParameterValue("p_idexamen")).intValue(),
                (String) sp.getOutputParameterValue("p_nombre_out"),
                (String) sp.getOutputParameterValue("p_descripcion_out"),
                ((Long) sp.getOutputParameterValue("p_totalpreguntas_out")).intValue(),
                ((Long) sp.getOutputParameterValue("p_preguntasporestudiante_out")).intValue(),
                ((Long) sp.getOutputParameterValue("p_tiempomaximo_out")).intValue(),
                (String) sp.getOutputParameterValue("p_fechadisponibleinicio_out"),
                (String) sp.getOutputParameterValue("p_fechadisponiblefin_out"),
                (Double) sp.getOutputParameterValue("p_porcentajecurso_out"),
                (Double) sp.getOutputParameterValue("p_notaaprobacion_out"),
                ((Long) sp.getOutputParameterValue("p_grupo_idgrupo_out")).intValue(),
                ((Long) sp.getOutputParameterValue("p_tema_idtema_out")).intValue(),
                ((Long) sp.getOutputParameterValue("p_profesor_idprofesor_out")).intValue()
        );
    }

    public List<Pregunta> obtenerPreguntasOpciones(int idTema) throws SQLException {
        List<Pregunta> preguntas = new ArrayList<>();

        String sql = "{CALL obtener_preguntas_opciones(?,?,?,?)}";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setLong(1, idTema);
            cs.registerOutParameter(2, java.sql.Types.REF_CURSOR);
            cs.registerOutParameter(3, java.sql.Types.NUMERIC);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.execute();

            int errorCode = cs.getInt(3);
            String errorMsg = cs.getString(4);
            if (errorCode != 0) {
                throw new SQLException("Error en procedimiento: " + errorMsg, "ORA-" + errorCode);
            }

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                while (rs.next()) {
                    Pregunta pregunta = new Pregunta();
                    pregunta.setIdPregunta(rs.getInt("idpregunta")); // Changed to getInt
                    pregunta.setTextoPregunta(rs.getString("textopregunta"));
                    pregunta.setTipoPreguntaId(rs.getInt("tipopregunta_idtipopregunta"));
                    pregunta.setTipoPreguntaNombre(rs.getString("tipopregunta_nombre"));
                    pregunta.setNivelId(rs.getInt("nivel_idnivel"));
                    pregunta.setNivelNombre(rs.getString("nivel_nombre"));
                    pregunta.setProfesorNombre(rs.getString("profesor_nombre"));
                    pregunta.setFechaCreacion(rs.getTimestamp("fechacreacion"));
                    pregunta.setOpciones(rs.getString("opciones"));
                    pregunta.setFuenteOpciones(rs.getString("fuente_opciones"));
                    preguntas.add(pregunta);
                }
            }
            return 0;
        });

        return preguntas;
    }

    public ExamenPregunta insertarExamenPregunta(Double porcentaje, Integer tiempoMaximo, Integer examenIdExamen, Integer preguntaIdPregunta) throws SQLException {
        String sql = "{CALL insertar_examenpregunta(?,?,?,?,?,?,?)}";
        return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setDouble(1, porcentaje);
            cs.setInt(2, tiempoMaximo);
            cs.setInt(3, examenIdExamen);
            cs.setInt(4, preguntaIdPregunta);
            cs.registerOutParameter(5, java.sql.Types.NUMERIC); // idexamenpregunta
            cs.registerOutParameter(6, java.sql.Types.NUMERIC); // error_code
            cs.registerOutParameter(7, java.sql.Types.VARCHAR); // error_msg
            cs.execute();

            int errorCode = cs.getInt(6);
            String errorMsg = cs.getString(7);
            if (errorCode != 0) {
                throw new SQLException("Error en procedimiento: " + errorMsg, "ORA-" + errorCode);
            }

            ExamenPregunta examenPregunta = new ExamenPregunta();
            examenPregunta.setIdExamenPregunta(cs.getInt(5));
            examenPregunta.setPorcentaje(porcentaje);
            examenPregunta.setTiempoMaximo(tiempoMaximo);
            examenPregunta.setExamenIdExamen(examenIdExamen);
            examenPregunta.setPreguntaIdPregunta(preguntaIdPregunta);
            return examenPregunta;
        });
    }


    public void completarExamen(int examenId) throws SQLException {
        String sql = "{call completar_examen(?)}";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setInt(1, examenId);
            cs.execute();

            // No output parameters to check, but SQLException will be thrown if the procedure fails
            return 0;
        });
    }
}