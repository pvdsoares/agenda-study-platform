package com.agendastudy.model;
import java.util.ArrayList;
import java.util.List;

public class Professor extends Usuario {
    private List<String> disciplinas;
    private String biografia;
    private List<String> qualificacoes;
    private double precoHora;
    private byte[] fotoPerfil;
    private String tipoImagem;
    private boolean perfilVerificado;

    public Professor(String id, String nome, String email, String senha) {
        super(id, nome, email, senha);
        this.disciplinas = new ArrayList<>();
        this.qualificacoes = new ArrayList<>();
        this.perfilVerificado = false;
    }

     // Getters e Setters
    public List<String> getDisciplinas() { return disciplinas; }
    public void setDisciplinas(List<String> disciplinas) { this.disciplinas = disciplinas; }
    
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    
    public List<String> getQualificacoes() { return qualificacoes; }
    public void setQualificacoes(List<String> qualificacoes) { this.qualificacoes = qualificacoes; }
    
    public double getPrecoHora() { return precoHora; }
    public void setPrecoHora(double precoHora) { this.precoHora = precoHora; }
    
    public byte[] getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(byte[] fotoPerfil, String tipoImagem) {
        this.fotoPerfil = fotoPerfil;
        this.tipoImagem = tipoImagem;
    }

    public boolean isPerfilVerificado() { return perfilVerificado; }
    public void setPerfilVerificado(boolean perfilVerificado) { this.perfilVerificado = perfilVerificado; }

   
    public String getTipoImagem() { 
        return tipoImagem; 
    }


    public void setTipoImagem(String tipoImagem) {
        this.tipoImagem = tipoImagem;
    }

     // Métodos específicos do professor
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
                ", precoHora=" + precoHora +
                ", verificado=" + perfilVerificado +
                '}';
    }
}
