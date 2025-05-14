package co.edu.uniquindio.bd.repository;

import co.edu.uniquindio.bd.model.Estudiante;
import co.edu.uniquindio.bd.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Estudiante, Integer> {
    Estudiante findEstudianteByEmailAndContrasenia(String email, String contrasenia);

    boolean existsByEmail(String email);

    Profesor findProfesorByEmailAndContrasenia(String email, String contrasenia);

}
