package com.agendastudy.DAO;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap; // Adicionado

import com.agendastudy.model.Aula;
import com.agendastudy.model.Professor;
import com.agendastudy.model.StatusAula;

/**
 * Gerencia as operações de acesso a dados (DAO) para a entidade Aula.
 * Simula um banco de dados de aulas em memória.
 *
 * @author VINICIUS ALVES RIBEIRO SILVA
 * @version 1.0
 */
public class AulaDAO {
    
    /** * 
     * Trocado para 'ConcurrentHashMap'
     * para que o SCRUM-114 (Notificações) e o SCRUM-35 (Agendamento)
     * possam acessar o mapa ao mesmo tempo sem quebrar a aplicação.
     */
    private static Map<String, Aula> aulas = new ConcurrentHashMap<>();
    private static int proximoId = 1; //contagem de id

    /**
     * Busca uma aula pelo seu ID.
     */
    public Aula buscarPorId(String id) {
        return aulas.get(id);
    }

    /**
     * Persiste (salva ou atualiza) uma aula no sistema.
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
     */
    public void cancelarAula(String idAula){
        Aula aula = aulas.get(idAula);

        if (aula == null){
            throw new NoSuchElementException("Aula de ID: " + idAula + "não encontrada!");
        }

        if ((aula.getStatus() == StatusAula.CANCELADA_ALUNO) || (aula.getStatus() == StatusAula.CANCELADA_PROFESSOR)){
            throw new IllegalStateException("Aula de ID: " + idAula + "já está cancelada!");
        }
        aula.setStatus(StatusAula.CANCELADA_ALUNO);
        System.out.println("Aula: " + idAula + " cancelada com sucesso.");
    }

    /**
     * Retorna todas as aulas cadastradas no sistema.
     * @return Lista de todas as aulas
     */
    public List<Aula> buscarTodasAulas() {
        return new ArrayList<>(aulas.values());
    }

}