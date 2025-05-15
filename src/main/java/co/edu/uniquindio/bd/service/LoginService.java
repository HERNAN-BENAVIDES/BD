package co.edu.uniquindio.bd.service;

import co.edu.uniquindio.bd.dto.EstudianteDto;
import co.edu.uniquindio.bd.dto.ProfesorDto;
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


    public EstudianteDto authenticateEstudiante(String email, String password) {
        Map<String, Object> result = estudianteRepository.loginEstudiante(email, password);
        return new EstudianteDto((Integer) result.get("id"), (String) result.get("nombre"), (String) result.get("apellido"), (String) result.get("email"), (String) result.get("contrasenia"));
    }

    public ProfesorDto authenticateProfesor(String email, String password) {
        Map<String, Object> result = profesorRepository.loginProfesor(email, password);
        return new ProfesorDto((Integer) result.get("id"), (String) result.get("nombre"), (String) result.get("apellido"), (String) result.get("email"), (String) result.get("contrasenia"));
    }

}
