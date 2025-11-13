package com.agendastudy.DAO;

import com.agendastudy.model.Usuario;
import java.util.HashMap;
import java.util.Map;

public abstract class UsuarioDAO {
    protected static Map<String, Usuario> usuarios = new HashMap<>();
    protected static int proximoId = 1;

    
    /**
     * Salva um usuário no sistema
     * @param usuario Usuario a ser salvo
     */
    public void salvar(Usuario usuario) {
        if (usuario.getId() == null || usuario.getId().isEmpty()) {
            usuario.setId("USER_" + proximoId++);
        }
        usuarios.put(usuario.getId(), usuario);
        System.out.println("Usuário salvo: " + usuario.getNome());
    }

    /**
     * Verifica se email já existe
     * @param email Email a verificar
     * @return true se email já exixte
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
