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
 * @version 1.0
 */

public class ReagendamentoService {

    private final DisponibilidadeRepository dispRepo;
    private final AulaRepository aulaRepo;

    private static final long PERIODO_BUSCA_DIAS = 7;
    private static final long INCREMENTO_SUGESTAO_MINUTOS = 30;

    // Construtor para Injeção de Dependência
    public ReagendamentoService(DisponibilidadeRepository dispRepo, AulaRepository aulaRepo) {
        this.dispRepo = dispRepo;
        this.aulaRepo = aulaRepo;
    }

    public List<HorarioSugerido> getSugestoesReagendamento(Aula aulaParaReagendar) {

        Tutor tutor = aulaParaReagendar.getTutor();
        Estudante estudante = aulaParaReagendar.getEstudante();
        long duracaoMinutos = aulaParaReagendar.getDuracaoEmMinutos();

        LocalDateTime inicioBusca = LocalDateTime.now();
        LocalDateTime fimBusca = inicioBusca.plusDays(PERIODO_BUSCA_DIAS);
        
        // 1. Busca disponibilidade do tutor
        List<SlotDisponivel> disponibilidadeTutor = dispRepo.findDisponibilidade(
            tutor.getId(), inicioBusca, fimBusca
        );

        // 2. Busca aulas já agendadas (horários ocupados)
        List<Aula> aulasOcupadasTutor = aulaRepo.findAulas(tutor.getId(), inicioBusca, fimBusca);
        List<Aula> aulasOcupadasAluno = aulaRepo.findAulas(estudante.getId(), inicioBusca, fimBusca);
        Set<Aula> todosHorariosOcupados = new HashSet<>(aulasOcupadasTutor);
        todosHorariosOcupados.addAll(aulasOcupadasAluno);
        
        todosHorariosOcupados.remove(aulaParaReagendar);

        List<HorarioSugerido> sugestoes = new ArrayList<>();

        for (SlotDisponivel slot : disponibilidadeTutor) {
            
            LocalDateTime inicioSlotProposto = slot.getInicio();
            
            while (!inicioSlotProposto.plusMinutes(duracaoMinutos).isAfter(slot.getFim())) {
                
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
     * A lógica de sobreposição (A.fim > B.inicio) E (A.inicio < B.fim) está correta.
     * @param aulasAgendadas Usamos 'Collection' pois agora recebemos um 'Set'.
     */
    private boolean temConflito(LocalDateTime inicioProposto, LocalDateTime fimProposto, Collection<Aula> aulasAgendadas) {
        for (Aula aula : aulasAgendadas) {
            if (inicioProposto.isBefore(aula.getFim()) && fimProposto.isAfter(aula.getInicio())) {
                // Sim, há sobreposição (conflito)
                return true;
            }
        }
        // Não foi encontrado nenhum conflito
        return false;
    }
}
