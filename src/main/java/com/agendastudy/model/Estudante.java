package com.agendastudy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um usuário do tipo Estudante no sistema.
 * Herda da classe Usuário.
 *
 * @author VINICIUS ALVES RIBEIRO SILVA
 * @version 1.0
 * @since 2025-11-11
 */
public class Estudante extends Usuario {
    private List<String> interesses;
    private byte[] fotoPerfil;
    // private String tipoImagem; // REMOVED unused

    /**
     * Construtor da classe Estudante.
     * 
     * @param id    O ID único do estudante.
     * @param nome  O nome completo do estudante.
     * @param email O email de login do estudante.
     * @param senha A senha de acesso do estudante.
     */
    public Estudante(String id, String nome, String email, String senha) {
        super(id, nome, email, senha);
        this.interesses = new ArrayList<>();
    }

    /**
     * Adiciona um novo interesse à lista de interesses do estudante.
     * 
     * @param interesse A String de interesse a ser adicionada.
     * @throws IllegalArgumentException se o interesse for nulo ou vazio.
     */
    public void adicionarInteresse(String interesse) {
        if (interesse != null && !interesse.trim().isEmpty()) {
            this.interesses.add(interesse.trim());
        } else {
            throw new IllegalArgumentException("Interesse inválido.");
        }
    }

    // --- Getters e Setters ---

    public List<String> getInteresses() {
        return interesses;
    }

    // vai definir a lista inteira, cogitar usar método adicionar com .add
    public void setInteresses(List<String> interesses) {
        this.interesses = interesses;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    // --- Métodos de Usuário (Override) ---

    @Override
    public void logout() {
        System.out.println("Estudante " + getNome() + " realizou logout.");
    }

    @Override
    public void atualizarPerfil() {
        System.out.println("Perfil do estudante " + getNome() + " atualizado.");
    }

    @Override
    public String toString() {
        return "Estudante{" +
                "id='" + getId() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", interesses=" + interesses +
                '}';
    }
}