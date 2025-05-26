package co.edu.uniquindio.bd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamenPregunta {
    private Integer idExamenPregunta;
    private Double porcentaje;
    private Integer tiempoMaximo;
    private Integer examenIdExamen;
    private Integer preguntaIdPregunta;
}