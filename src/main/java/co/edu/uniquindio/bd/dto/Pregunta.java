package co.edu.uniquindio.bd.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pregunta {
    private Integer idPregunta;
    private String textoPregunta;
    private Integer tipoPreguntaId;
    private String tipoPreguntaNombre;
    private Integer nivelId;
    private String nivelNombre;
    private String profesorNombre;
    private Timestamp fechaCreacion;
    private String opciones;
    private String fuenteOpciones;
}