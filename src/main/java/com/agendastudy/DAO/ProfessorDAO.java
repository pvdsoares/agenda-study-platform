package com.agendastudy.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.agendastudy.model.Professor;
import com.agendastudy.model.Usuario;

/**
 * Gerencia as operações de acesso a dados (DAO) para a entidade Professor.
 * 
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

    /**
     * Busca todos os Professores que têm a disciplina específica em sua lista.
     * Necessário para o ProfessorRankingService.
     * * @param nomeDisciplina O nome da disciplina (como String) a ser filtrada.
     * 
     * @return Uma lista de Professores qualificados.
     */
    public List<Professor> findByDisciplina(String nomeDisciplina) {
        if (nomeDisciplina == null || nomeDisciplina.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Acesso direto ao mapa estático de 'usuarios' assumido no UsuarioDAO
        return usuarios.values().stream()
                // Filtra apenas objetos que são instâncias de Professor
                .filter(usuario -> usuario instanceof Professor)
                .map(usuario -> (Professor) usuario)
                // Filtra onde a lista de disciplinas do Professor contém a disciplina buscada
                // (ignorando case)
                .filter(professor -> professor.getDisciplinas().stream()
                        .anyMatch(d -> d.equalsIgnoreCase(nomeDisciplina.trim())))
                .collect(Collectors.toList());
    }
}
