package com.agendastudy.service;

import com.agendastudy.DAO.AulaDAO;
import com.agendastudy.DAO.DisponibilidadeDAO;
import com.agendastudy.model.Aula;
import com.agendastudy.model.Professor;
import com.agendastudy.model.SlotDisponivel;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.HorarioSugerido;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ReagendamentoService (SCRUM-81).
 *
 *
 * @author Lucas Chagas
 * @version 1.1 (Ajustado por Paulo Vitor)
 * @since 2025-11-29
 */

public class ReagendamentoService {

    private final DisponibilidadeDAO dispDAO;
    private final AulaDAO aulaDAO;

    private static final long PERIODO_BUSCA_DIAS = 7;
    private static final long INCREMENTO_SUGESTAO_MINUTOS = 30;

    // Construtor para Injeção de Dependência
    public ReagendamentoService(DisponibilidadeDAO dispDAO, AulaDAO aulaDAO) {
        this.dispDAO = dispDAO;
        this.aulaDAO = aulaDAO;
    }

    public List<HorarioSugerido> getSugestoesReagendamento(Aula aulaParaReagendar) {

        Professor professor = aulaParaReagendar.getProfessor();
        Estudante estudante = aulaParaReagendar.getEstudante();
        long duracaoMinutos = aulaParaReagendar.getDuracaoMinutos();

        // Critério de Aceite: Busca nos próximos 7 dias APÓS o horário ATUAL
        LocalDateTime inicioBusca = LocalDateTime.now();
        LocalDateTime fimBusca = inicioBusca.plusDays(PERIODO_BUSCA_DIAS);

        // --- 1. Busca e Unificação de Horários Ocupados ---

        // 1. Aulas já agendadas para o professor
        List<Aula> aulasOcupadasProfessor = aulaDAO.findAulasPorPeriodo(professor.getId(), inicioBusca, fimBusca);
        // 2. Aulas já agendadas para o aluno (para evitar conflitos na agenda dele)
        List<Aula> aulasOcupadasEstudante = aulaDAO.findAulasPorPeriodo(estudante.getId(), inicioBusca, fimBusca);

        // Unifica os horários ocupados de ambos
        Set<Aula> todosHorariosOcupados = new HashSet<>(aulasOcupadasProfessor);
        todosHorariosOcupados.addAll(aulasOcupadasEstudante);

        // CRUCIAL: Remove a aula antiga da lista de ocupados.
        // FUNCIONA APENAS SE AULA TIVER equals() e hashCode() baseados no idAula!
        todosHorariosOcupados.remove(aulaParaReagendar);

        // --- 2. Busca e Filtro por Disponibilidade ---

        // Busca os blocos de tempo em que o professor disse que está disponível.
        List<SlotDisponivel> disponibilidadeProfessor = dispDAO.findDisponibilidade(
                professor.getId(), inicioBusca, fimBusca);

        List<HorarioSugerido> sugestoes = new ArrayList<>();

        for (SlotDisponivel slot : disponibilidadeProfessor) {

            // Inicia a sugestão no começo do bloco de disponibilidade ou no próximo
            // INCREMENTO
            LocalDateTime inicioSlotProposto = slot.getInicio();

            // Percorre o slot de disponibilidade (do professor)
            while (!inicioSlotProposto.plusMinutes(duracaoMinutos).isAfter(slot.getFim())) {

                // Pula horários que já passaram (se o bloco de disponibilidade começar no
                // passado)
                if (inicioSlotProposto.isBefore(LocalDateTime.now())) {
                    inicioSlotProposto = inicioSlotProposto.plusMinutes(INCREMENTO_SUGESTAO_MINUTOS);
                    continue;
                }

                LocalDateTime fimSlotProposto = inicioSlotProposto.plusMinutes(duracaoMinutos);

                // Checa conflito contra a lista unificada
                boolean conflito = temConflito(inicioSlotProposto, fimSlotProposto, todosHorariosOcupados);

                if (!conflito) {
                    sugestoes.add(new HorarioSugerido(inicioSlotProposto, fimSlotProposto));
                }

                // Usa a constante (Melhoria 1)
                inicioSlotProposto = inicioSlotProposto.plusMinutes(INCREMENTO_SUGESTAO_MINUTOS);
            }
        }

        return sugestoes;
    }

    /**
     * Verifica se um horário proposto (inicio/fim) sobrepõe qualquer aula agendada.
     * A lógica de sobreposição (A.fim > B.inicio) E (A.inicio < B.fim) está
     * correta.
     * 
     * @param aulasAgendadas Usamos 'Collection' pois agora recebemos um 'Set'.
     */
    private boolean temConflito(LocalDateTime inicioProposto, LocalDateTime fimProposto,
            Collection<Aula> aulasAgendadas) {
        for (Aula aula : aulasAgendadas) {
            // Lógica de conflito: Se o fim de A for APÓS o início de B E o início de A for
            // ANTES do fim de B, há sobreposição.
            if (fimProposto.isAfter(aula.getDataHora()) && inicioProposto.isBefore(aula.getFimAula())) {
                // Sim, há sobreposição (conflito)
                return true;
            }
        }
        // Não foi encontrado nenhum conflito
        return false;
    }
}
