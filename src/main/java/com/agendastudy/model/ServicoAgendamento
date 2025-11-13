package com.agendastudy.service;

import com.agendastudy.DAO.AulaDAO;
import com.agendastudy.model.Aula;
import com.agendastudy.model.Professor;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.Usuario;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Serviço responsável pela lógica de negócio de agendamento, reagendamento, 
 * validação de horário e confirmação de aulas.
 */
public class ServicoAgendamento {
    private final AulaDAO aulaDAO;

    public ServicoAgendamento(AulaDAO aulaDAO) {
        this.aulaDAO = aulaDAO;
    }

    // --- FUNÇÕES AUXILIARES DE VALIDAÇÃO ---

    /**
     * Valida se um professor está disponível em um determinado horário.
     */
    private boolean isHorarioDisponivel(Professor professor, LocalDateTime dataHora, String idAulaExcluir) {
        List<Aula> aulasAgendadas = aulaDAO.buscarAulasDoProfessor(professor);
        LocalDateTime dataFim = dataHora.plusHours(1); // Assumindo 1 hora de duração padrão

        for (Aula aula : aulasAgendadas) {
            // Ignora a própria aula na verificação de conflito
            if (aula.getIdAula().equals(idAulaExcluir)) {
                continue; 
            }
            
            LocalDateTime inicioExistente = aula.getDataHora();
            LocalDateTime fimExistente = inicioExistente.plusHours(1);

            if (dataHora.isBefore(fimExistente) && dataFim.isAfter(inicioExistente)) {
                return false; // Conflito
            }
        }
        return true;
    }

    // --- FUNÇÕES DE CRIAÇÃO (Professor) ---

    /**
     * Professor cria a disponibilidade de aula (aula aberta).
     */
    public Aula criarDisponibilidade(Professor professor, String titulo, String descricao, LocalDateTime dataHora) {
        if (dataHora.isBefore(LocalDateTime.now().plusMinutes(5))) {
            throw new IllegalArgumentException("A disponibilidade deve ser criada para o futuro.");
        }

        if (!isHorarioDisponivel(professor, dataHora, null)) {
            throw new IllegalStateException("O professor já possui uma aula agendada neste horário.");
        }

        // Cria a aula sem estudante (vaga aberta)
        Aula novaAula = new Aula(
            null, // ID será gerado pelo DAO
            titulo,
            descricao,
            professor,
            null, // Estudante é nulo
            dataHora
        );

        return aulaDAO.salvarOuAtualizar(novaAula);
    }

    // --- FUNÇÕES DE RESERVA/AGENDAMENTO (Estudante) ---

    /**
     * Estudante agenda a aula, confirmando a reserva.
     */
    public Aula agendarAula(String idAula, Estudante estudante) {
        Aula aula = aulaDAO.buscarPorId(idAula);

        if (aula == null || aula.isCancelada()) {
            throw new NoSuchElementException("Disponibilidade de aula não encontrada ou cancelada.");
        }
        if (aula.getEstudante() != null) {
            throw new IllegalStateException("Esta aula já está reservada por outro estudante.");
        }

        aula.setEstudante(estudante); // Registra a confirmação do estudante
        System.out.println("Aula " + idAula + " reservada pelo estudante " + estudante.getNome());
        return aulaDAO.salvarOuAtualizar(aula);
    }

    // --- FUNÇÕES DE REAGENDAMENTO (Professor E Estudante) ---

    /**
     * Reagenda uma aula, permitindo a ação tanto pelo Professor quanto pelo Estudante.
     */
    public Aula reagendarAula(String idAula, Usuario usuarioReagendando, LocalDateTime novaDataHora) {
        Aula aula = aulaDAO.buscarPorId(idAula);
        
        if (aula == null) {
            throw new NoSuchElementException("Aula de ID: " + idAula + " não encontrada!");
        }
        if (novaDataHora.isBefore(LocalDateTime.now().plusMinutes(5))) {
            throw new IllegalArgumentException("Não é possível reagendar para menos de 5 minutos no futuro.");
        }
        
        // 1. Verificar permissão
        boolean isProfessorDono = usuarioReagendando instanceof Professor && usuarioReagendando.getId().equals(aula.getProfessor().getId());
        boolean isEstudanteReserva = aula.getEstudante() != null && usuarioReagendando instanceof Estudante && usuarioReagendando.getId().equals(aula.getEstudante().getId());

        if (!isProfessorDono && !isEstudanteReserva) {
            throw new SecurityException("Usuário não tem permissão para reagendar esta aula.");
        }
        
        // 2. Validação de conflito do PROFESSOR
        if (!isHorarioDisponivel(aula.getProfessor(), novaDataHora, idAula)) {
            throw new IllegalStateException("O novo horário conflita com outra aula já agendada do professor.");
        }
        
        // 3. Persiste o reagendamento
        aula.setDataHora(novaDataHora);
        System.out.println("Aula " + idAula + " reagendada para: " + novaDataHora + " por " + usuarioReagendando.getNome());
        return aulaDAO.salvarOuAtualizar(aula);
    }
    
    // --- FUNÇÕES DE CANCELAMENTO ---

    /**
     * Permite o cancelamento da aula, verificando se o usuário tem permissão.
     */
    public void cancelarAgendamento(String idAula, Usuario usuarioCancelando) {
        Aula aula = aulaDAO.buscarPorId(idAula);

        if (aula == null) {
            throw new NoSuchElementException("Aula de ID: " + idAula + " não encontrada!");
        }
        
        // 1. Verificar permissão para cancelar
        boolean isProfessorDono = usuarioCancelando instanceof Professor && usuarioCancelando.getId().equals(aula.getProfessor().getId());
        boolean isEstudanteReserva = aula.getEstudante() != null && usuarioCancelando instanceof Estudante && usuarioCancelando.getId().equals(aula.getEstudante().getId());

        if (!isProfessorDono && !isEstudanteReserva) {
            throw new SecurityException("Usuário não tem permissão para cancelar esta aula.");
        }
        
        // 2. Chama o método de persistência (AulaDAO)
        aulaDAO.cancelarAula(idAula);
        System.out.println("Cancelamento processado pelo Serviço.");
    }

    // --- FUNÇÕES DE CONSULTA ---
    
    /**
     * Retorna a agenda de um professor para exibição no calendário.
     */
    public List<Aula> getAgendaDoProfessor(Professor professor) {
        return aulaDAO.buscarAulasDoProfessor(professor);
    }
}
