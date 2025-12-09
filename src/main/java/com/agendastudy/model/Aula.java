package com.agendastudy.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa uma Aula agendada na plataforma.
 * Conecta um Professor e um Estudante (opcionalmente) em um horário específico.
 *
 * @author VINICIUS ALVES RIBEIRO SILVA
 * @version 1.0
 * @since 2025-11-11
 */
public class Aula implements Serializable {
    private String idAula;
    private String titulo;
    private String descricao;
    private Professor professor; // as informações de professores e alunos vão ser acessadas da suas respectivas
                                 // classes, como id por exemplo.
    private Estudante estudante;
    private LocalDateTime dataHora;
    private int duracaoMinutos;
    private StatusAula status; // Enum criado para as estatísticas do relatório de rendimento

    /**
     * Construtor da classe Aula.
     * 
     * @param idAula         O ID único da aula (pode ser nulo para nova aula).
     * @param titulo         O título da aula.
     * @param descricao      Uma breve descrição do conteúdo.
     * @param professor      O Professor que ministrará a aula.
     * @param estudante      O Estudante que participará da aula (pode ser nulo se
     *                       for apenas disponibilidade).
     * @param dataHora       A data e hora agendadas.
     * @param duracaoMinutos A duração da aula em minutos.
     */
    public Aula(String idAula, String titulo, String descricao, Professor professor, Estudante estudante,
            LocalDateTime dataHora, int duracaoMinutos) {
        this.idAula = idAula;
        this.titulo = titulo;
        this.descricao = descricao;
        this.professor = professor;
        this.estudante = estudante;
        this.dataHora = dataHora;
        this.duracaoMinutos = duracaoMinutos;
        this.status = StatusAula.AGENDADA;
    }

    // --- Getters e Setters ---
    public String getIdAula() {
        return idAula;
    }

    public void setIdAula(String idAula) {
        this.idAula = idAula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusAula getStatus() {
        return status;
    }

    public void setStatus(StatusAula status) {
        this.status = status;
    }

    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(int duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    /**
     * Método de conveniência para calcular o horário de término.
     */
    public LocalDateTime getFimAula() {
        return this.dataHora.plusMinutes(this.duracaoMinutos);
    }

    /**
     * Marca a aula como cancelada pelo aluno.
     */
    public void cancelarPorAluno() {
        this.status = StatusAula.CANCELADA_ALUNO;
    }

    /**
     * Marca a aula como cancelada pelo professor.
     */
    public void cancelarPorProfessor() {
        this.status = StatusAula.CANCELADA_PROFESSOR;
    }

    public boolean isCancelada() {
        return this.status == StatusAula.CANCELADA_ALUNO || this.status == StatusAula.CANCELADA_PROFESSOR;
    }

    /**
     * Permite que duas Aulas sejam comparadas usando APENAS o ID.
     * Essencial para remover a aula original da lista de conflitos em um Set/List.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        // O idAula pode ser nulo para aulas novas não salvas
        if (o == null || getClass() != o.getClass() || idAula == null)
            return false;

        Aula aula = (Aula) o;
        // Compara se os IDs são iguais
        return idAula.equals(aula.idAula);
    }

    /**
     * Garante que objetos iguais tenham o mesmo código hash.
     * Baseado apenas no idAula.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idAula);
    }

    @Override
    public String toString() {
        return "Aula{" +
                "idAula='" + idAula + '\'' +
                ", titulo='" + titulo + '\'' +
                ", status=" + status + // Adiciona o status da aula
                ", dataHora=" + dataHora + // Adiciona a data e hora
                ", descricao='" + descricao + '\'' +
                ", professor=" + professor.getNome() + " (ID: " + professor.getId() + ")" +
                ", estudante=" + (estudante != null ? estudante.getNome() : "VAGA ABERTA") +
                '}';
    }

}