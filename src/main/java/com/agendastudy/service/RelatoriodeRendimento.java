package com.agendastudy.controller;

import java.util.*;
import com.agendastudy.model.Aula;
import com.agendastudy.model.Professor;

public class RelatorioController {

    private Map<Professor, List<Aula>> aulasPorTutor;

    // Construtor correto
    public RelatorioController(Map<Professor, List<Aula>> aulasPorTutor) {
        this.aulasPorTutor = aulasPorTutor;
    }

    // Total de aulas concluídas por período
    public int calcularTotalAulas(Professor tutor, Date inicio, Date fim) {
        List<Aula> aulas = aulasPorTutor.getOrDefault(tutor, new ArrayList<>());
        int count = 0;

        for (Aula aula : aulas) {
            Date data = aula.getData();

            boolean dentroPeriodo =
                    !data.before(inicio) && !data.after(fim);

            if (dentroPeriodo && "concluida".equalsIgnoreCase(aula.getStatus())) {
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
            if ("canceladaAluno".equalsIgnoreCase(aula.getStatus())) {
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
            if ("canceladaTutor".equalsIgnoreCase(aula.getStatus())) {
                canceladasTutor++;
            }
        }

        return aulas.isEmpty() ? 0.0 :
                (canceladasTutor * 100.0 / aulas.size());
    }
}
