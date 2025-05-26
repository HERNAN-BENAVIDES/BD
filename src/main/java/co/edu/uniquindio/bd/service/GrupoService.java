package co.edu.uniquindio.bd.service;

import co.edu.uniquindio.bd.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GrupoService {

    @Autowired
    private GrupoRepository grupoRepository;

    public List<Map<String, Object>> obtenerGruposPorProfesor(int idProfesor) {
        return grupoRepository.obtenerGruposPorProfesor(idProfesor);
    }

    public List<Map<String, Object>> obtenerTemasPorGrupo(int idGrupo) {
        return grupoRepository.obtenerTemasPorGrupo(idGrupo);
    }
}