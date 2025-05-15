package co.edu.uniquindio.bd.controller;


import co.edu.uniquindio.bd.dto.EstudianteDto;
import co.edu.uniquindio.bd.dto.ProfesorDto;
import co.edu.uniquindio.bd.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;


    public EstudianteDto authenticateEstudiante(String email, String password) {
        return loginService.authenticateEstudiante(email, password);
    }

    public ProfesorDto authenticateProfesor(String email, String password) {
        return loginService.authenticateProfesor(email, password);
    }
}
