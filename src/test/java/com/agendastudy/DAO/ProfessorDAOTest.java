package com.agendastudy.DAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.agendastudy.model.Professor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de testes de integração para {@link ProfessorDAO}.
 * Testa as funcionalidades de persistência, geração de ID e busca,
 * validando a correta interação com o armazenamento estático (simulação de DB).
 *
 * @author Paulo Vitor Dias Soares
 * @version 1.0
 * @since 2025-11-29
 */
public class ProfessorDAOTest {

    private ProfessorDAO professorDAO;
    private Professor professorExemplo;

    /**
     * Configuração inicial antes de cada teste.
     * CRUCIAL: Limpa o mapa estático de Usuários (simulando o banco de dados)
     * para garantir que cada teste comece em um estado limpo e isolado.
     */
    @BeforeEach
    void setUp() {
        professorDAO = new ProfessorDAO();

        // Limpa o estado estático do UsuarioDAO para isolar os testes
        UsuarioDAO.usuarios.clear();
        UsuarioDAO.proximoId = 1;

        professorExemplo = new Professor(null, "Mariana Teste DAO", "mariana@dao.com", "daoSenha123");
    }

    /**
     * Testa se o método salvarProfessor gera um ID válido e armazena o objeto no
     * sistema.
     */
    @Test
    void deveSalvarProfessorEGerarId() {
        // ACT
        professorDAO.salvarProfessor(professorExemplo);

        // ASSERT: 1. Verifica se o ID foi gerado
        assertNotNull(professorExemplo.getId());
        assertTrue(professorExemplo.getId().startsWith("USER_"));

        // ASSERT: 2. Verifica se o professor foi armazenado e pode ser recuperado
        Professor professorSalvo = professorDAO.buscarPorId(professorExemplo.getId());
        assertNotNull(professorSalvo);
        assertEquals("Mariana Teste DAO", professorSalvo.getNome());
    }

    /**
     * Testa se o método emailExiste() retorna true para um email cadastrado e false
     * para um inexistente.
     */
    @Test
    void deveVerificarSeEmailExisteCorretamente() {
        // ARRANGE: Salva o professor
        professorDAO.salvarProfessor(professorExemplo);

        // ACT & ASSERT: Email existente
        assertTrue(professorDAO.emailExiste("mariana@dao.com"), "O email deve existir.");

        // ACT & ASSERT: Email inexistente
        assertFalse(professorDAO.emailExiste("naoexiste@dao.com"), "O email não deve existir.");
    }

    /**
     * Testa a busca de um Professor pelo Email, verificando o casting correto.
     */
    @Test
    void deveBuscarProfessorPorEmailCorretamente() {
        // ARRANGE: Salva o professor
        professorDAO.salvarProfessor(professorExemplo);

        // ACT
        Professor encontrado = professorDAO.buscarPorEmail("mariana@dao.com");

        // ASSERT: 1. Verifica se encontrou e é do tipo correto
        assertNotNull(encontrado);

        // ASSERT: 2. Verifica a integridade dos dados
        assertEquals("Mariana Teste DAO", encontrado.getNome());
        assertEquals(professorExemplo.getId(), encontrado.getId());
    }

    /**
     * Testa se a busca por e-mail retorna null quando o email não está cadastrado.
     */
    @Test
    void deveRetornarNullAoBuscarEmailInexistente() {
        // ACT
        Professor encontrado = professorDAO.buscarPorEmail("naoexiste@dao.com");

        // ASSERT
        assertNull(encontrado);
    }
}