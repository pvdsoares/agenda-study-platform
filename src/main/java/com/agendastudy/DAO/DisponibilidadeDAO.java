package com.agendastudy.DAO;

import com.agendastudy.model.SlotDisponivel;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;

/**
 * Gerencia as operações de acesso a dados (DAO) para os Slots de
 * Disponibilidade do Professor.
 * Simula um banco de dados em memória, garantindo segurança para operações
 * multi-thread.
 *
 * @author Paulo Vitor Dias Soares
 * @version 1.0
 * @since 2025-11-29
 */
public class DisponibilidadeDAO {

    // Armazena os slots de tempo que o professor marcou como disponível.
    // Usamos ConcurrentHashMap para ser seguro em ambientes multi-thread.
    private static final Map<String, SlotDisponivel> slots = new ConcurrentHashMap<>();
    private static int proximoId = 1;

    /**
     * Salva um novo SlotDisponivel na simulação de banco de dados.
     * 
     * @param slot O objeto SlotDisponivel a ser salvo.
     * @return O slot salvo (com ID implícito).
     */
    public SlotDisponivel salvarSlot(SlotDisponivel slot) {
        String id = "SLOT_" + proximoId++;
        // Na prática, você salvaria isso no banco. Aqui, usamos o Map.
        slots.put(id, slot);
        return slot;
    }

    /**
     * Busca todos os slots de tempo que o professor marcou como disponível em um
     * período específico.
     * Este é o método CRUCIAL usado pelo ServicoAgendamento para sugerir horários.
     * * @param professorId O ID do professor cuja disponibilidade está sendo
     * consultada.
     * 
     * @param inicio O início da janela de busca.
     * @param fim    O fim da janela de busca.
     * @return Lista de SlotsDisponiveis que se sobrepõem ao período.
     */
    public List<SlotDisponivel> findDisponibilidade(String professorId, LocalDateTime inicio, LocalDateTime fim) {
        // Se a busca for ampla, pode-se retornar uma lista vazia se o ID for nulo
        if (professorId == null) {
            return new ArrayList<>();
        }

        return slots.values().stream()
                .filter(slot ->
                // 1. Filtra pelo professor
                slot.getProfessorId().equals(professorId) &&
                // 2. Checa se o slot de disponibilidade se sobrepõe ao período de busca:
                // (Início do Slot < Fim da Busca) E (Fim do Slot > Início da Busca)
                        slot.getInicio().isBefore(fim) &&
                        slot.getFim().isAfter(inicio))
                .collect(Collectors.toList());
    }

    // Método auxiliar, útil para testes e para limpar o 'banco' em memória
    public static void clear() {
        slots.clear();
        proximoId = 1;
    }
}