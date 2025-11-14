package com.agendastudy.model;

import java.time.LocalDateTime;

/**
 * Representa a Avaliação de uma Aula feita por um Estudante.
 *
 * @author MATHEUS MARCELINO DE ARRUDA
 * @version 1.0
 * @since 2025-11-12
 */
public class Avaliacao {
    private String idAvaliacao;
    private Estudante estudante;
    private Aula aula;
    private Professor professor;
    private int nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;

    /**
     * Construtor da classe Avaliação.
     * @param idAvaliacao O ID único da avaliação.
     * @param estudante O Estudante que fez a avaliação.
     * @param aula A Aula que foi avaliada.
     * @param nota A nota (1-5) atribuída.
     * @param comentario O comentário textual.
     */
    public Avaliacao(String idAvaliacao, Estudante estudante, Aula aula, int nota, String comentario) {
        this.idAvaliacao = idAvaliacao;
        this.estudante = estudante;
        this.aula = aula;
        this.professor = aula.getProfessor(); // pega automaticamente o professor da aula
        this.setNota(nota); // Usa o setter para validar
        this.comentario = comentario;
        this.dataAvaliacao = LocalDateTime.now();
    }

    // --- Getters e Setters ---
    public String getIdAvaliacao() { return idAvaliacao; }

    public Estudante getEstudante() { return estudante; }

    public Aula getAula() { return aula; }

    public Professor getProfessor() { return professor; }

    public int getNota() { return nota; }

    /**
     * Define a nota, garantindo que esteja entre 1 e 5.
     * @param nota A nota (1-5).
     */
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