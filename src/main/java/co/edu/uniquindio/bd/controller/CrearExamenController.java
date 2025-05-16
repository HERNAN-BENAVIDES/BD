package co.edu.uniquindio.bd.controller;

import co.edu.uniquindio.bd.model.Profesor;
import co.edu.uniquindio.bd.service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class CrearExamenController {

    @Autowired
    private GrupoService grupoService;

    /**
     * Obtiene los grupos asociados a un profesor.
     * @param profesor El profesor cuyos grupos se desean obtener.
     * @return Lista de grupos en formato de mapa.
     */
    public List<Map<String, Object>> obtenerGruposPorProfesor(Profesor profesor) {
        return grupoService.obtenerGruposPorProfesor(profesor.getIdprofesor());
    }
}