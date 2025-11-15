package com.agendastudy.controller;

import com.agendastudy.DAO.UsuarioDAO;
import com.agendastudy.model.Usuario;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class GerenciamentoUsuariosController implements Serializable {

    @Inject
    private UsuarioDAO usuarioDAO;

    // --- Variáveis de Paginação e Listagem ---
    private List<Usuario> usuarios;
    private int paginaAtual = 1;
    private int tamanhoPagina = 10; // Exibir 10 usuários por página
    private int totalUsuarios;
    private int totalPaginas;

    // --- Variáveis de Filtro Avançado ---
    private String termoBusca; // Nome ou E-mail
    private String tipoUsuario; // "Estudante" ou "Professor"
    private Boolean statusUsuario; // true (Ativo), false (Inativo), null (Todos)

    // --- Inicialização ---
    @PostConstruct
    public void init() {
        carregarUsuarios();
    }

    // --- Métodos de Controle ---

    public void carregarUsuarios() {
        totalUsuarios = usuarioDAO.contarUsuariosFiltrados(termoBusca, tipoUsuario, statusUsuario);
        totalPaginas = (int) Math.ceil((double) totalUsuarios / tamanhoPagina);

        // Garante que a página atual não exceda o limite
        if (paginaAtual > totalPaginas && totalPaginas > 0) {
            paginaAtual = totalPaginas;
        } else if (paginaAtual < 1) {
            paginaAtual = 1;
        }

        usuarios = usuarioDAO.buscarPaginada(termoBusca, tipoUsuario, statusUsuario, paginaAtual, tamanhoPagina);
    }

    public void aplicarFiltros() {
        // Reinicia para a primeira página ao aplicar novos filtros
        paginaAtual = 1; 
        carregarUsuarios();
    }
    
    public void limparFiltros() {
        termoBusca = null;
        tipoUsuario = null;
        statusUsuario = null;
        paginaAtual = 1;
        carregarUsuarios();
    }

    /**
     * Alterna o status do usuário e recarrega a lista.
     * @param usuario O objeto usuário a ser alterado.
     */
    public void alterarStatusUsuario(Usuario usuario) {
        boolean novoStatus = !usuario.isAtivo();
        if (usuarioDAO.alterarStatus(usuario.getId(), novoStatus)) {
            // Atualiza a propriedade do modelo na view imediatamente
            usuario.setAtivo(novoStatus);
            // Mensagem de sucesso aqui (via FacesContext.addMessage)
            System.out.println("Status de " + usuario.getNome() + " alterado para " + (novoStatus ? "ATIVO" : "INATIVO"));
        }
        // Não é necessário carregarUsuarios() novamente, apenas o status na linha muda.
    }

    public void proximaPagina() {
        if (paginaAtual < totalPaginas) {
            paginaAtual++;
            carregarUsuarios();
        }
    }

    public void paginaAnterior() {
        if (paginaAtual > 1) {
            paginaAtual--;
            carregarUsuarios();
        }
    }

    // --- Getters auxiliares para a View ---

    /**
     * Converte o nome da classe para um tipo de usuário legível.
     */
    public String getTipoUsuarioLegivel(Usuario usuario) {
        String tipo = usuario.getClass().getSimpleName();
        return switch (tipo) {
            case "Estudante" -> "Aluno";
            case "Professor" -> "Tutor";
            case "Administrador" -> "Admin";
            default -> tipo;
        };
    }

    // --- Getters e Setters (Necessários para JSF/View) ---

    public List<Usuario> getUsuarios() { return usuarios; }
    public int getPaginaAtual() { return paginaAtual; }
    public int getTotalPaginas() { return totalPaginas; }
    public String getTermoBusca() { return termoBusca; }
    public void setTermoBusca(String termoBusca) { this.termoBusca = termoBusca; }
    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public Boolean getStatusUsuario() { return statusUsuario; }
    public void setStatusUsuario(Boolean statusUsuario) { this.statusUsuario = statusUsuario; }
}
