package com.agendastudy.DAO;

import com.agendastudy.model.Usuario;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe DAO (Data Access Object) abstrata para Usuários.
 * Gerencia o armazenamento e recuperação de todos os tipos de usuários
 * em um mapa estático compartilhado (simulando um banco de dados).
 *
 * @author PAULO VITOR DIAS SOARES
 * @version 1.0
 * @since 2025-11-11
 */
public abstract class UsuarioDAO {
    
    /** Mapa estático para simular a tabela 'usuarios' do banco de dados */
    protected static Map<String, Usuario> usuarios = new HashMap<>();
    /** Contador para gerar IDs únicos */
    protected static int proximoId = 1;

    // NOVO: Lista estática para logs (armazenando Strings formatadas)
    private static final List<String> logs = new ArrayList<>();
    /**
     * Salva ou atualiza um usuário no sistema.
     * Se o usuário não tiver ID, gera um novo.
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
}
