package co.edu.uniquindio.bd.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GrupoRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public List<Map<String, Object>> obtenerGruposPorProfesor(int idProfesor) {
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("obtener_grupos_profesor");
            query.registerStoredProcedureParameter("p_idprofesor", Integer.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_resultado", void.class, jakarta.persistence.ParameterMode.REF_CURSOR);
            query.setParameter("p_idprofesor", idProfesor);
            query.execute();

            List<Object[]> results = query.getResultList();
            List<Map<String, Object>> grupos = new ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> grupo = new HashMap<>();
                grupo.put("idgrupo", row[0]);
                grupo.put("nombre_grupo", row[1]);
                grupo.put("anio", row[2]);
                grupo.put("periodo", row[3]);
                grupo.put("nombre_curso", row[4]);
                grupo.put("numero_estudiantes", row[5]);
                grupos.add(grupo);
            }
            return grupos;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los grupos: " + e.getMessage(), e);
        }
    }


    @Transactional
    public List<Map<String, Object>> obtenerTemasPorGrupo(int idGrupo) {
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("obtener_temas_por_grupo");
            query.registerStoredProcedureParameter("p_idgrupo", Integer.class, jakarta.persistence.ParameterMode.IN);
            query.registerStoredProcedureParameter("p_resultado", void.class, jakarta.persistence.ParameterMode.REF_CURSOR);
            query.setParameter("p_idgrupo", idGrupo);
            query.execute();

            List<Object[]> results = query.getResultList();
            List<Map<String, Object>> temas = new ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> tema = new HashMap<>();
                tema.put("idcurso", row[0]);
                tema.put("idunidad", row[1]);
                tema.put("nombre_unidad", row[2]);
                tema.put("idtema", row[3]);
                tema.put("nombre_tema", row[4]);
                tema.put("descripcion_tema", row[5]);
                temas.add(tema);
            }
            return temas;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los temas: " + e.getMessage(), e);
        }
    }
}