package com.agendastudy.service;


import java.time.LocalDateTime;
import java.time.ZoneId; 
import java.util.*;
import com.agendastudy.model.Aula;
import com.agendastudy.model.Professor;
import com.agendastudy.model.StatusAula;

public class RelatoriodeRendimento {

    private Map<Professor, List<Aula>> aulasPorTutor;

    // Construtor correto
    public RelatoriodeRendimento(Map<Professor, List<Aula>> aulasPorTutor) {
        this.aulasPorTutor = aulasPorTutor;
    }

    // Total de aulas concluídas por período
   public int calcularTotalAulas(Professor tutor, LocalDateTime inicio, LocalDateTime fim) { 
        List<Aula> aulas = aulasPorTutor.getOrDefault(tutor, new ArrayList<>());
        int count = 0;

        for (Aula aula : aulas) {
LocalDateTime data = aula.getDataHora();
            boolean dentroPeriodo =
 !data.isBefore(inicio) && !data.isAfter(fim);

            if (dentroPeriodo && aula.getStatus() == StatusAula.CONCLUIDA) { 
                count++;
            }
        }
        return count;
    }

    // Taxa de cancelamento pelo aluno
    public double calcularTaxaCancelamentoAluno(Professor tutor) {
        List<Aula> aulas = aulasPorTutor.getOrDefault(tutor, new ArrayList<>());

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
    public double calcularTaxaCancelamentoTutor(Professor tutor) {
        List<Aula> aulas = aulasPorTutor.getOrDefault(tutor, new ArrayList<>());

        int canceladasTutor = 0;
        for (Aula aula : aulas) {
            if (aula.getStatus() == StatusAula.CANCELADA_PROFESSOR) {
                canceladasTutor++;
            }
        }

        return aulas.isEmpty() ? 0.0 :
                (canceladasTutor * 100.0 / aulas.size());
    }
}
