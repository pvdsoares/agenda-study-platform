package com.agendastud.service;

import java.util.List;

/**
 * Objeto DTO (Data Transfer Object) para carregar todos os filtros da interface.
 */
public class CriteriosBusca {
    public List<String> disciplinasSelecionadas;
    public Double precoMin;
    public Double precoMax;
    public Double distanciaMaxKm;
    public String textoBuscaLivre;
}