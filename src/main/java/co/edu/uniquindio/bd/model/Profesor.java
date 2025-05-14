package co.edu.uniquindio.bd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Profesor {
    @Id
    private Integer idprofesor;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasenia;
}
