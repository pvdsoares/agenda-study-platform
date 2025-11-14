package com.agendastudy.DAO;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.agendastudy.model.Aula;
import com.agendastudy.model.Professor;

/**
 * Gerencia as operações de acesso a dados (DAO) para a entidade Aula.
 * Simula um banco de dados de aulas em memória.
 *
 * @author VINICIUS ALVES RIBEIRO SILVA
 * @version 1.0
 * @since 2025-11-11
 */
public class AulaDAO {
    private static Map<String, Aula> aulas = new HashMap<>();
    private static int proximoId = 1; //contagem de id

    /**
     * Busca uma aula pelo seu ID.
     * @param id O ID da aula a ser buscada.
     * @return O objeto Aula, ou null se não for encontrado.
     */
    public Aula buscarPorId(String id) {
        return aulas.get(id);
    }

    /**
     * Persiste (salva ou atualiza) uma aula no sistema.
     * Gera um novo ID se a aula for nova.
     * @param aula A aula a ser salva.
     * @return A aula salva (com ID, se foi gerado).
     */
    public Aula salvarOuAtualizar(Aula aula) {
        if (aula.getIdAula() == null || aula.getIdAula().isEmpty()) {
            aula.setIdAula("AULA_" + proximoId++);
        }
        aulas.put(aula.getIdAula(), aula); 
        return aula;
    }
    
    /**
     * Busca todas as aulas de um professor específico.
     * Filtra apenas aulas que não foram canceladas.
     * @param professor O professor dono das aulas.
     * @return Uma lista de Aulas ativas do professor.
     */
    public List<Aula> buscarAulasDoProfessor(Professor professor) {
        if (professor == null) {
            return new ArrayList<>();
        }
        // Usa Stream para filtrar as aulas do mapa
        return aulas.values().stream()
                .filter(aula -> aula.getProfessor().getId().equals(professor.getId()) && !aula.isCancelada())
                .collect(Collectors.toList());
    }
    
    /**
     * Marca uma aula como cancelada no sistema.
     * @param idAula ID da aula a ser cancelada
     * @throws NoSuchElementException se a aula não for encontrada.
     * @throws IllegalStateException se a aula já estiver cancelada.
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
