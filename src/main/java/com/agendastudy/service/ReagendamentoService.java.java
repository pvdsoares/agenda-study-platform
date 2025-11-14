public class ReagendamentoService {

    private DisponibilidadeRepository dispRepo; 
    private AulaRepository aulaRepo;         

    public List<HorarioSugerido> getSugestoesReagendamento(Aula aulaParaReagendar) {

        Tutor tutor = aulaParaReagendar.getTutor();
        Estudante estudante = aulaParaReagendar.getEstudante();
        long duracaoMinutos = aulaParaReagendar.getDuracaoEmMinutos();

        LocalDateTime inicioBusca = LocalDateTime.now();
        LocalDateTime fimBusca = inicioBusca.plusDays(7);
        
        List<SlotDisponivel> disponibilidadeTutor = dispRepo.findDisponibilidade(
            tutor.getId(), inicioBusca, fimBusca
        );

        List<Aula> aulasOcupadasTutor = aulaRepo.findAulas(tutor.getId(), inicioBusca, fimBusca);
        List<Aula> aulasOcupadasAluno = aulaRepo.findAulas(estudante.getId(), inicioBusca, fimBusca);
        
        List<HorarioSugerido> sugestoes = new ArrayList<>();

        for (SlotDisponivel slot : disponibilidadeTutor) {
            
            LocalDateTime inicioSlot = slot.getInicio();
            
            while (inicioSlot.plusMinutes(duracaoMinutos).isBefore(slot.getFim())) {
                
                LocalDateTime fimSlotProposto = inicioSlot.plusMinutes(duracaoMinutos);
                
                boolean conflitoTutor = temConflito(inicioSlot, fimSlotProposto, aulasOcupadasTutor);
                boolean conflitoAluno = temConflito(inicioSlot, fimSlotProposto, aulasOcupadasAluno);

                if (!conflitoTutor && !conflitoAluno) {
                    sugestoes.add(new HorarioSugerido(inicioSlot, fimSlotProposto));
                }
                
                inicioSlot = inicioSlot.plusMinutes(30); 
            }
        }
        
        return sugestoes;
    }
    
    private boolean temConflito(LocalDateTime inicio, LocalDateTime fim, List<Aula> aulasAgendadas) {
        for (Aula aula : aulasAgendadas) {
            if (inicio.isBefore(aula.getFim()) && fim.isAfter(aula.getInicio())) {
                return true;
            }
        }
        return false;
    }
}