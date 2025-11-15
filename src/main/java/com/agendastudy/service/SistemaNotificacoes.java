package com.agendastudy.service;

import com.agendastudy.model.Notificacao;
import com.agendastudy.model.TipoNotificacao;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Sistema central de notificações (SCRUM-114).
 *
 * Esta versão implementa o processamento em background
 * usando um ExecutorService e as duas filas (ConcurrentLinkedQueue e PriorityBlockingQueue)
 *
 * @author Enzo Andrade 
 * @version 1.0
 */
public class SistemaNotificacoes {

    private static final SistemaNotificacoes instancia = new SistemaNotificacoes();

    // --- As 4 Estruturas do SCRUM-114  ---
    
    // 1. Fila de ENTRADA thread-safe (rápida para adicionar)
    private final ConcurrentLinkedQueue<Notificacao> filaEntrada = new ConcurrentLinkedQueue<>();

    // 2. Fila de PROCESSAMENTO (lenta, mas ordenada por prioridade)
    private final PriorityBlockingQueue<Notificacao> filaPrioridade = new PriorityBlockingQueue<>();

    // 3. Configurações do usuário (ex: não perturbe)
    private final Map<String, Boolean> configuracoesUsuarios = new ConcurrentHashMap<>();

    // 4. Templates de mensagem
    private final Map<TipoNotificacao, String> templates = new EnumMap<>(TipoNotificacao.class);
    
    // --- Fim das Estruturas ---

    // O "Motor" que roda as tarefas em background
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    private volatile boolean running = true;

    /**
     * Construtor privado que inicia as tarefas em background.
     */
    private SistemaNotificacoes() {
        carregarTemplates();
        
        // Tarefa 1: Move da fila de entrada para a fila de prioridade
        executor.scheduleAtFixedRate(this::moverParaPrioridade, 0, 1, TimeUnit.SECONDS);
        
        // Tarefa 2: Processa a fila de prioridade (envia a mais urgente)
        executor.scheduleAtFixedRate(this::processarFilaPrioridade, 0, 2, TimeUnit.SECONDS);
    }

    public static SistemaNotificacoes getInstancia() {
        return instancia;
    }

    private void carregarTemplates() {
        templates.put(TipoNotificacao.AULA_CANCELADA, "Atenção: Sua aula foi cancelada.");
        
    }

    /**
     * Método PÚBLICO que o ServicoAgendamento chama.
     */
    public void enfileirar(String usuarioId, TipoNotificacao tipo, String mensagem, int prioridade) {
        if (!running) return;
        
        // Adiciona na fila de entrada rápida
        filaEntrada.add(new Notificacao(usuarioId, tipo, mensagem, prioridade));
    }

    /**
     * Tarefa 1 (Background): Esvazia a fila de entrada
     * e joga tudo na fila de prioridade.
     */
    private void moverParaPrioridade() {
        if (!running) return;
        Notificacao notificacao;
        while ((notificacao = filaEntrada.poll()) != null) {
            filaPrioridade.add(notificacao); // A PriorityQueue ordena sozinha
        }
    }

    /**
     * Tarefa 2 (Background): Processa a fila de prioridade.
     * Pega o item mais urgente e "entrega".
     */
    private void processarFilaPrioridade() {
        if (!running) return;
        
        Notificacao notificacao = filaPrioridade.poll(); // Pega o item mais urgente
        if (notificacao != null) {
            
            // Verifica o HashMap de configurações
            if (configuracoesUsuarios.getOrDefault(notificacao.getUsuarioId(), true)) {
                entregarNotificacao(notificacao);
            }
        }
    }

    /**
     * Simula a entrega da notificação (popup, email, etc.)
     */
    private void entregarNotificacao(Notificacao n) {
        System.out.println("[NOTIFICAÇÃO] (Prioridade: " + n.getPrioridade() + ") Para " 
                           + n.getUsuarioId() + ": " + n.getMensagem());
    }

    /**
     * Desliga o serviço (chamado pelo App.java ao fechar)
     */
    public void shutdown() {
        running = false;
        executor.shutdown();
    }
}