package com.agendastudy.model;

/**
 * Representa uma notificação gerada pelo sistema.
 * Armazena o usuário, o texto e a prioridade.
 *
 * Utilizada no SCRUM-114.
 *
 * @author Enzo Andrade
 * @version 1.0
 */
public class Notificacao implements Comparable<Notificacao> {

    private final String usuarioId;
    private final TipoNotificacao tipo;
    private final String mensagem;
    private final int prioridade;

    public Notificacao(String usuarioId, TipoNotificacao tipo, String mensagem, int prioridade) {
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.mensagem = mensagem;
        this.prioridade = prioridade;
    }

    public String getUsuarioId() { return usuarioId; }
    public TipoNotificacao getTipo() { return tipo; }
    public String getMensagem() { return mensagem; }
    public int getPrioridade() { return prioridade; }

    @Override
    public int compareTo(Notificacao outra) {
        return Integer.compare(this.prioridade, outra.prioridade);
    }
}
