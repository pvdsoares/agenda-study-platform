package com.agendastudy.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.agendastudy.DAO.ProfessorDAO;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.Professor;

/**
 * TutorRakingService (SCRUM-76).
 *
 *
 * @author Lucas Chagas
 * @version 1.0
 */

public class ProfessorRankingService {

    private final ProfessorDAO tutorRepo;
    private final AvaliacaoService avaliacaoService;
    private final DisponibilidadeService dispService;

    private static final double W_AVALIACAO = 0.5;
    private static final double W_COMPATIBILIDADE = 0.4;
    private static final double W_CANCELAMENTO = 0.1;

    // Constante para normalizar a pontuação (assumindo um sistema de 5 estrelas)
    private static final double MAX_AVALIACAO_SCORE = 5.0;

    // Construtor para injeção de dependência (Ex: @Autowired no Spring)
    public ProfessorRankingService(ProfessorDAO tutorRepo,
            AvaliacaoService avaliacaoService,
            DisponibilidadeService dispService) {
        this.tutorRepo = tutorRepo;
        this.avaliacaoService = avaliacaoService;
        this.dispService = dispService;
    }

    private record TutorScore(Professor Professor, double score) {
    }

    public List<Professor> getTopNTutores(Estudante estudante, String nomeDisciplina, int N) {

        List<Professor> todosOsTutores = tutorRepo.findByDisciplina(nomeDisciplina);

        // O processo original (loop + stream) foi refatorado para um pipeline de stream
        // único,
        // que é mais funcional e legível.
        return todosOsTutores.stream()
                // 1. Filtra apenas os que têm disponibilidade (lógica original mantida)
                .filter(tutor -> dispService.temDisponibilidadeProximas72h(tutor))

                // 2. Mapeia o Tutor para um objeto TutorScore, calculando a pontuação
                .map(tutor -> new TutorScore(tutor, calcularScoreRanking(tutor, estudante)))

                // 3. Ordena pelo score (do maior para o menor)
                .sorted(Comparator.comparingDouble(TutorScore::score).reversed())

                // 4. Limita aos N melhores resultados
                .limit(N)

                // 5. Mapeia de volta para o objeto Tutor
                .map(TutorScore::Professor)

                // 6. Coleta o resultado final em uma lista
                .collect(Collectors.toList());
    }

    private double calcularScoreRanking(Professor tutor, Estudante estudante) {

        double mediaAvaliacoes = avaliacaoService.getMediaAvaliacoes(tutor);
        double taxaCancelamento = tutor.getTaxaCancelamento();
        double indiceCompatibilidade = dispService.calcularCompatibilidadeHorarios(tutor, estudante);

        double scoreAvaliacaoNormalizado = (mediaAvaliacoes / MAX_AVALIACAO_SCORE);

        // Assumindo que 'indiceCompatibilidade' e 'taxaCancelamento' já estão na escala
        // 0.0 - 1.0

        double score = (scoreAvaliacaoNormalizado * W_AVALIACAO) +
                (indiceCompatibilidade * W_COMPATIBILIDADE) -
                (taxaCancelamento * W_CANCELAMENTO); // Penalidade por cancelamento

        // Garante que o score não seja negativo (caso a taxa de cancelamento seja muito
        // alta)
        return Math.max(0.0, score);
    }
}
