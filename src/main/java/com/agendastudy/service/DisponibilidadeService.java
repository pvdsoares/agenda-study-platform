package com.agendastudy.service;

import com.agendastudy.DAO.DisponibilidadeDAO;
import com.agendastudy.model.Professor;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.SlotDisponivel;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Serviço responsável pela lógica de negócios complexa de verificação e
 * cálculo de compatibilidade de horários.
 * É essencial para o ranqueamento de professores.
 *
 * @author Gemini AI (Criada a pedido do usuário)
 * @version 1.0
 * @since 2025-11-30
 */
public class DisponibilidadeService {

    // Injetamos (usamos) o DAO para buscar os dados brutos de disponibilidade.
    private final DisponibilidadeDAO dispDAO;

    // Define a janela de tempo de interesse para o ranqueamento (72 horas)
    private static final long JANELA_PROXIMAS_HORAS = 72;

    public DisponibilidadeService(DisponibilidadeDAO dispDAO) {
        this.dispDAO = dispDAO;
    }

    // --- MÉTODOS REQUERIDOS PELO ProfessorRankingService ---

    /**
     * Verifica se o professor possui algum SlotDisponivel nas próximas 72 horas.
     * Esta é uma regra de negócio que influencia o ranking.
     *
     * @param professor O professor a ser verificado.
     * @return true se houver disponibilidade na janela, false caso contrário.
     */
    public boolean temDisponibilidadeProximas72h(Professor professor) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime futuro = agora.plusHours(JANELA_PROXIMAS_HORAS);

        // Delega a consulta ao DAO
        List<SlotDisponivel> slots = dispDAO.findDisponibilidade(
                professor.getId(),
                agora,
                futuro);

        // Aplica a lógica: se a lista não estiver vazia, há slots.
        return !slots.isEmpty();
    }

    /**
     * Calcula um índice de compatibilidade de horários entre o professor e o
     * estudante.
     * (Lógica de negócios complexa: Simulação).
     *
     * NOTA: Este é um cálculo simplificado. Na prática, envolveria comparar
     * os SlotsDisponiveis do professor com os horários preferidos do estudante.
     *
     * @param professor O professor.
     * @param estudante O estudante.
     * @return Um índice double (0.0 a 1.0) que mede a compatibilidade.
     */
    public double calcularCompatibilidadeHorarios(Professor professor, Estudante estudante) {
        // Para fins de ranqueamento, esta lógica pode ser complexa.
        // Aqui, simulamos o cálculo.

        // 1. Busca os slots do professor nas próximas semanas (ex: 2 semanas).
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime periodoBusca = agora.plus(14, ChronoUnit.DAYS);

        List<SlotDisponivel> slotsDoProfessor = dispDAO.findDisponibilidade(
                professor.getId(),
                agora,
                periodoBusca);

        // Lógica de Negócio de Compatibilidade:
        // A lógica real compararia os horários preferenciais do Estudante
        // (supondo que o Estudante tenha um atributo List<SlotDisponivel>
        // getPreferenciasHorario())
        // com os slotsDoProfessor.

        // Simulação:
        if (slotsDoProfessor.isEmpty()) {
            return 0.0; // Nenhuma compatibilidade
        }

        // Se o professor tiver muitos slots, assumimos que a compatibilidade é maior.
        // (Isso é apenas um placeholder para a lógica real de negócios)
        double maxSlotsPara100PorCento = 30.0;
        double compatibilidadeBruta = (double) slotsDoProfessor.size() / maxSlotsPara100PorCento;

        // Limita o índice entre 0.0 e 1.0.
        return Math.min(1.0, compatibilidadeBruta);
    }
}