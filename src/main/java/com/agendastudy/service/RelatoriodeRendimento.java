package com.agendastudy.service;

import java.time.LocalDateTime;
import java.util.*;
import com.agendastudy.model.Aula;
import com.agendastudy.model.Professor;
import com.agendastudy.model.StatusAula;

/**
 * Serviço responsável por gerar relatórios de rendimento para os professores.
 * A classe consolida indicadores como total de aulas concluídas por período
 * e taxas de cancelamento, baseando-se no mapa de aulas fornecido no construtor.
 *
 * @author LUARA GABRIELLI 
 * @version 1.1
 * @since 2025-11-14
 */
public class RelatoriodeRendimento {

    private Map<Professor, List<Aula>> aulasPorProfessor;

    public RelatoriodeRendimento(Map<Professor, List<Aula>> aulasPorProfessor) {
        this.aulasPorProfessor = aulasPorProfessor;
    }

    // Total de aulas concluídas por período
    public int calcularTotalAulas(Professor professor, LocalDateTime inicio, LocalDateTime fim) { 
        List<Aula> aulas = aulasPorProfessor.getOrDefault(professor, new ArrayList<>());
        int count = 0;

        for (Aula aula : aulas) {
            LocalDateTime data = aula.getDataHora();

            boolean dentroPeriodo = 
                !data.isBefore(inicio) && 
                !data.isAfter(fim);

            if (dentroPeriodo && aula.getStatus() == StatusAula.CONCLUIDA) { 
                count++;
            }
        }
        return count;
    }

    // Taxa de cancelamento pelo aluno
    public double calcularTaxaCancelamentoAluno(Professor professor) {
        List<Aula> aulas = aulasPorProfessor.getOrDefault(professor, new ArrayList<>());

        int canceladasAluno = 0;
        for (Aula aula : aulas) {
            if (aula.getStatus() == StatusAula.CANCELADA_ALUNO) { 
                canceladasAluno++;
            }
        }

        return aulas.isEmpty() ? 0.0 :
                (canceladasAluno * 100.0 / aulas.size());
    }

    // Taxa de cancelamento pelo professor
    public double calcularTaxaCancelamentoProfessor(Professor professor) {
        List<Aula> aulas = aulasPorProfessor.getOrDefault(professor, new ArrayList<>());

        int canceladasProfessor = 0;
        for (Aula aula : aulas) {
            if (aula.getStatus() == StatusAula.CANCELADA_PROFESSOR) {
                canceladasProfessor++;
            }
        }

        return aulas.isEmpty() ? 0.0 :
                (canceladasProfessor * 100.0 / aulas.size());
    }

    // cancelamentos por aluno
    public Map<String, Integer> calcularCancelamentosPorAluno(Professor professor) {
        List<Aula> aulas = aulasPorProfessor.getOrDefault(professor, new ArrayList<>());

        Map<String, Integer> cancelamentos = new HashMap<>();

        for (Aula aula : aulas) {
            if (aula.getStatus() == StatusAula.CANCELADA_ALUNO) {
                String nomeAluno = aula.getEstudante().getNome(); // ajuste se o método for diferente

                cancelamentos.put(nomeAluno,
                        cancelamentos.getOrDefault(nomeAluno, 0) + 1);
            }
        }

        return cancelamentos;
    }
}
