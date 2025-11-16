package com.agendastudy.DAO;

import com.agendastudy.model.Administrador;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.Professor;
import com.agendastudy.model.Usuario;
import java.util.Map;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia as operações de acesso a dados (DAO) para a entidade Administrador.
 * Herda de UsuarioDAO para ter acesso ao mapa global de usuários.
 *
 * @author Alexandro Costa Santos
 * @version 1.0
 * @since 2025-11-13
 */
public class AdministradorDAO extends UsuarioDAO {

    /**
     * Salva um novo administrador no sistema, reutilizando o método 'salvar' da classe pai.
     *
     * @param admin O objeto Administrador a ser salvo.
     */
    public void salvar(Administrador admin) {
        super.salvar(admin);
        System.out.println("Administrador salvo: " + admin.getNome());
    }

    /**
     * Autentica um usuário, garantindo que ele seja um Administrador.
     * Isso cumpre o requisito de "login único".
     *
     * @param email O email do administrador para autenticação.
     * @param senha A senha do administrador para autenticação.
     * @return O objeto Administrador se o login for válido e o usuário for
     * efetivamente um admin; retorna null caso contrário.
     */
    public Administrador autenticarAdmin(String email, String senha) {
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getEmail().equalsIgnoreCase(email) && usuario.getSenha().equals(senha)) {
                // Verifica se o usuário é DA CLASSE Administrador
                if (usuario instanceof Administrador) {
                    return (Administrador) usuario;
                } else {
                    // Email e senha corretos, mas NÃO é um admin
                    return null;
                }
            }
        }
        // Email ou senha incorretos
        return null;
    }

    /**
     * Retorna uma lista de todos os Professores cadastrados no sistema.
     * Cumpre o requisito de "acesso a dados de professores".
     *
     * @return Uma List<Professor> contendo todos os professores cadastrados.
     */
    public List<Professor> listarTodosProfessores() {
        List<Professor> professores = new ArrayList<>();
        for (Usuario usuario : usuarios.values()) {
            if (usuario instanceof Professor) {
                professores.add((Professor) usuario);
            }
        }
        return professores;
    }

    /**
     * Retorna uma lista de todos os Estudantes cadastrados no sistema.
     * Cumpre o requisito de "acesso a dados de... estudantes".
     *
     * @return Uma List<Estudante> contendo todos os estudantes cadastrados.
     */
    public List<Estudante> listarTodosEstudantes() {
        List<Estudante> estudantes = new ArrayList<>();
        for (Usuario usuario : usuarios.values()) {
            if (usuario instanceof Estudante) {
                estudantes.add((Estudante) usuario);
            }
        }
        return estudantes;
    }
    /**
     * Gera um relatório com o número total de alunos (Estudante) e tutores (Professor).
     * (Funcionalidade: Relatório - NOVO MÉTODO)
     * @return Um Map com as chaves "Estudantes" e "Professores" e seus respectivos totais.
     */
    public Map<String, Integer> gerarRelatorioContagem() {
        int totalEstudantes = 0;
        int totalProfessores = 0;
        
        // Percorre o mapa 'usuarios' herdado de UsuarioDAO
        for (Usuario usuario : usuarios.values()) {
            if (usuario instanceof Estudante) {
                totalEstudantes++;
            } else if (usuario instanceof Professor) {
                totalProfessores++;
            }
        }
        
        Map<String, Integer> relatorio = new HashMap<>();
        relatorio.put("Total Estudantes (Alunos)", totalEstudantes);
        relatorio.put("Total Professores (Tutores)", totalProfessores);
        return relatorio;
    }
}
