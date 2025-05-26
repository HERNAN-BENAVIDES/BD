package co.edu.uniquindio.bd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamenPresentadoDto {
    private Integer idExamen;
    private String fecha;
    private BigDecimal calificacion;
    private String nombreExamen;
}
