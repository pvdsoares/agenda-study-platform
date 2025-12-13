package com.agendastudy.service;

import com.agendastudy.DAO.EstudanteDAO;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.Aula;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço responsável pelas regras de negócio específicas da entidade
 * Estudante.
 * Orquestra as ações entre os Controllers, DAOs e outros Services.
 */
public class EstudanteService {

    private final EstudanteDAO estudanteDAO;
    private final ServicoAgendamento servicoAgendamento;
    private final AvaliacaoService avaliacaoService; // Melhor chamar o Service de Avaliação

    /**
     * Construtor para Injeção de Dependência.
     * Recebe os DAOs e Services necessários para operar.
     */
    public EstudanteService(EstudanteDAO estudanteDAO,
            ServicoAgendamento servicoAgendamento,
            AvaliacaoService avaliacaoService) {
        this.estudanteDAO = estudanteDAO;
        this.servicoAgendamento = servicoAgendamento;
        this.avaliacaoService = avaliacaoService;
    }

    // --- MÉTODOS DE AÇÃO DELEGADOS PARA OUTROS SERVICES ---

    /**
     * Cadastra um novo Estudante.
     * Delega a persistência para o EstudanteDAO (herdado de UsuarioDAO).
     */
    public Estudante cadastrar(Estudante estudante) {
        // Lógica de negócio de cadastro, se houver (ex: criptografia de senha)

        // Chamada de persistência
        return (Estudante) estudanteDAO.salvar(estudante);
    }

    /**
     * Método de ação para o estudante agendar uma aula (confirmar a reserva).
     */
    public Aula agendarAula(String idAula, Estudante estudante) {
        return servicoAgendamento.agendarAula(idAula, estudante);
    }

    /**
     * Método de ação para o estudante reagendar uma aula.
     */
    public Aula reagendarAula(String idAula, Estudante estudante, LocalDateTime novaDataHora, int novaDuracaoMinutos) {
        // O método reagendarAula no ServicoAgendamento precisa da duração.
        return servicoAgendamento.reagendarAula(idAula, estudante, novaDataHora, novaDuracaoMinutos);
    }

    /**
     * Método de ação para o estudante cancelar uma aula.
     */
    public void cancelarAula(String idAula, Estudante estudante) {
        servicoAgendamento.cancelarAgendamento(idAula, estudante);
    }

    /**
     * Permite que o estudante avalie uma aula após sua conclusão.
     */
    public boolean avaliarAula(Estudante estudante, Aula aula, int nota, String comentario) {
        // A AvaliacaoService deve conter a lógica para verificar se a aula foi
        // concluída, etc.
        return avaliacaoService.avaliar(estudante, aula, nota, comentario);
    }

    // --- MÉTODOS DE CONSULTA (Podem chamar o DAO diretamente ou serem regras de
    // negócio) ---

    /**
     * Verifica se o email já está cadastrado (Regra de Negócio/Validação).
     */
    public boolean emailExiste(String email) {
        // Chamamos a função de busca do DAO (herdada de UsuarioDAO)
        return estudanteDAO.buscarPorEmail(email) != null;
    }

    /**
     * Retorna todas as aulas agendadas por um determinado estudante.
     */
    public List<Aula> getAulasAgendadas(Estudante estudante) {
        // Delega para o DAO (que já está filtrando a lista)
        return estudanteDAO.getAulasAgendadas(estudante);
    }

}