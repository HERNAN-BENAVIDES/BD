package co.edu.uniquindio.bd.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Transactional
@Repository
public class ProfesorRepository {

    @PersistenceContext
    private EntityManager em; // para ser inyectado manualmente

    public  Map<String, Object> loginProfesor(String email, String contrasenia) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("login_profesor");

        sp.registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_contrasenia", String.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter("p_id", Integer.class, ParameterMode.OUT);
        sp.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.OUT);
        sp.registerStoredProcedureParameter("p_apellido", String.class, ParameterMode.OUT);
        sp.registerStoredProcedureParameter("p_email_out", String.class, ParameterMode.OUT);
        sp.registerStoredProcedureParameter("p_contrasenia_out", String.class, ParameterMode.OUT);

        sp.setParameter("p_email", email);
        sp.setParameter("p_contrasenia", contrasenia);

        sp.execute();

        Map<String, Object> result = new HashMap<>();
        result.put("id", sp.getOutputParameterValue("p_id"));
        result.put("nombre", sp.getOutputParameterValue("p_nombre"));
        result.put("apellido", sp.getOutputParameterValue("p_apellido"));
        result.put("email", sp.getOutputParameterValue("p_email_out"));
        result.put("contrasenia", sp.getOutputParameterValue("p_contrasenia_out"));

        return result;
    }

}
