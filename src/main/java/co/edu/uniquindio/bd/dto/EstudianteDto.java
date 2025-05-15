package co.edu.uniquindio.bd.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteDto {
    @Id
    private int idestudiante;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasenia;

}
