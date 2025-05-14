package co.edu.uniquindio.bd.service;

import co.edu.uniquindio.bd.model.Estudiante;
import co.edu.uniquindio.bd.model.Profesor;
import co.edu.uniquindio.bd.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    /**
     * Authenticates a user based on email, password, and role
     * @param email User's email
     * @param password User's password
     * @param role User's role (Estudiante or Profesor)
     * @return The authenticated user object or null if authentication fails
     */
    public Object authenticate(String email, String password, String role) {
        if (role.equals("Estudiante")) {
            return loginRepository.findEstudianteByEmailAndContrasenia(email, password);
        } else if (role.equals("Profesor")) {
            return loginRepository.findProfesorByEmailAndContrasenia(email, password);
        }
        return null;
    }
}
