package com.agendastudy.DAO;

import com.agendastudy.model.Professor;
import com.agendastudy.model.Usuario;
import com.agendastudy.model.Avaliacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Gerencia as operações de acesso a dados (DAO) para a entidade Professor.
 * Inclui lógica de negócio para manipulação de dados do professor.
 *
 * @author PAULO VITOR DIAS SOARES
 * @version 1.1
 * @since 2025-11-10
 */
public class ProfessorDAO extends UsuarioDAO {

    /**
     * Salva um professor no sistema
     * @param professor Professor a ser salvo
     */
    public void salvarProfessor(Professor professor) {
        salvar(professor);
        System.out.println("Professor salvo: " + professor.getNome());
    }

    /**
     * Busca um professor pelo seu ID.
     * @param id O ID do professor.
     * @return O objeto Professor se encontrado, ou null.
     */
    public Professor buscarPorId(String id) {
        Usuario usuario = usuarios.get(id);
        return (usuario instanceof Professor) ? (Professor) usuario : null;
    }

    /**
     * Atualiza a foto de perfil de um professor.
     * @param id O ID do professor.
     * @param foto O array de bytes da foto.
     * @param tipoImagem O tipo da imagem (ex: "image/png").
     */
    public void atualizarFotoPerfil(String id, byte[] foto, String tipoImagem) {
        Professor professor = buscarPorId(id);
        if (professor != null) {
            professor.setFotoPerfil(foto, tipoImagem);
            System.out.println("Foto do professor " + professor.getNome() + " atualizada");
        }
    }

    /**
     * Valida se o professor possui qualificações preenchidas.
     * @param professor O professor a ser validado.
     * @return true se houver qualificações válidas, false caso contrário.
     */
    public boolean validarQualificacoes(Professor professor) {
        if (professor.getQualificacoes() == null || professor.getQualificacoes().isEmpty()) {
            return false;
        }

        for (String qualificacao : professor.getQualificacoes()) {
            if (qualificacao != null && !qualificacao.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se um professor cumpre os requisitos para ser "verificado".
     * Requisitos: ter qualificações, disciplinas e foto.
     * @param professor O professor a ser verificado.
     * @return true se o professor pode ser verificado, false caso contrário.
     */
    public boolean podeSerVerificado(Professor professor) {
        return validarQualificacoes(professor) &&
                professor.getDisciplinas() != null &&
                !professor.getDisciplinas().isEmpty() &&
                professor.temFoto();
    }

    /**
     * Solicita o cancelamento de uma aula.
     * @param idAula O ID da aula a ser cancelada.
     * @param aulaDAO O DAO de Aula para processar o cancelamento.
     */
     // O método solicitarCancelamento antigo foi removido para evitar conflito.

    /**
     * Método de ação para o professor criar uma nova disponibilidade de aula.
     */
    public Aula criarDisponibilidadeDeAula(Professor professor, String titulo, String descricao, LocalDateTime dataHora, ServicoAgendamento servicoAgendamento) {
        return servicoAgendamento.criarDisponibilidade(professor, titulo, descricao, dataHora);
    }
    /**
     * Método de ação para o professor reagendar uma aula criada ou reservada.
     */
    public Aula reagendarAula(String idAula, Professor professor, LocalDateTime novaDataHora, ServicoAgendamento servicoAgendamento) {
        return servicoAgendamento.reagendarAula(idAula, professor, novaDataHora);
    }
    /**
     * Método de ação para o professor cancelar uma aula.
     */
    public void cancelarAula(String idAula, Professor professor, ServicoAgendamento servicoAgendamento) {
        servicoAgendamento.cancelarAgendamento(idAula, professor); 
    }
    /**
     * Calcula e retorna a nota média das avaliações de um professor.
     *
     * @param professor O objeto Professor do qual se deseja a média.
     * @return A média (double) das notas das avaliações.
     * @author Alexandro Costa Santos
     */
    public double getMediaAvaliacoes(Professor professor) {
        List<Avaliacao> avaliacoes = professor.getAvaliacoes();

        if (avaliacoes.isEmpty()) {
            return 0.0;
        }
        double somaTotal = 0.0;
        for (Avaliacao aval : avaliacoes) {
            somaTotal += aval.getNota();
        }
        return somaTotal / (double) avaliacoes.size();
    }

    /**
     * Retorna uma lista de avaliações de um professor, ordenada por data (mais recentes primeiro).
     *
     * @param professor O professor a ser consultado.
     * @return Uma List<Avaliacao> ordenada por data.
     * @author Alexandro Costa Santos
     */
    public List<Avaliacao> getAvaliacoesPorData(Professor professor) {
        List<Avaliacao> copiaOrdenada = new ArrayList<>(professor.getAvaliacoes());
        copiaOrdenada.sort(Comparator.comparing(Avaliacao::getDataAvaliacao).reversed());
        return copiaOrdenada;
    }

    /**
     * Retorna uma lista de avaliações de um professor, ordenada por relevância (maior nota primeiro).
     * Utiliza uma PriorityQueue (MaxHeap) para garantir a ordem.
     *
     * @param professor O professor a ser consultado.
     * @return Uma List<Avaliacao> ordenada por relevância.
     * @author Alexandro Costa Santos
     */
    public List<Avaliacao> getAvaliacoesPorRelevancia(Professor professor) {
        List<Avaliacao> avaliacoes = professor.getAvaliacoes();

        Comparator<Avaliacao> comparadorRelevancia =
                Comparator.comparingInt(Avaliacao::getNota).reversed();

        PriorityQueue<Avaliacao> maxHeap = new PriorityQueue<>(comparadorRelevancia);
        maxHeap.addAll(avaliacoes);

        List<Avaliacao> ordenadasPorRelevancia = new ArrayList<>();
        while (!maxHeap.isEmpty()) {
            ordenadasPorRelevancia.add(maxHeap.poll());
        }
        return ordenadasPorRelevancia;
    }
}
