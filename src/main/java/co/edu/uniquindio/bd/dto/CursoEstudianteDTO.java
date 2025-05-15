package co.edu.uniquindio.bd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for representing course data returned by the obtener_cursos_estudiante stored procedure
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CursoEstudianteDTO {
    private Integer idCurso;
    private String nombreCurso;
    private String nombreProfesor;
    private String grupo;
}