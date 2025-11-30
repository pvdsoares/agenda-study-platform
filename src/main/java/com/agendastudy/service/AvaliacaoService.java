package com.agendastudy.service;

import com.agendastudy.DAO.AvaliacaoDAO;
import com.agendastudy.model.Professor;
import com.agendastudy.model.Avaliacao;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.Aula;

import java.util.List;

/**
 * Serviço responsável pela lógica de negócios relacionadas às Avaliações.
 * Gerencia operações de avaliação, consulta e cálculo de métricas.
 * 
 * @author Paulo Vitor Dias Soares
 * @version 1.7
 * @since 2025-11-13
 */
public class AvaliacaoService {

    private final AvaliacaoDAO avaliacaoDAO;

    /**
     * Construtor do serviço de avaliações.
     * 
     * @param avaliacaoDAO DAO responsável pela persistência das avaliações
     */
    public AvaliacaoService(AvaliacaoDAO avaliacaoDAO) {
        this.avaliacaoDAO = avaliacaoDAO;
    }

    // -----------------------------------------------------------------
    // MÉTODOS DE AÇÃO
    // -----------------------------------------------------------------

    /**
     * Realiza e registra uma nova avaliação de aula.
     * Valida os dados antes de delegar para o DAO.
     *
     * @param estudante  O estudante que está avaliando (não pode ser nulo)
     * @param aula       A aula a ser avaliada (não pode ser nula)
     * @param nota       A nota de 1 a 5
     * @param comentario O feedback em texto (pode ser vazio)
     * @return true se a avaliação foi realizada, false caso contrário
     * @throws IllegalArgumentException se estudante, aula forem nulos ou nota
     *                                  inválida
     */
    public boolean avaliar(Estudante estudante, Aula aula, int nota, String comentario) {
        if (estudante == null || aula == null) {
            throw new IllegalArgumentException("Estudante e aula não podem ser nulos");
        }
        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("Nota deve estar entre 1 e 5");
        }

        return avaliacaoDAO.avaliar(estudante, aula, nota, comentario);
    }

    /**
     * Verifica se um estudante já avaliou uma determinada aula.
     * Utiliza a lógica de validação do DAO para maior eficiência.
     *
     * @param estudante O estudante a ser verificado (não pode ser nulo)
     * @param aula      A aula a ser verificada (não pode ser nula)
     * @return true se já avaliou, false caso contrário
     */
    public boolean jaAvaliou(Estudante estudante, Aula aula) {
        if (estudante == null || aula == null) {
            return false;
        }
        return !avaliacaoDAO.podeAvaliar(estudante, aula);
    }

    // -----------------------------------------------------------------
    // MÉTODOS DE CONSULTA / MÉTRICAS
    // -----------------------------------------------------------------

    /**
     * Calcula e retorna a nota média das avaliações de um professor.
     * Utiliza a lista de avaliações diretamente do objeto Professor.
     *
     * @param professor O professor cuja média deve ser calculada
     * @return A média das avaliações (0.0 se não houver avaliações)
     */
    public double getMediaAvaliacoes(Professor professor) {
        if (professor == null) {
            return 0.0;
        }

        List<Avaliacao> avaliacoes = professor.getAvaliacoes();

        if (avaliacoes == null || avaliacoes.isEmpty()) {
            return 0.0;
        }

        double somaTotal = avaliacoes.stream()
                .mapToDouble(Avaliacao::getNota)
                .sum();

        return somaTotal / avaliacoes.size();
    }

    /**
     * Retorna todas as avaliações de um professor específico.
     * Método de conveniência para consultas externas.
     *
     * @param professor O professor cujas avaliações devem ser retornadas
     * @return Lista de avaliações do professor (lista vazia se não houver)
     */
    public List<Avaliacao> getAvaliacoesPorProfessor(Professor professor) {
        if (professor == null) {
            return List.of();
        }
        return professor.getAvaliacoes();
    }
}