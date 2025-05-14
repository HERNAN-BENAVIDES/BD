package co.edu.uniquindio.bd.controller;

import co.edu.uniquindio.bd.model.Estudiante;
import co.edu.uniquindio.bd.model.Profesor;
import co.edu.uniquindio.bd.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;


    public Estudiante authenticateEstudiante(String email, String password) {
        return loginService.authenticateEstudiante(email, password);
    }

    public Profesor authenticateProfesor(String email, String password) {
        return loginService.authenticateProfesor(email, password);
    }
}
