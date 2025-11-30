package com.agendastudy.DAO;

import com.agendastudy.model.Estudante;
import com.agendastudy.model.Professor;
import com.agendastudy.model.Aula;
import com.agendastudy.model.StatusAula;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de testes de integração para {@link AvaliacaoDAO}.
 * Valida as regras de avaliação de aula, verificando que:
 * Uma aula concluída e ocorrida no passado pode ser avaliada com sucesso,
 * A mesma aula não pode ser avaliada duas vezes pelo mesmo estudante,
 * As avaliações registradas são corretamente recuperadas via DAO,
 * O professor é associado automaticamente a partir da Aula.
 *
 * @author MATHEUS PEREIRA RODRIGUES
 * @version 1.0
 * @since 2025-11-30
 */
public class AvaliacaoDAOTest {

    private AvaliacaoDAO avaliacaoDAO;
    private Professor professor;
    private Estudante estudante;
    private Aula aula;

    /**
     * Configuração inicial antes de cada teste.
     *
     * Recria os objetos base e define uma Aula com data no passado e status
     * {@link StatusAula#CONCLUIDA}, garantindo um estado limpo e isolado
     * para os testes.
     */
    @BeforeEach
    void setUp() {
        avaliacaoDAO = new AvaliacaoDAO();

        professor = new Professor("P1", "Carlos Silva", "cralos123@gmail.com", "silva456");
        estudante = new Estudante("E1", "Matheus Pereira", "mts@gmail.com", "mpmpmp");
        aula = new Aula("A1", "POO", "Introdução, Conceitos iniciais", professor, estudante, LocalDateTime.now().minusDays(2), 120);

        aula.setProfessor(professor);
        aula.setEstudante(estudante);
        aula.setStatus(StatusAula.CONCLUIDA);
    }

    /**
     * Testa se uma avaliação válida é aceita e armazenada corretamente pelo DAO.
     */
    @Test
    void deveAceitarEAArmazenarAvaliacaoValida() {
        // ACT
        boolean sucesso = avaliacaoDAO.avaliar(estudante, aula, 5, "Aula excelente!");

        // ASSERT
        assertTrue(sucesso, "A avaliação deveria ser aceita.");
    }

    /**
     * Testa se o DAO impede a avaliação duplicada da mesma aula pelo mesmo estudante.
     */
    @Test
    void naoDevePermitirAvaliacaoDuplicada() {
        // ARRANGE: registra a primeira avaliação
        avaliacaoDAO.avaliar(estudante, aula, 4, "Muito boa");

        // ACT: tenta avaliar novamente
        boolean duplicada = avaliacaoDAO.avaliar(estudante, aula, 5, "Tentando de novo");

        // ASSERT
        assertFalse(duplicada, "A avaliação duplicada não deveria ser permitida.");
    }

    /**
     * Testa se o método getAvaliacaoPorEstudante() retorna as avaliações do estudante correto.
     */
    @Test
    void deveRetornarAvaliacaoDoEstudanteCorreto() {
        // ARRANGE: registra uma avaliação
        avaliacaoDAO.avaliar(estudante, aula, 5, "Perfeita!");

        // ACT
        var lista = avaliacaoDAO.getAvaliacaoPorEstudante(estudante);

        // ASSERT: deve conter exatamente 1 avaliação registrada
        assertEquals(1, lista.size(), "O estudante deveria ter 1 avaliação salva.");
        assertEquals("Perfeita!", lista.get(0).getComentario(), "O comentário salvo deveria bater.");
    }

}
