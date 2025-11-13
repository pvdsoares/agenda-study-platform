package com.agendastudy.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Professor extends Usuario {
    private List<String> disciplinas;
    private String biografia;
    private List<String> qualificacoes;
    private byte[] fotoPerfil;
    private String tipoImagem;
    private boolean perfilVerificado;
    private List<Avaliacao> avaliacoes;

    public Professor(String id, String nome, String email, String senha) {
        super(id, nome, email, senha);
        this.disciplinas = new ArrayList<>();
        this.qualificacoes = new ArrayList<>();
        this.perfilVerificado = false;
        this.avaliacoes = new ArrayList<>();
    }

    // Getters e Setters
    public List<String> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<String> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public List<String> getQualificacoes() {
        return qualificacoes;
    }

    public void setQualificacoes(List<String> qualificacoes) {
        this.qualificacoes = qualificacoes;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil, String tipoImagem) {
        this.fotoPerfil = fotoPerfil;
        this.tipoImagem = tipoImagem;
    }

    public boolean isPerfilVerificado() {
        return perfilVerificado;
    }

    public void setPerfilVerificado(boolean perfilVerificado) {
        this.perfilVerificado = perfilVerificado;
    }

    public String getTipoImagem() {
        return tipoImagem;
    }

    public void setTipoImagem(String tipoImagem) {
        this.tipoImagem = tipoImagem;
    }

    public void adicionarDisciplina(String disciplina) {
        if (disciplinas == null) {
            disciplinas = new ArrayList<>();
        }
        disciplinas.add(disciplina);
    }

    public void adicionarQualificacao(String qualificacao) {
        if (qualificacoes == null) {
            qualificacoes = new ArrayList<>();
        }
        qualificacoes.add(qualificacao);
    }

    public boolean temFoto() {
        return fotoPerfil != null && fotoPerfil.length > 0;
    }

    @Override
    public void logout() {
        System.out.println("Professor " + getNome() + " realizou logout.");
    }

    @Override
    public void atualizarPerfil() {
        System.out.println("Perfil do professor " + getNome() + " atualizado.");
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id='" + getId() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", disciplinas=" + disciplinas +
                ", verificado=" + perfilVerificado +
                '}';
    }

   /**
     * // TODO: TAREFA DE MATHEUS (Método Provisório para Teste)
     * *
     * * Este método é uma implementação provisória.
     * * Ele existe APENAS para permitir o teste dos métodos da *minha* tarefa
     * * (ex: getMediaAvaliacoes, getAvaliacoesPorData).
     * *
     * * A implementação FINAL e correta (da task "COMO Estudante QUERO avaliar...")
     * * será feita por MATHEUS e irá substituir este método.
     */
    public void adicionarAvaliacao(Avaliacao novaAvaliacao) {
        if (novaAvaliacao.getProfessor().getId().equals(this.getId())) {
            this.avaliacoes.add(novaAvaliacao);
        } else {
            System.err.println("Erro: Tentativa de adicionar avaliação de outro professor.");
        }
    }

    public double getMediaAvaliacoes() {
        if (avaliacoes.isEmpty()) {
            return 0.0;
        }
        double somaTotal = 0.0;
        for (Avaliacao aval : avaliacoes) {
            somaTotal += aval.getNota();
        }
        return somaTotal / (double) avaliacoes.size();
    }

    public List<Avaliacao> getAvaliacoesPorData() {
        List<Avaliacao> copiaOrdenada = new ArrayList<>(this.avaliacoes);
        copiaOrdenada.sort(Comparator.comparing(Avaliacao::getDataAvaliacao).reversed());
        return copiaOrdenada;
    }

    public List<Avaliacao> getAvaliacoesPorRelevancia() {
        Comparator<Avaliacao> comparadorRelevancia = 
                Comparator.comparingInt(Avaliacao::getNota).reversed();
            PriorityQueue<Avaliacao> maxHeap = new PriorityQueue<>(comparadorRelevancia);
            maxHeap.addAll(this.avaliacoes);
            List<Avaliacao> ordenadasPorRelevancia = new ArrayList<>();
            while (!maxHeap.isEmpty()) {
            ordenadasPorRelevancia.add(maxHeap.poll());
        }
        return ordenadasPorRelevancia;
    }
}
