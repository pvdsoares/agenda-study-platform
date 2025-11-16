public class TutorRankingService {

    private TutorRepository tutorRepo;
    private AvaliacaoRepository avaliacaoRepo;
    private DisponibilidadeService dispService;

    private class TutorScore {
        public Tutor tutor;
        public double score;

        public TutorScore(Tutor t, double s) {
            this.tutor = t;
            this.score = s;
        }
    }

    public List<Tutor> getTopNTutores(Estudante estudante, Disciplina disciplina, int N) {
        
        List<Tutor> todosOsTutores = tutorRepo.findByDisciplina(disciplina);
        
        List<TutorScore> tutoresComPontuacao = new ArrayList<>();

        for (Tutor tutor : todosOsTutores) {
            
            if (!dispService.temDisponibilidadeProximas72h(tutor)) {
                continue; 
            }

            double score = calcularScoreRanking(tutor, estudante);
            
            tutoresComPontuacao.add(new TutorScore(tutor, score));
        }

        tutoresComPontuacao.sort((ts1, ts2) -> Double.compare(ts2.score, ts1.score));

        return tutoresComPontuacao.stream()
                .limit(N)
                .map(ts -> ts.tutor)
                .collect(Collectors.toList());
    }

    private double calcularScoreRanking(Tutor tutor, Estudante estudante) {
        
        double mediaAvaliacoes = avaliacaoRepo.getMediaAvaliacoes(tutor.getId()); 
        
        double taxaCancelamento = tutor.getTaxaCancelamento(); 
        
        double indiceCompatibilidade = dispService.calcularCompatibilidadeHorarios(tutor, estudante); 

        double W_AVALIACAO = 0.5;
        double W_COMPATIBILIDADE = 0.4;
        double W_CANCELAMENTO = 0.1;

        double score = (mediaAvaliacoes * W_AVALIACAO) +
                       (indiceCompatibilidade * W_COMPATIBILIDADE) -
                       (taxaCancelamento * W_CANCELAMENTO);
        
        return score;
    }
}