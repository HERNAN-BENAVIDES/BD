package co.edu.uniquindio.bd.service;

import co.edu.uniquindio.bd.model.Estudiante;
import co.edu.uniquindio.bd.model.Profesor;
import co.edu.uniquindio.bd.repository.EstudianteRepository;
import co.edu.uniquindio.bd.repository.ProfesorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    /**
     * Authenticates a user based on email, password, and role
     * @param email User's email
     * @param password User's password
     * @param role User's role (Estudiante or Profesor)
     * @return The authenticated user object or null if authentication fails
     */
    public Object authenticate(String email, String password, String role) {
        if (role.equals("Estudiante")) {
            return estudianteRepository.loginEstudiante(email, password);
        } else if (role.equals("Profesor")) {

        }
        return null;
    }

    public Estudiante authenticateEstudiante(String email, String password) {
        Map<String, Object> result = estudianteRepository.loginEstudiante(email, password);
        return new Estudiante((Integer) result.get("id"), (String) result.get("nombre"), (String) result.get("apellido"), (String) result.get("email"), (String) result.get("contrasenia"));
    }

    public Profesor authenticateProfesor(String email, String password) {
        Map<String, Object> result = profesorRepository.loginProfesor(email, password);
        return new Profesor((Integer) result.get("id"), (String) result.get("nombre"), (String) result.get("apellido"), (String) result.get("email"), (String) result.get("contrasenia"));
    }

}
