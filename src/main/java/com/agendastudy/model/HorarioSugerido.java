package com.agendastudy.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa um slot de tempo calculado e sugerido pelo sistema
 * para reagendamento.
 * * É uma classe de dados simples, focada em retornar o Início e Fim da
 * Sugestão.
 *
 * @author Paulo Vitor Dias Soares
 * @version 1.0
 * @since 2025-11-29
 */
public final class HorarioSugerido {

    // Início e Fim do slot sugerido
    private final LocalDateTime inicio;
    private final LocalDateTime fim;

    // Formatador para exibição na tela/logs (opcional, mas útil)
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM HH:mm");

    /**
     * Construtor da classe HorarioSugerido.
     * * @param inicio O horário de início da aula sugerida.
     * 
     * @param fim O horário de fim da aula sugerida.
     */
    public HorarioSugerido(LocalDateTime inicio, LocalDateTime fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

    // --- Getters ---

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    /**
     * Retorna uma representação amigável do horário.
     */
    @Override
    public String toString() {
        return "Sugestão: " + inicio.format(FORMATTER) + " a " + fim.format(FORMATTER);
    }
}