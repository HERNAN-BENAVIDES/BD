package co.edu.uniquindio.bd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Estudiante {
    @Id
    private int idestudiante;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasenia;
}
