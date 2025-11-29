package com.agendastudy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;

import com.agendastudy.DAO.ProfessorDAO;
import com.agendastudy.model.Avaliacao;
import com.agendastudy.model.Professor;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.Aula;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Classe de testes de unidade para {@link ProfessorService}.
 * Foca em validar as regras de negócio: unicidade de email, validação de campos
 * e requisitos para verificação de perfil, isolando a camada de persistência.
 *
 * @author Paulo Vitor Dias Soares
 * @version 1.0
 * @since 2025-11-29
 */
@ExtendWith(MockitoExtension.class)
public class ProfessorServiceTest {

    // Simula a dependência ProfessorDAO (Mock)
    @Mock
    private ProfessorDAO professorDAO;

    // A classe sob teste, onde o mock será injetado (@InjectMocks)
    @InjectMocks
    private ProfessorService professorService;

    @Mock
    private Estudante estudanteMock;
    @Mock
    private Aula aulaMock;

    private Professor professorValido;
    private Professor professorComDadosIncompletos;

    /**
     * Configuração inicial antes de cada teste.
     * Cria instâncias de Professor para serem usadas nos cenários de teste.
     */
    @BeforeEach
    void setUp() {
        // Professor Completo e Válido para cenários de sucesso e verificação
        professorValido = new Professor("USER_1", "Maria Teste", "maria@teste.com", "senhaForte123");
        professorValido.setBiografia("Doutora em História.");
        professorValido.adicionarDisciplina("Matemática");
        professorValido.adicionarQualificacao("Mestrado em Álgebra");
        professorValido.setFotoPerfil(new byte[] { 1, 2, 3 }, "jpg");

        // Professor Incompleto/Inválido para cenários de exceção
        professorComDadosIncompletos = new Professor(null, "Pedro", "pedro@teste.com", "123");
    }

    /**
     * Testa o cenário de sucesso do cadastro de professor.
     * Verifica se o DAO é chamado para salvar.
     */
    @Test
    void deveCadastrarProfessorComSucesso() {
        // ARRANGE: Mockito garante que o DAO retorna FALSE para emailExiste()
        when(professorDAO.emailExiste(professorValido.getEmail())).thenReturn(false);

        // ACT: O cadastro deve ocorrer sem lançar exceções
        assertDoesNotThrow(() -> professorService.cadastrarProfessor(professorValido));

        // ASSERT: Verifica se o método salvarProfessor foi chamado exatamente uma vez
        verify(professorDAO, times(1)).salvarProfessor(professorValido);
    }

    /**
     * Testa se {@code cadastrarProfessor} lança exceção quando o e-mail já está em
     * uso.
     * Deve lançar IllegalArgumentException.
     */
    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        // ARRANGE: Mockito garante que o DAO retorna TRUE para emailExiste()
        when(professorDAO.emailExiste(professorValido.getEmail())).thenReturn(true);

        // ACT & ASSERT: Verifica se a exceção correta é lançada
        assertThrows(
                IllegalArgumentException.class,
                () -> professorService.cadastrarProfessor(professorValido));

        // ASSERT: Garante que o método de salvar NUNCA é chamado
        verify(professorDAO, never()).salvarProfessor(any());
    }

    /**
     * Testa se {@code cadastrarProfessor} lança exceção quando a senha é muito
     * curta.
     * Deve lançar IllegalStateException.
     */
    @Test
    void deveLancarExcecaoQuandoSenhaForMuitoCurta() {
        // ARRANGE: Mockito garante que o DAO retorna FALSE (passa na 1ª validação)
        when(professorDAO.emailExiste(professorComDadosIncompletos.getEmail())).thenReturn(false);

        // ACT & ASSERT: Testa a validação de IllegalStateException (senha < 6)
        assertThrows(
                IllegalStateException.class,
                () -> professorService.cadastrarProfessor(professorComDadosIncompletos));

        // ASSERT: Garante que o método de salvar NUNCA é chamado
        verify(professorDAO, never()).salvarProfessor(any());
    }

    /**
     * Testa se a regra de negócio {@code podeSerVerificado} retorna true quando
     * todos os requisitos (Foto, Disciplinas, Qualificações) são atendidos.
     */
    @Test
    void deveRetornarVerificadoQuandoTodosOsRequisitosAtendidos() {
        assertTrue(professorService.podeSerVerificado(professorValido));
    }

    /**
     * Testa se a regra de negócio {@code podeSerVerificado} retorna false quando
     * falta a foto.
     */
    @Test
    void naoDeveRetornarVerificadoQuandoFaltarFoto() {
        // ARRANGE: Remove a foto
        professorValido.setFotoPerfil(null, null);
        assertFalse(professorService.podeSerVerificado(professorValido));
    }

    /**
     * Testa o cálculo correto da média de avaliações.
     */
    @Test
    void deveCalcularMediaCorretamente() {
        // ARRANGE: Mockito garante que quando o construtor de Avaliacao chamar
        // aula.getProfessor(),
        // ele receberá o objeto professorValido, evitando NullPointerException.
        when(aulaMock.getProfessor()).thenReturn(professorValido);

        // ARRANGE: Cria avaliações usando o construtor COMPLETO existente:
        // new Avaliacao(idAvaliacao, estudante, aula, nota, comentario)
        List<Avaliacao> avaliacoes = List.of(
                new Avaliacao("AV_1", estudanteMock, aulaMock, 4, "Bom professor."),
                new Avaliacao("AV_2", estudanteMock, aulaMock, 5, "Excelente."),
                new Avaliacao("AV_3", estudanteMock, aulaMock, 3, "Poderia melhorar."));

        // Simula o preenchimento da lista de avaliações no professor
        professorValido.getAvaliacoes().addAll(avaliacoes);

        // ACT
        double media = professorService.getMediaAvaliacoes(professorValido);

        // ASSERT
        // Média esperada: (4 + 5 + 3) / 3 = 4.0
        assertEquals(4.0, media, 0.001);
    }
}