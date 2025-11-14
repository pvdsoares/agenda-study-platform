package com.agendastudy.controller;

import java.util.*;
import com.agendastudy.model.Aula;
import com.agendastudy.model.Professor;


public class RelatorioCotroller {
     private Map<Professor, List<Aula>> aulasPorTutor;

    public RelatorioService(Map<Professor, List<Aula>> aulasPorTutor) {
        this.aulasPorTutor = aulasPorTutor;
    }

    public int calcularTotalAulas(Professor tutor, Date inicio, Date fim) {
        List<Aula> aulas = aulasPorTutor.getOrDefault(tutor, new ArrayList<>());
        int count = 0;

        for (Aula aula : aulas) {
            if (!aula.getData().before(inicio) && !aula.getData().after(fim)
                && aula.getStatus().equals("concluida")) {

                count++;
            }
        }
        return count;
    }

    public double calcularTaxaCancelamentoAluno(Professor tutor) {
        List<Aula> aulas = aulasPorTutor.getOrDefault(tutor, new ArrayList<>());
        int canceladasAluno = 0;

        for (Aula aula : aulas) {
            if ("canceladaAluno".equals(aula.getStatus())) {
                canceladasAluno++;
            }
        }

        return aulas.isEmpty() ? 0.0 : (canceladasAluno * 100.0 / aulas.size());
    }

    public double calcularTaxaCancelamentoTutor(Professor tutor) {
        List<Aula> aulas = aulasPorTutor.getOrDefault(tutor, new ArrayList<>());
        int canceladasTutor = 0;

        for (Aula aula : aulas) {
            if ("canceladaTutor".equals(aula.getStatus())) {
                canceladasTutor++;
            }
        }

        return aulas.isEmpty() ? 0.0 : (canceladasTutor * 100.0 / aulas.size());
    }
}

