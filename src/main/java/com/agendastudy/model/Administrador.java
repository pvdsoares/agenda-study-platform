package com.agendastudy.model;

/**
 * Representa um usuário do tipo Administrador no sistema.
 * Esta classe herda de Usuario e define as responsabilidades
 * específicas de um administrador.
 *
 * @author Alexandro Costa Santos
 * @version 1.0
 * @since 2025-11-13
 */
public class Administrador extends Usuario {

    /**
     * Construtor padrão da classe Administrador.
     * Chama o construtor da classe pai (Usuario) para inicializar
     * os atributos comuns.
     *
     * @param id    O ID único do administrador.
     * @param nome  O nome completo do administrador.
     * @param email O email de login do administrador.
     * @param senha A senha de acesso do administrador.
     */
    public Administrador(String id, String nome, String email, String senha) {
        super(id, nome, email, senha);
    }

    /**
     * Implementação do método abstrato 'logout' da classe Usuario.
     */
    @Override
    public void logout() {
        System.out.println("Administrador " + getNome() + " realizou logout.");
    }

    /**
     * Implementação do método abstrato 'atualizarPerfil' da classe Usuario.
     */
    @Override
    public void atualizarPerfil() {
        System.out.println("Perfil do administrador " + getNome() + " atualizado.");
    }

    /**
     * Retorna uma representação em String do objeto Administrador.
     *
     * @return Uma String formatada com o id, nome e email do administrador.
     */
    @Override
    public String toString() {
        return "Administrador{" +
                "id='" + getId() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}