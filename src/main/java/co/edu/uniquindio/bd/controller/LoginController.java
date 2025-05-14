package co.edu.uniquindio.bd.controller;

import co.edu.uniquindio.bd.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    /**
     * Authenticates a user based on email, password, and role
     * @param email User's email
     * @param password User's password
     * @param role User's role (Estudiante or Profesor)
     * @return The authenticated user object or null if authentication fails
     */
    public Object authenticateUser(String email, String password, String role) {
        return loginService.authenticate(email, password, role);
    }
}
