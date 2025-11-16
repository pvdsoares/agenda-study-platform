package com.agendastud.service;

import com.agendastud.model.Professor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável por aplicar as regras de negócio de busca e filtragem.
 */
public class BuscaService {

    // Lista de professores (Deve ser populada pelo DAO/Repositório ou API)
    private final List<Professor> professoresDisponiveis;

    /**
     * Construtor. Recebe a lista completa de professores da camada de dados.
     * @param professores Lista completa de professores no sistema.
     */
    public BuscaService(List<Professor> professores) {
        this.professoresDisponiveis = professores;
    }

    /**
     * Filtra a lista de professores aplicando todos os critérios simultaneamente (SCRUM-3).
     * @param criterios Os critérios de filtro definidos pelo usuário (Tela 3).
     * @return Lista de professores que satisfazem todas as condições.
     */
    public List<Professor> filtrarProfessores(CriteriosBusca criterios) {

        return professoresDisponiveis.stream()
                // 1. Filtro por Disciplina (SCRUM-6)
                .filter(p -> matchDisciplina(p, criterios.disciplinasSelecionadas))

                // 2. Filtro por Preço (SCRUM-4)
                .filter(p -> matchPreco(p, criterios.precoMin, criterios.precoMax))

                // 3. Filtro por Localização (SCRUM-5)
                .filter(p -> matchLocalizacao(p, criterios.distanciaMaxKm))

                // 4. Busca Livre (Texto da barra de busca)
                .filter(p -> matchBuscaLivre(p, criterios.textoBuscaLivre))

                .collect(Collectors.toList());
    }

    // --- Métodos Auxiliares de Filtragem ---

    private boolean matchDisciplina(Professor p, List<String> disciplinasSelecionadas) {
        if (disciplinasSelecionadas == null || disciplinasSelecionadas.isEmpty()) {
            return true;
        }
        return disciplinasSelecionadas.stream()
                .map(String::toLowerCase)
                .anyMatch(d -> p.getDisciplina().toLowerCase().equals(d));
    }

    private boolean matchPreco(Professor p, Double precoMin, Double precoMax) {
        boolean matchMin = (precoMin == null || precoMin <= 0 || p.getPrecoHora() >= precoMin);
        boolean matchMax = (precoMax == null || precoMax <= 0 || p.getPrecoHora() <= precoMax);
        return matchMin && matchMax;
    }

    private boolean matchLocalizacao(Professor p, Double distanciaMaxKm) {
        if (distanciaMaxKm == null || distanciaMaxKm <= 0) {
            return true;
        }
        return p.getDistanciaKm() <= distanciaMaxKm;
    }

    private boolean matchBuscaLivre(Professor p, String textoBuscaLivre) {
        if (textoBuscaLivre == null || textoBuscaLivre.trim().isEmpty()) {
            return true;
        }
        String busca = textoBuscaLivre.toLowerCase();
        return p.getNome().toLowerCase().contains(busca) ||
                p.getDisciplina().toLowerCase().contains(busca);
    }
}