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
public class ConceptoDto {
    @Id
    private Integer idConcepto;
    private String textoConcepto;
    private String textoParejaConcepto;
    private Integer idPregunta;
}
