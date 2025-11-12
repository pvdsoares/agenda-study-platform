package com.agendastudy.model;
import java.time.LocalDateTime;

public class Aula {
    private String idAula;
    private String titulo;
    private String descricao;
    private Professor professor; // as informações de professores e alunos vão ser acessadas da suas respectivas classes, como id por exemplo.
    private Estudante estudante;
    private LocalDateTime dataHora; 

    public Aula(String idAula, String titulo, String descricao, Professor professor, Estudante estudante, LocalDateTime dataHora) {
        this.idAula = idAula;
        this.titulo = titulo;
        this.descricao = descricao;
        this.professor = professor;
        this.estudante = estudante;
        this.dataHora = dataHora;
    }

    public String getIdAula() { return idAula; }
    public void setIdAula(String idAula) { this.idAula = idAula; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }

    public Estudante getEstudante() { return estudante; }
    public void setEstudante(Estudante estudante) { this.estudante = estudante; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    @Override
    public String toString() {
        return "Aula{" +
                "idAula='" + idAula + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", professor=" + professor.getNome() + "(ID: " + professor.getId() + ")" +
                ", estudante=" + estudante.getNome() + "(ID: " + estudante.getId() + ")" +
                ", dataHora=" + dataHora +
                '}';
    }

}   