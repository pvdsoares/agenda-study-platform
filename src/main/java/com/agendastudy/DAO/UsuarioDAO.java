package com.agendastudy.DAO;

import com.agendastudy.model.Usuario;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap; // ADICIONADO (Para SCRUM-114)
import java.util.List; // Adicionado
import java.util.ArrayList; // Adicionado
import java.util.concurrent.CopyOnWriteArrayList; // ADICIONADO (Para SCRUM-114)
import java.util.stream.Collectors;
import java.util.Collections;
import java.time.LocalDateTime;

/**
 * Classe DAO (Data Access Object) abstrata para Usuários.
 * Gerencia o armazenamento e recuperação de todos os tipos de usuários
 * em um mapa estático compartilhado (simulando um banco de dados).
 *
 * @author PAULO VITOR DIAS SOARES
 * @version 1.1
 * @since 2025-11-11
 */
public abstract class UsuarioDAO {

    protected static Map<String, Usuario> usuarios = new ConcurrentHashMap<>();
    protected static int proximoId = 1;
    private static final List<String> logs = new CopyOnWriteArrayList<>();

    /**
     * Salva ou atualiza um usuário no sistema.
     * Se o usuário não tiver ID, gera um novo.
     * 
     * @param usuario O Usuário (Estudante, Professor, Admin) a ser salvo.
     */
    public void salvar(Usuario usuario) {
        if (usuario.getId() == null || usuario.getId().isEmpty()) {
            usuario.setId("USER_" + proximoId++);
        }
        usuarios.put(usuario.getId(), usuario);
        System.out.println("Usuário salvo: " + usuario.getNome());
    }

    /**
     * Verifica se um email já está cadastrado no sistema (case-insensitive).
     * 
     * @param email Email a verificar.
     * @return true se o email já existe, false caso contrário.
     */
    public boolean emailExiste(String email) {
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Busca um Usuário pelo seu ID. (Funcionalidade: Visualizar)
     */
    public Usuario buscarPorId(String id) {
        return usuarios.get(id);
    }

    // Método privado para registrar o log como String
    private void registrarLog(String usuarioId, String nomeUsuario, boolean novoStatus) {
        String statusText = novoStatus ? "Conta Ativada" : "Conta Desativada";
        String logEntry = String.format("[%s] Usuário %s (%s): %s",
                LocalDateTime.now().toString(),
                nomeUsuario,
                usuarioId,
                statusText);
        logs.add(logEntry);
    }

    /**
     * Busca um Usuário (de qualquer tipo) pelo seu Email (case-insensitive).
     * ESSENCIAL para a implementação dos DAOs filhos (ProfessorDAO, EstudanteDAO).
     *
     * @param email O email do usuário.
     * @return O objeto Usuario se encontrado, ou null.
     */
    public Usuario buscarPorEmail(String email) {
        // Aproveita o loop que já existe em emailExiste
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Implementa a funcionalidade de Desativar/Ativar conta.
     * (Funcionalidade: Desativar e Logs)
     * 
     * @param id         O ID do usuário.
     * @param novoStatus true para Ativo, false para Desativado.
     */
    public boolean alterarStatus(String id, boolean novoStatus) {
        Usuario usuario = buscarPorId(id);
        if (usuario != null) {
            if (usuario.isAtivo() != novoStatus) {
                registrarLog(usuario.getId(), usuario.getNome(), novoStatus);
                usuario.setAtivo(novoStatus);
                System.out.println("Status do usuário " + usuario.getNome() + " alterado para: "
                        + (novoStatus ? "ATIVO" : "DESATIVADO"));
            }
            return true;
        }
        return false;
    }

    /**
     * Retorna a lista de logs de alteração de status.
     * (Funcionalidade: Logs)
     */
    public List<String> getLogsDeStatus() {
        return new ArrayList<>(logs);
    }

    // --- MÉTODOS DE BUSCA, PAGINAÇÃO E FILTROS AVANÇADOS (NOVOS) ---

    /**
     * Realiza a busca paginada e filtrada de usuários.
     * (Funcionalidade: Busca, Paginação e Filtros Avançados)
     */
    public List<Usuario> buscarPaginada(String termoBusca, String tipoUsuario, Boolean status, int pagina,
            int tamanhoPagina) {
        List<Usuario> todosUsuarios = new ArrayList<>(usuarios.values());

        // APLICACAO DE FILTROS
        List<Usuario> usuariosFiltrados = todosUsuarios.stream()
                .filter(u -> {
                    boolean matchTipo = (tipoUsuario == null || tipoUsuario.isEmpty())
                            || u.getClass().getSimpleName().equalsIgnoreCase(tipoUsuario);
                    boolean matchStatus = (status == null) || (u.isAtivo() == status);
                    boolean matchTermo = (termoBusca == null || termoBusca.isEmpty()) ||
                            u.getNome().toLowerCase().contains(termoBusca.toLowerCase()) ||
                            u.getEmail().toLowerCase().contains(termoBusca.toLowerCase());
                    return matchTipo && matchStatus && matchTermo;
                })
                .collect(Collectors.toList());

        // PAGINAÇÃO
        int inicio = (pagina - 1) * tamanhoPagina;
        int fim = Math.min(inicio + tamanhoPagina, usuariosFiltrados.size());

        if (inicio >= usuariosFiltrados.size() || inicio < 0) {
            return Collections.emptyList();
        }

        return usuariosFiltrados.subList(inicio, fim);
    }

    /**
     * Retorna o número total de usuários após a aplicação dos filtros.
     * Útil para calcular o total de páginas.
     */
    public int contarUsuariosFiltrados(String termoBusca, String tipoUsuario, Boolean status) {
        return (int) usuarios.values().stream()
                .filter(u -> {
                    boolean matchTipo = (tipoUsuario == null || tipoUsuario.isEmpty())
                            || u.getClass().getSimpleName().equalsIgnoreCase(tipoUsuario);
                    boolean matchStatus = (status == null) || (u.isAtivo() == status);
                    boolean matchTermo = (termoBusca == null || termoBusca.isEmpty()) ||
                            u.getNome().toLowerCase().contains(termoBusca.toLowerCase()) ||
                            u.getEmail().toLowerCase().contains(termoBusca.toLowerCase());
                    return matchTipo && matchStatus && matchTermo;
                })
                .count();
    }
}