package com.agendastudy.DAO;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.time.LocalDateTime;
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
 * @version 1.1
 */
public class AulaDAO {

    /**
     * 
     * Trocado para 'ConcurrentHashMap'
     * para que o SCRUM-114 (Notificações) e o SCRUM-35 (Agendamento)
     * possam acessar o mapa ao mesmo tempo sem quebrar a aplicação.
     */
    private static Map<String, Aula> aulas = new ConcurrentHashMap<>();
    private static int proximoId = 1; // contagem de id

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
     * Busca aulas agendadas para um usuário (Professor ou Estudante) DENTRO de um
     * período específico.
     *
     * @param usuarioId O ID do Professor ou Estudante.
     * @param inicio    O início do período de busca.
     * @param fim       O fim do período de busca.
     * @return Lista de Aulas ativas em que o usuário está envolvido no período.
     */
    public List<Aula> findAulasPorPeriodo(String usuarioId, LocalDateTime inicio, LocalDateTime fim) {
        if (usuarioId == null || inicio == null || fim == null) {
            return new ArrayList<>();
        }

        return aulas.values().stream()
                .filter(aula ->
                // 1. O usuário é o Professor OU o Estudante
                (aula.getProfessor().getId().equals(usuarioId) ||
                        (aula.getEstudante() != null && aula.getEstudante().getId().equals(usuarioId))) &&

                // 2. A aula não está cancelada
                        !aula.isCancelada() &&

                        // 3. Checa se a aula se sobrepõe ao período de busca:
                        aula.getDataHora().isBefore(fim) &&
                        aula.getFimAula().isAfter(inicio))
                .collect(Collectors.toList());
    }

    /**
     * Retorna todas as aulas cadastradas no sistema.
     * 
     * @return Lista de todas as aulas
     */
    public List<Aula> buscarTodasAulas() {
        return new ArrayList<>(aulas.values());
    }

}