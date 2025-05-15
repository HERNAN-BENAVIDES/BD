package co.edu.uniquindio.bd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemaDTO {
    private int idTema;
    private String nombre;
    private String unidad;
    private String descripcion;
}

