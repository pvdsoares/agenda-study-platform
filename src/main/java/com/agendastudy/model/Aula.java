package com.agendastudy.model;
import java.time.LocalDateTime;

/**
 * Representa uma Aula agendada na plataforma.
 * Conecta um Professor e um Estudante (opcionalmente) em um horário específico.
 *
 * @author VINICIUS ALVES RIBEIRO SILVA
 * @version 1.0
 * @since 2025-11-11
 */
public class Aula {
    private String idAula;
    private String titulo;
    private String descricao;
    private Professor professor; // as informações de professores e alunos vão ser acessadas da suas respectivas classes, como id por exemplo.
    private Estudante estudante;
    private LocalDateTime dataHora; 
    private boolean cancelada;

    /**
     * Construtor da classe Aula.
     * @param idAula O ID único da aula (pode ser nulo para nova aula).
     * @param titulo O título da aula.
     * @param descricao Uma breve descrição do conteúdo.
     * @param professor O Professor que ministrará a aula.
     * @param estudante O Estudante que participará da aula (pode ser nulo se for apenas disponibilidade).
     * @param dataHora A data e hora agendadas.
     */
    public Aula(String idAula, String titulo, String descricao, Professor professor, Estudante estudante, LocalDateTime dataHora) {
        this.idAula = idAula;
        this.titulo = titulo;
        this.descricao = descricao;
        this.professor = professor;
        this.estudante = estudante;
        this.dataHora = dataHora;
        this.cancelada = false;
    }

    // --- Getters e Setters ---
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

    public boolean isCancelada() { return cancelada; }
    public void setCancelada(boolean cancelada) { this.cancelada = cancelada; }
    
    /**
     * Marca a aula como cancelada.
     */
    public void cancelar(){
        this.cancelada = true;
    }


    @Override
    public String toString() {
        return "Aula{" +
                "idAula='" + idAula + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", professor=" + professor.getNome() + "(ID: " + professor.getId() + ")" +
                ", estudante=" + (estudante != null ? estudante.getNome() : "VAGA") + "(