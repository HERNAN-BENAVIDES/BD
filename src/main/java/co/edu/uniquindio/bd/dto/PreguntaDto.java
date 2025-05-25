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
public class PreguntaDto {
    @Id
    private Integer idPregunta;
    private String pregunta;
    private Integer tiempo;
    private int porcentaje;
    private Integer idTipoPregunta;
    private Integer idExamen;

}
