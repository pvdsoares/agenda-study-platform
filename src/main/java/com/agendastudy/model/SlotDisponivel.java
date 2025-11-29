package com.agendastudy.model;

import java.time.LocalDateTime;

/**
 * Representa um bloco de tempo em que o Professor está disponível
 * para agendar aulas.
 * * É uma classe imutável (final) para garantir a integridade dos horários.
 *
 * @author Paulo Vitor Dias Soares
 * @version 1.0
 * @since 2025-11-29
 */
public final class SlotDisponivel {

    private final String professorId;
    private final LocalDateTime inicio;
    private final LocalDateTime fim;

    /**
     * Construtor da classe SlotDisponivel.
     * * @param professorId O ID do professor que possui este slot.
     * 
     * @param inicio O horário de início do bloco de disponibilidade.
     * @param fim    O horário de fim do bloco de disponibilidade.
     */
    public SlotDisponivel(String professorId, LocalDateTime inicio, LocalDateTime fim) {
        this.professorId = professorId;
        this.inicio = inicio;
        this.fim = fim;
    }

    // --- Getters ---
    // A classe não possui Setters, garantindo a imutabilidade.

    public String getProfessorId() {
        return professorId;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }
}