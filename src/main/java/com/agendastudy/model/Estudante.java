package com.agendastudy.model;
import java.util.ArrayList;
import java.util.List;

// talvez adicionar mais métodos no futuro
public class Estudante extends Usuario{
    private List<String> interesses;
    private byte[] fotoPerfil;
    private String tipoImagem;

    public Estudante(String id, String nome, String email, String senha) {
        super(id, nome, email, senha);
        this.interesses = new ArrayList<>();
    }

    public List<String> getInteresses() { return interesses; }

    // vai definir a lista inteira, cogitar usar método adicionar com .add
    public void setInteresses (List<String> interesses){
        this.interesses = interesses;
    }

    public byte[] getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(byte[] fotoPerfil, String tipoImagem) {
        this.fotoPerfil = fotoPerfil;
        this.tipoImagem = tipoImagem;
    }

    @Override
    public void logout() {
        System.out.println("Estudante " + getNome() + " realizou logout.");
    }

    @Override
    public void atualizarPerfil() {
        System.out.println("Perfil do estudante " + getNome() + " atualizado.");
    }

    @Override
    public String toString(){
        return "Estudante{" +
                "id='" + getId() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", interesses=" + interesses +
                '}';
    }
}
