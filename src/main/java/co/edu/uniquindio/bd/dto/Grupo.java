package co.edu.uniquindio.bd.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Grupo {
    @Id
    private Integer idgrupo;
    private String nombre;
    private Integer anio;
    private Integer periodo;
    private Integer cursoIdcurso;
}