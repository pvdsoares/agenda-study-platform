package com.agendastudy.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.agendastudy.DAO.AvaliacaoDAO;
import com.agendastudy.DAO.ProfessorDAO;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.Professor;

/**
 * Service para ranking de professores (Antigo TutorRankingService).
 *
 * @author Lucas Chagas
 * @version 1.1
 */
public class TutorRankingService {

    private final ProfessorDAO professorDAO;
    private final AvaliacaoDAO avaliacaoDAO;
    private final DisponibilidadeService dispService;

    private static final double W_AVALIACAO = 0.5;
    private static final double W_COMPATIBILIDADE = 0.4;
    private static final double W_CANCELAMENTO = 0.1;

    // Constante para normalizar a pontuação (assumindo um sistema de 5 estrelas)
    private static final double MAX_AVALIACAO_SCORE = 5.0;

    // Construtor para injeção de dependência
    public TutorRankingService(ProfessorDAO professorDAO,
            AvaliacaoDAO avaliacaoDAO,
            DisponibilidadeService dispService) {
        this.professorDAO = professorDAO;
        this.avaliacaoDAO = avaliacaoDAO;
        this.dispService = dispService;
    }

    private record ProfessorScore(Professor professor, double score) {
    }

    /**
     * Retorna os N melhores professores para uma dada disciplina.
     * 
     * @param estudante  O estudante que está buscando.
     * @param disciplina O nome da disciplina (String).
     * @param N          O número máximo de resultados.
     * @return Lista de professores ranqueados.
     */
    public List<Professor> getTopNProfessores(Estudante estudante, String disciplina, int N) {

        List<Professor> todosOsProfessores = professorDAO.findByDisciplina(disciplina);

        return todosOsProfessores.stream()
                // 1. Filtra apenas os que têm disponibilidade
                .filter(prof -> dispService.temDisponibilidadeProximas72h(prof))

                // 2. Mapeia para score
                .map(prof -> new ProfessorScore(prof, calcularScoreRanking(prof, estudante)))

                // 3. Ordena pelo score
                .sorted(Comparator.comparingDouble(ProfessorScore::score).reversed())

                // 4. Limita
                .limit(N)

                // 5. Mapeia de volta
                .map(ProfessorScore::professor)

                .collect(Collectors.toList());
    }

    private double calcularScoreRanking(Professor professor, Estudante estudante) {

        double mediaAvaliacoes = avaliacaoDAO.getMediaAvaliacoes(professor.getId());
        double taxaCancelamento = professor.getTaxaCancelamento();
        double indiceCompatibilidade = dispService.calcularCompatibilidadeHorarios(professor, estudante);

        double scoreAvaliacaoNormalizado = (mediaAvaliacoes / MAX_AVALIACAO_SCORE);

        double score = (scoreAvaliacaoNormalizado * W_AVALIACAO) +
                (indiceCompatibilidade * W_COMPATIBILIDADE) -
                (taxaCancelamento * W_CANCELAMENTO);

        return Math.max(0.0, score);
    }
}
