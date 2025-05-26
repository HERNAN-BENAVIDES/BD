package co.edu.uniquindio.bd.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Examen {
    private Integer idExamen;
    private String nombre;
    private String descripcion;
    private Integer totalPreguntas;
    private Integer preguntasPorEstudiante;
    private Integer tiempoMaximo;
    private String fechaDisponibleInicio;
    private String fechaDisponibleFin;
    private Double porcentajeCurso;
    private Double notaAprobacion;
    private Integer grupoIdGrupo;
    private Integer temaIdTema;
    private Integer profesorIdProfesor;
}