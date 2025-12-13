package com.agendastudy.service;

import com.agendastudy.DAO.AulaDAO;
import com.agendastudy.DAO.DisponibilidadeDAO;
import com.agendastudy.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Serviço responsável pela lógica de negócio de agendamento.
 * Centraliza as regras de validação (conflito de horário, permissões)
 * para criação, reserva, reagendamento e cancelamento de aulas.
 *
 * @author VINICIUS ALVES RIBEIRO SILVA
 * @version 1.2
 * @since 2025-11-29
 */
public class ServicoAgendamento {
    private final AulaDAO aulaDAO;
    // private final DisponibilidadeDAO dispDAO; // REMOVED unused
    // private static final int DURACAO_PADRAO_MINUTOS = 60; // REMOVED unused

    /**
     * Construtor do serviço. Recebe o AulaDAO por injeção de dependência.
     * 
     * @param aulaDAO O DAO de Aula, necessário para persistir as alterações.
     * @param dispDAO O DAO de Disponibilidade.
     */
    public ServicoAgendamento(AulaDAO aulaDAO, DisponibilidadeDAO dispDAO) {
        this.aulaDAO = aulaDAO;
        // this.dispDAO = dispDAO;
    }

    // --- FUNÇÕES AUXILIARES DE VALIDAÇÃO ---

    /**
     * Valida se um professor está disponível em um determinado horário.
     * Verifica conflitos com outras aulas do professor.
     * 
     * @param professor      O professor a ser verificado.
     * @param dataHora       O horário de início desejado.
     * @param duracaoMinutos A duração da aula proposta.
     * @param idAulaExcluir  O ID da própria aula (caso seja um reagendamento)
     *                       para ser ignorado na checagem de conflito.
     * @return true se o horário estiver livre, false se houver conflito.
     */
    private boolean isHorarioDisponivel(Professor professor, LocalDateTime dataHora, int duracaoMinutos,
            String idAulaExcluir) {
        List<Aula> aulasAgendadas = aulaDAO.buscarAulasDoProfessor(professor);
        LocalDateTime dataFim = dataHora.plusMinutes(duracaoMinutos); // Assumindo 1 hora de duração padrão

        for (Aula aula : aulasAgendadas) {
            // Ignora a própria aula na verificação de conflito (para reagendamentos)
            if (aula.getIdAula() != null && aula.getIdAula().equals(idAulaExcluir)) {
                continue;
            }

            LocalDateTime inicioExistente = aula.getDataHora();
            LocalDateTime fimExistente = aula.getFimAula();

            // Lógica de verificação de sobreposição de horários
            if (dataHora.isBefore(fimExistente) && dataFim.isAfter(inicioExistente)) {
                return false; // Conflito
            }
        }
        return true; // Horário disponível
    }

    // --- FUNÇÕES DE CRIAÇÃO (Professor) ---

    /**
     * Professor cria a disponibilidade de aula (aula vaga).
     * primeiro salva um SlotDisponivel no DispDAO, e depois a Aula (ou você pode
     * optar por criar a Aula e, se agendada, removê-lo. Por simplicidade, faremos a
     * criação direta da Aula).
     * 
     * @param professor      O professor que está criando a vaga.
     * @param titulo         O título da aula.
     * @param descricao      A descrição da aula.
     * @param dataHora       O horário da vaga.
     * @param duracaoMinutos A duração da aula proposta.
     * @return A Aula criada e salva.
     * @throws IllegalArgumentException se o horário for no passado.
     * @throws IllegalStateException    se houver conflito de horário.
     */
    public Aula criarDisponibilidade(Professor professor, String titulo, String descricao, LocalDateTime dataHora,
            int duracaoMinutos) {
        if (dataHora.isBefore(LocalDateTime.now().plusMinutes(5))) {
            throw new IllegalArgumentException("A disponibilidade deve ser criada para o futuro.");
        }
        if (duracaoMinutos <= 0) {
            throw new IllegalArgumentException("A duração da aula deve ser positiva.");
        }
        if (!isHorarioDisponivel(professor, dataHora, duracaoMinutos, null)) {
            throw new IllegalStateException("O professor já possui uma aula agendada neste horário.");
        }

        // Cria a aula sem estudante (vaga aberta)
        Aula novaAula = new Aula(
                null, // ID será gerado pelo DAO
                titulo,
                descricao,
                professor,
                null, // Estudante é nulo
                dataHora,
                duracaoMinutos);

        return aulaDAO.salvarOuAtualizar(novaAula);
    }

    // --- FUNÇÕES DE RESERVA/AGENDAMENTO (Estudante) ---

    /**
     * Estudante agenda (reserva) uma disponibilidade de aula.
     * 
     * @param idAula    O ID da aula (disponibilidade) a ser reservada.
     * @param estudante O estudante que está reservando.
     * @return A Aula atualizada com o estudante.
     * @throws NoSuchElementException se a aula não for encontrada.
     * @throws IllegalStateException  se a aula já estiver reservada.
     */
    public Aula agendarAula(String idAula, Estudante estudante) {
        Aula aula = aulaDAO.buscarPorId(idAula);

        if (aula == null || aula.getStatus() == StatusAula.CANCELADA_ALUNO) {
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
     * Reagenda uma aula, permitindo a ação tanto pelo Professor quanto pelo
     * Estudante.
     * Valida permissão do usuário e conflito de horário.
     * 
     * @param idAula             O ID da aula a ser reagendada.
     * @param usuarioReagendando O Usuário (Professor ou Estudante) que está fazendo
     *                           a ação.
     * 
     * @param novaDataHora       O novo horário desejado.
     * @return A Aula atualizada.
     * @throws NoSuchElementException   se a aula não for encontrada.
     * @throws IllegalArgumentException se o novo horário for no passado.
     * @throws SecurityException        se o usuário não tiver permissão.
     * @throws IllegalStateException    se houver conflito de horário com o
     *                                  professor.
     */
    public Aula reagendarAula(String idAula, Usuario usuarioReagendando, LocalDateTime novaDataHora,
            int novaDuracaoMinutos) {
        Aula aula = aulaDAO.buscarPorId(idAula);

        if (aula == null) {
            throw new NoSuchElementException("Aula de ID: " + idAula + " não encontrada!");
        }
        if (novaDataHora.isBefore(LocalDateTime.now().plusMinutes(5))) {
            throw new IllegalArgumentException("Não é possível reagendar para menos de 5 minutos no futuro.");
        }

        // Se a duração não foi passada (é -1), usa a duração atual da aula
        int duracaoFinal = (novaDuracaoMinutos > 0) ? novaDuracaoMinutos : aula.getDuracaoMinutos();
        if (duracaoFinal <= 0) {
            throw new IllegalArgumentException("A duração final da aula deve ser positiva.");
        }

        // 1. Verificar permissão
        boolean isProfessorDono = usuarioReagendando instanceof Professor
                && usuarioReagendando.getId().equals(aula.getProfessor().getId());
        boolean isEstudanteReserva = aula.getEstudante() != null && usuarioReagendando instanceof Estudante
                && usuarioReagendando.getId().equals(aula.getEstudante().getId());

        if (!isProfessorDono && !isEstudanteReserva) {
            throw new SecurityException("Usuário não tem permissão para reagendar esta aula.");
        }

        // 2. Validação de conflito do PROFESSOR (ignorando a própria aula)
        if (!isHorarioDisponivel(aula.getProfessor(), novaDataHora, duracaoFinal, idAula)) {
            throw new IllegalStateException("O novo horário conflita com outra aula já agendada do professor.");
        }

        // 3. Persiste o reagendamento
        aula.setDataHora(novaDataHora);

        if (novaDuracaoMinutos > 0) {
            aula.setDuracaoMinutos(novaDuracaoMinutos);
        }
        System.out.println(
                "Aula " + idAula + " reagendada para: " + novaDataHora + " por " + usuarioReagendando.getNome());
        return aulaDAO.salvarOuAtualizar(aula);
    }

    // --- FUNÇÕES DE CANCELAMENTO ---

    /**
     * Permite o cancelamento da aula, verificando se o usuário (Professor ou
     * Estudante)
     * tem permissão para tal.
     * 
     * @param idAula            O ID da aula a ser cancelada.
     * @param usuarioCancelando O usuário que está realizando a ação.
     * @throws NoSuchElementException se a aula não for encontrada.
     * @throws SecurityException      se o usuário não tiver permissão.
     */
    public void cancelarAgendamento(String idAula, Usuario usuarioCancelando) {
        Aula aula = aulaDAO.buscarPorId(idAula);

        if (aula == null) {
            throw new NoSuchElementException("Aula de ID: " + idAula + " não encontrada!");
        }

        // 1. Verificar permissão para cancelar
        boolean isProfessorDono = usuarioCancelando instanceof Professor
                && usuarioCancelando.getId().equals(aula.getProfessor().getId());
        boolean isEstudanteReserva = aula.getEstudante() != null && usuarioCancelando instanceof Estudante
                && usuarioCancelando.getId().equals(aula.getEstudante().getId());

        if (!isProfessorDono && !isEstudanteReserva) {
            throw new SecurityException("Usuário não tem permissão para cancelar esta aula.");
        }

        if (isProfessorDono) {
            aula.setStatus(StatusAula.CANCELADA_PROFESSOR);
        } else { // Deve ser o estudante, por causa da verificação de permissão
            aula.setStatus(StatusAula.CANCELADA_ALUNO);
        }

        // 3. Salva a alteração de status no DAO (Persistência)
        aulaDAO.salvarOuAtualizar(aula);
        System.out.println("Cancelamento processado pelo Serviço. Status: " + aula.getStatus());

    }

    // --- FUNÇÕES DE CONSULTA ---

    /**
     * Retorna a agenda de um professor (aulas ativas) para exibição.
     * 
     * @param professor O professor cuja agenda será consultada.
     * @return A lista de Aulas ativas.
     */
    public List<Aula> getAgendaDoProfessor(Professor professor) {
        return aulaDAO.buscarAulasDoProfessor(professor);
    }
}