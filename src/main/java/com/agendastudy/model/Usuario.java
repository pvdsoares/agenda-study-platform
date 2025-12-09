package com.agendastudy.model;


import java.io.Serializable;

/**
 * Classe abstrata que representa um Usuário genérico no sistema.
 * Serve como base para Estudante, Professor e Administrador.
 *
 * @author PAULO VITOR DIAS SOARES
 * @version 1.0
 * @since 2025-11-09
 */
public abstract class Usuario implements Serializable {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String endereco;
    private boolean ativo = true; 

    /**
     * Construtor da classe Usuário.
     * @param id O ID único do usuário.
     * @param nome O nome completo do usuário.
     * @param email O email de login do usuário.
     * @param senha A senha de acesso do usuário.
     */
    public Usuario(String id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

     // --- Getters e Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    // NOVO GETTER e SETTER para o status
    /**
     * Verifica se o usuário está ativo.
     */
    public boolean isAtivo() {
        return ativo;
    }
    /**
     * Define o status de ativação do usuário (true: Ativo, false: Desativado).
     */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    // --- Métodos Abstratos ---
    /**
     * Define o comportamento de logout para o usuário.
     */
    public abstract void logout();

    /**
     * Define o comportamento de atualização de perfil para o usuário.
     */
    public abstract void atualizarPerfil();
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
