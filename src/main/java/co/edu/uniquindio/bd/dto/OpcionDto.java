package co.edu.uniquindio.bd.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpcionDto {
    @Id
    private Integer idOpcionSeleccion;
    private String textoOpcion;
    private int escorrecta;
}
