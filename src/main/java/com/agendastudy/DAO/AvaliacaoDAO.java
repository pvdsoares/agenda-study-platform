package com.agendastudy.DAO;
import com.agendastudy.model.Aula;
import com.agendastudy.model.Avaliacao;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.Professor;

import java.time.LocalDateTime;
import java.util.*;


public class AvaliacaoDAO {
    private Map<Professor, List<Avaliacao>> avaliacoesPorProfessor = new HashMap<>();

    // Com comentário
    public void avaliar(Estudante estudante, Aula aula, int nota, String comentario) {
        if (comentario == null) {
            comentario = "";
        }
        avaliarInterno(estudante, aula, nota, comentario);
    }

    // Sem comentário
    public void avaliar(Estudante estudante, Aula aula, int nota) {
        avaliarInterno(estudante, aula, nota, "");
    }

    /**
     * Avalia uma aula já concluída e ainda não avaliada
     *
     * @param estudante estudante que irá avaliar a aula
     * @param aula aula a ser avaliada
     * @param nota nota da avaliação
     * @param comentario comentário da avaliação
     */
    private void avaliarInterno(Estudante estudante, Aula aula, int nota, String comentario) {
        if (podeAvaliar(estudante, aula)) {
            String idAvaliacao = UUID.randomUUID().toString(); //Gera novo ID para cada avaliação
            Avaliacao avaliacao = new Avaliacao(idAvaliacao, estudante, aula, nota, comentario);

            //Se não existir uma lista para o professor é criada uma nova
            avaliacoesPorProfessor.putIfAbsent(aula.getProfessor(), new LinkedList<>());

            //Adiciona a avaliação a lista do professor
            avaliacoesPorProfessor.get(aula.getProfessor()).add(avaliacao);
        }
    }

    /**
     * Verifica se aula pode ser avaliada
     *
     * @param estudante estudante que irá avaliar a aula
     * @param aula aula a ser a ser avaliada
     * @return true se a aula pode ser avaliada, false caso contrário
     */
    public boolean podeAvaliar(Estudante estudante, Aula aula) {
        //se aula ainda não aconteceu, não pode ser avaliada
        if (aula.getDataHora().isAfter(LocalDateTime.now())) {
            return false;
        }

        //se aula cancelada, não pode ser avaliada
        if (aula.isCancelada()) {
            return false;
        }

        //pega as avaliações do professor da aula a ser verificada (ou lista vazia, se não existir)
        List<Avaliacao> avaliacoesDoProfessor = avaliacoesPorProfessor
                .getOrDefault(aula.getProfessor(), Collections.emptyList());

        //verifica apenas as avaliações desse professor. S e aula já avaliada, não pode ser avaliada novamente
        for (Avaliacao a : avaliacoesDoProfessor) {
            if (a.getAula().getIdAula().equals(aula.getIdAula()) &&
                    a.getEstudante().getId().equals(estudante.getId())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Retorna todas as avalições realizadas por um estudante
     *
     * @param estudante estudante cujas as avaliações devem ser retornadas
     * @return uma lista contendo as avaliações feitas pelo estudante
     */
    public List<Avaliacao> getAvaliacaoPorEstudante(Estudante estudante) {
        List<Avaliacao> resultado = new ArrayList<>();
        
        for (List<Avaliacao> lista : avaliacoesPorProfessor.values()) {
            for (Avaliacao a : lista) {
                if (a.getEstudante().getId().equals(estudante.getId())) {
                    resultado.add(a);
                }
            }
        }

        return resultado;
    }
}
