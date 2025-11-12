package com.agendastudy.model;

import java.time.LocalDateTime;

public class Avaliacao {
    private String idAvaliacao;
    private Estudante estudante;
    private Aula aula;
    private Professor professor;
    private int nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;

    public Avaliacao(String idAvaliacao, Estudante estudante, Aula aula, int nota, String comentario) {
        this.idAvaliacao = idAvaliacao;
        this.estudante = estudante;
        this.aula = aula;
        this.professor = aula.getProfessor(); // pega automaticamente o professor da aula
        this.nota = nota;
        this.comentario = comentario;
        this.dataAvaliacao = LocalDateTime.now();
    }

    // Getters e Setters
    public String getIdAvaliacao() { return idAvaliacao; }

    public Estudante getEstudante() { return estudante; }

    public Aula getAula() { return aula; }

    public Professor getProfessor() { return professor; }

    public int getNota() { return nota; }

    public void setNota(int nota) {
        if (nota < 1 || nota > 5)
            throw new IllegalArgumentException("A nota deve ser entre 1 e 5");
        this.nota = nota;
    }

    public String getComentario() { return comentario; }

    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDateTime getDataAvaliacao() { return dataAvaliacao; }

    @Override
    public String toString() {
        return "Avaliacao{" +
                "idAvaliacao='" + idAvaliacao + '\'' +
                ", estudante=" + estudante.getNome() +
                ", professor=" + professor.getNome() +
                ", aula='" + aula.getTitulo() + '\'' +
                ", nota=" + nota +
                ", comentario='" + comentario + '\'' +
                ", data=" + dataAvaliacao +
                '}';
    }
}
