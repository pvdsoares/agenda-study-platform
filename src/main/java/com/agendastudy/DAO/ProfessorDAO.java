package com.agendastudy.DAO;

import com.agendastudy.model.Professor;
import com.agendastudy.model.Usuario;

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
     * 
     * @param professor Professor a ser salvo
     */
    public void salvarProfessor(Professor professor) {
        salvar(professor);
        System.out.println("Professor salvo: " + professor.getNome());
    }

    /**
     * Busca um professor pelo seu ID.
     * 
     * @param id O ID do professor.
     * @return O objeto Professor se encontrado, ou null.
     */
    public Professor buscarPorId(String id) {
        Usuario usuario = usuarios.get(id);
        return (usuario instanceof Professor) ? (Professor) usuario : null;
    }

    /**
     * Busca um professor pelo seu Email.
     * Este método é usado pelo Service para verificar a unicidade do e-mail.
     * * @param email O email do professor.
     * 
     * @return O objeto Professor se encontrado, ou null.
     */
    public Professor buscarPorEmail(String email) {
        Usuario usuario = super.buscarPorEmail(email); // Assumindo que este método existe em UsuarioDAO
        return (usuario instanceof Professor) ? (Professor) usuario : null;
    }
}
