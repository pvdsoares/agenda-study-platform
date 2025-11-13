package com.agendastudy.DAO;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.agendastudy.model.Aula;
import com.agendastudy.model.Professor;

public class AulaDAO {
    private static Map<String, Aula> aulas = new HashMap<>();
    private static int proximoId = 1; //contagem de id

    // IMPLEMENTAR DEMAIS MÉTODOS RELACIONADOS AQUI
    /**
     * Busca uma aula pelo seu ID.
     */
    public Aula buscarPorId(String id) {
        return aulas.get(id);
    }

    /**
     * Persiste uma nova aula no sistema ou atualiza uma existente. 
     */
    public Aula salvarOuAtualizar(Aula aula) {
        if (aula.getIdAula() == null || aula.getIdAula().isEmpty()) {
            aula.setIdAula("AULA_" + proximoId++);
        }
        aulas.put(aula.getIdAula(), aula); 
        return aula;
    }
    
    /**
     * Busca todas as aulas de um professor (ativas e não canceladas).
     */
    public List<Aula> buscarAulasDoProfessor(Professor professor) {
        if (professor == null) {
            return new ArrayList<>();
        }
        return aulas.values().stream()
                .filter(aula -> aula.getProfessor().getId().equals(professor.getId()) && !aula.isCancelada())
                .collect(Collectors.toList());
    }
    /**
     * 
     * @param idAula ID da aula a ser cancelada
     * 
     */
    public void cancelarAula(String idAula){
        Aula aula = aulas.get(idAula);

        if (aula == null){
            throw new NoSuchElementException("Aula de ID: " + idAula + "não encontrada!");
        }

        if (aula.isCancelada()){
            throw new IllegalStateException("Aula de ID: " + idAula + "já está cancelada!");
        }
        aula.cancelar();
        System.out.println("Aula: " + idAula + " cancelada com sucesso.");
    }
    
}
