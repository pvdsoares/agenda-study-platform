package com.agendastudy.service;

import java.util.List;

import com.agendastudy.DAO.ProfessorDAO;
import com.agendastudy.model.Professor;

/**
 * Serviço responsável pela lógica de negócio e regras de cadastro e perfil
 * do Professor. Orquestra a persistência através do ProfessorDAO.
 *
 * @author Paulo Vitor Dias Soares
 * @version 1.3
 * @since 2025-11-30
 */
public class ProfessorService {
    private final ProfessorDAO professorDAO;

    // Assumimos que você tem um DAO para Avaliações (se necessário para cálculos)
    // private final AvaliacaoDAO avaliacaoDAO;

    /**
     * Construtor que recebe o DAO por Injeção de Dependência.
     *
     * @param professorDAO O objeto DAO responsável pela persistência do Professor
     */
    public ProfessorService(ProfessorDAO professorDAO) {
        this.professorDAO = professorDAO;
    }

    // --- LÓGICA DE CADASTRO ---

    /**
     * Valida e salva um novo professor no sistema.
     *
     * @param professor O objeto Professor a ser cadastrado
     * @throws IllegalArgumentException Se o e-mail já estiver em uso
     * @throws IllegalStateException    Se faltarem dados essenciais para o cadastro
     *                                  inicial
     */
    public void cadastrarProfessor(Professor professor) {
        // Validação de unicidade de e-mail (regra de negócio)
        if (professorDAO.buscarPorEmail(professor.getEmail()) != null) {
            throw new IllegalArgumentException(
                    "O e-mail '" + professor.getEmail() + "' já está cadastrado no sistema.");
        }

        // Validação de campos essenciais (Ex: senha mínima, formato de email etc.)
        if (professor.getNome() == null || professor.getNome().trim().isEmpty() ||
                professor.getSenha() == null || professor.getSenha().length() < 6) {
            throw new IllegalStateException("Nome e senha devem ser preenchidos corretamente.");
        }

        // Delega a persistência para o DAO
        professorDAO.salvarProfessor(professor);
    }

    // --- LÓGICA DE ATUALIZAÇÃO E EDIÇÃO ---

    /**
     * Busca um professor por ID.
     *
     * @param id O ID do professor
     * @return O Professor encontrado ou null
     */
    public Professor buscarPorId(String id) {
        return professorDAO.buscarPorId(id);
    }

    /**
     * Atualiza os dados de um professor.
     *
     * @param professor O objeto Professor com os dados atualizados
     */
    public void atualizarProfessor(Professor professor) {
        // Lógica de pré-atualização, como garantir que o ID exista.
        if (professorDAO.buscarPorId(professor.getId()) == null) {
            throw new IllegalArgumentException(
                    "Impossível atualizar: Professor de ID " + professor.getId() + " não encontrado.");
        }

        // Delega a persistência ao DAO (o DAO deve ter um método que lida com update)
        professorDAO.salvarProfessor(professor); // Reutilizando 'salvar' como 'salvar ou atualizar'
    }

    // --- LÓGICA DE VERIFICAÇÃO DE PERFIL ---

    /**
     * Valida se o professor possui qualificações válidas preenchidas.
     * (Esta lógica foi movida do ProfessorDAO para cá).
     *
     * @param professor O professor a ser validado
     * @return true se houver ao menos uma qualificação válida, false caso contrário
     */
    public boolean validarQualificacoes(Professor professor) {
        List<String> qualificacoes = professor.getQualificacoes();
        if (qualificacoes == null || qualificacoes.isEmpty()) {
            return false;
        }

        for (String qualificacao : qualificacoes) {
            if (qualificacao != null && !qualificacao.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se um professor cumpre os requisitos para ser "verificado" (Regra de
     * Negócio).
     * Requisitos: ter qualificações, disciplinas e foto.
     * (Esta lógica foi movida do ProfessorDAO para cá).
     *
     * @param professor O professor a ser verificado
     * @return true se o professor pode ser verificado, false caso contrário
     */
    public boolean podeSerVerificado(Professor professor) {
        return validarQualificacoes(professor) &&
                professor.getDisciplinas() != null &&
                !professor.getDisciplinas().isEmpty() &&
                professor.temFoto();
    }

    /**
     * Define o status de verificação do perfil, se os requisitos forem atendidos.
     *
     * @param professor O professor a ter o perfil verificado
     * @throws IllegalStateException se os requisitos mínimos não forem atendidos
     */
    public void verificarPerfil(Professor professor) {
        if (podeSerVerificado(professor)) {
            professor.setPerfilVerificado(true);
            professorDAO.salvarProfessor(professor);
        } else {
            throw new IllegalStateException(
                    "O perfil do professor não atende aos requisitos de verificação (Qualificações, Disciplinas, Foto).");
        }
    }

    // --- LÓGICA DE CONSULTA E AGRAGAÇÃO (Movida do ProfessorDAO) ---

    /**
     * Calcula e retorna a nota média das avaliações de um professor.
     *
     * @param professor O objeto Professor
     * @return A média (double) das notas das avaliações
     */
    public double getMediaAvaliacoes(Professor professor) {
        List<com.agendastudy.model.Avaliacao> avaliacoes = professor.getAvaliacoes();

        if (avaliacoes.isEmpty()) {
            return 0.0;
        }
        double somaTotal = 0.0;
        for (com.agendastudy.model.Avaliacao aval : avaliacoes) {
            somaTotal += aval.getNota();
        }
        return somaTotal / (double) avaliacoes.size();
    }
}