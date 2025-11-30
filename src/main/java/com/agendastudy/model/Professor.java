package com.agendastudy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um usuário do tipo Professor no sistema.
 * Esta classe armazena os dados e estado do professor.
 *
 * @author VINICIUS ALVES RIBEIRO SILVA
 * @version 1.1
 * @since 2025-11-12
 */
public class Professor extends Usuario {
    private List<String> disciplinas;
    private String biografia;
    private List<String> qualificacoes;
    private byte[] fotoPerfil;
    private String tipoImagem;
    private boolean perfilVerificado;
    private double taxaCancelamento;

    /**
     * Lista de avaliações recebidas pelo professor.
     * Esta lista é preenchida pelo AvaliacaoDAO e lida pelo ProfessorDAO.
     */
    private List<Avaliacao> avaliacoes;

    /**
     * Construtor da classe Professor.
     *
     * @param id    O ID único do professor.
     * @param nome  O nome completo do professor.
     * @param email O email de login do professor.
     * @param senha A senha de acesso do professor.
     */
    public Professor(String id, String nome, String email, String senha) {
        super(id, nome, email, senha);
        this.disciplinas = new ArrayList<>();
        this.qualificacoes = new ArrayList<>();
        this.perfilVerificado = false;
        this.avaliacoes = new ArrayList<>(); // A inicialização continua aqui
    }

    // --- Getters e Setters ---

    public List<String> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<String> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public List<String> getQualificacoes() {
        return qualificacoes;
    }

    public void setQualificacoes(List<String> qualificacoes) {
        this.qualificacoes = qualificacoes;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil, String tipoImagem) {
        this.fotoPerfil = fotoPerfil;
        this.tipoImagem = tipoImagem;
    }

    public boolean isPerfilVerificado() {
        return perfilVerificado;
    }

    public void setPerfilVerificado(boolean perfilVerificado) {
        this.perfilVerificado = perfilVerificado;
    }

    public String getTipoImagem() {
        return tipoImagem;
    }

    public void setTipoImagem(String tipoImagem) {
        this.tipoImagem = tipoImagem;
    }

    // --- Métodos de Lógica de Negócio (Modelo) ---

    public void adicionarDisciplina(String disciplina) {
        if (disciplinas == null) {
            disciplinas = new ArrayList<>();
        }
        disciplinas.add(disciplina);
    }

    public void adicionarQualificacao(String qualificacao) {
        if (qualificacoes == null) {
            qualificacoes = new ArrayList<>();
        }
        qualificacoes.add(qualificacao);
    }

    public boolean temFoto() {
        return fotoPerfil != null && fotoPerfil.length > 0;
    }

    /**
     * Retorna a lista bruta de avaliações do professor.
     * O processamento (média, ordenação) é feito pelo ProfessorDAO.
     *
     * @return A lista de objetos Avaliacao.
     * @author Alexandro Costa Santos
     */
    public List<Avaliacao> getAvaliacoes() {
        return this.avaliacoes;
    }

    // --- Métodos de Usuário (Override) ---

    @Override
    public void logout() {
        System.out.println("Professor " + getNome() + " realizou logout.");
    }

    @Override
    public void atualizarPerfil() {
        System.out.println("Perfil do professor " + getNome() + " atualizado.");
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id='" + getId() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", disciplinas=" + disciplinas +
                ", verificado=" + perfilVerificado +
                '}';
    }

    /**
     * Retorna a principal disciplina do professor (ou todas, unidas).
     * Necessário para o método de busca que espera uma String.
     *
     * @return A primeira disciplina da lista ou null/vazio se não houver.
     */
    public String getDisciplina() {
        if (this.disciplinas == null || this.disciplinas.isEmpty()) {
            return "";
        }
        // Retorna a primeira disciplina como a principal para a busca,
        // ou considere concatenar todas elas (ex: "Matemática, Física").
        return this.disciplinas.get(0);
    }

    // Adicione este campo (para a localização do professor)
    private String localizacao;

    // Adicione este campo (este valor deve ser preenchido/calculado pelo seu
    // Service/DAO)
    private double distanciaKm;

    // --- Getters e Setters (adicionar) ---

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    /**
     * Retorna a distância em Km até o local do estudante.
     * Este é o método que o BuscaService está procurando.
     */
    public double getDistanciaKm() {
        return distanciaKm;
    }

    /**
     * Usado para setar o valor antes de ser filtrado.
     */
    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public double getTaxaCancelamento() {
        return this.taxaCancelamento;
    }

    public void setTaxaCancelamento(double taxaCancelamento) {
        this.taxaCancelamento = taxaCancelamento;
    }

}