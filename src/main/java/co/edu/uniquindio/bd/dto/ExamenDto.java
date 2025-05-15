package co.edu.uniquindio.bd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamenDto {
    private Integer idExamen;
    private String nombreExamen;
    private String horaExamen;
    private String fechaExamen;
    private String duracionExamen;
    private Integer preguntasExamen;
    private String temaExamen;
    private String grupoExamen;
    private String cursoExamen;
}
