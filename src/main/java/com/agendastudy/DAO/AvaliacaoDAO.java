package com.agendastudy.DAO;

import com.agendastudy.model.Estudante;
import com.agendastudy.model.Professor;
import com.agendastudy.model.Avaliacao;
import com.agendastudy.model.Aula;
import com.agendastudy.model.StatusAula;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Gerencia as operações de acesso a dados (DAO) para a entidade Avaliacao.
 * Armazena e recupera avaliações, associando-as aos professores.
 *
 * @author MATHEUS PEREIRA RODRIGUES
 * @version 1.0
 * @since 2025-11-13
 */
public class AvaliacaoDAO {
    // Simula uma tabela de avaliações, agrupada por professor
    private Map<Professor, List<Avaliacao>> avaliacoesPorProfessor = new HashMap<>();

    /**
     * Construtor sem parâmetros que chama o método que faz a leitura de arquivos, para resgatar as avaliações
     * que já houvessem sido feitas.
     */
    public AvaliacaoDAO () {
        lerArquivo();
    }

    /**
     * Método público para avaliar uma aula (com comentário).
     * 
     * @param estudante  O estudante que está avaliando.
     * @param aula       A aula a ser avaliada.
     * @param nota       A nota de 1 a 5.
     * @param comentario O feedback em texto.
     * @return true se a avaliação foi realizada com sucesso, false caso contrário.
     */
    public boolean avaliar(Estudante estudante, Aula aula, int nota, String comentario) {
        if (comentario == null) {
            comentario = "";
        }
        return avaliarInterno(estudante, aula, nota, comentario);
    }

    /**
     * Método público para avaliar uma aula (sem comentário).
     * 
     * @param estudante O estudante que está avaliando.
     * @param aula      A aula a ser avaliada.
     * @param nota      A nota de 1 a 5.
     * @return true se a avaliação foi realizada com sucesso, false caso contrário.
     */
    public boolean avaliar(Estudante estudante, Aula aula, int nota) {
        return avaliarInterno(estudante, aula, nota, "");
    }

    /**
     * Lógica interna para criar e armazenar a avaliação.
     * Verifica se a aula pode ser avaliada antes de salvar.
     *
     * @param estudante  O estudante que está avaliando.
     * @param aula       A aula a ser avaliada.
     * @param nota       A nota de 1 a 5.
     * @param comentario O feedback em texto.
     * @return true se a avaliação foi criada e armazenada com sucesso, false caso
     *         contrário.
     */
    private boolean avaliarInterno(Estudante estudante, Aula aula, int nota, String comentario) {
        boolean avaliado;
        if (podeAvaliar(estudante, aula)) {
            String idAvaliacao = UUID.randomUUID().toString(); // Gera novo ID para cada avaliação
            Avaliacao avaliacao = new Avaliacao(idAvaliacao, estudante, aula, nota, comentario);

            // Se não existir uma lista para o professor, cria uma nova
            avaliacoesPorProfessor.putIfAbsent(aula.getProfessor(), new LinkedList<>());

            // Adiciona a avaliação à lista do professor no DAO
            avaliacoesPorProfessor.get(aula.getProfessor()).add(avaliacao);

            // Adiciona também na lista interna do Model Professor
            // (ESSENCIAL PARA OS MÉTODOS 'get' do ProfessorDAO)
            aula.getProfessor().getAvaliacoes().add(avaliacao);

            avaliado = true;
            salvarArquivo();

        } else {
            System.err.println("Erro: Aula " + aula.getIdAula() + " não pode ser avaliada.");
            avaliado = false;
        }

        return avaliado;
    }

    /**
     * Verifica se uma aula pode ser avaliada pelo estudante.
     * Regras: Aula deve ter ocorrido, não pode estar cancelada,
     * e não pode ter sido avaliada pelo mesmo estudante antes.
     *
     * @param estudante O estudante que está tentando avaliar.
     * @param aula      A aula a ser verificada.
     * @return true se a aula pode ser avaliada, false caso contrário.
     */
    public boolean podeAvaliar(Estudante estudante, Aula aula) {
        // se aula ainda não aconteceu, não pode ser avaliada
        if (aula.getDataHora().isAfter(LocalDateTime.now())) {
            return false;
        }

        // se aula cancelada, não pode ser avaliada
        if ((aula.getStatus() == StatusAula.CANCELADA_ALUNO)
                || (aula.getStatus() == StatusAula.CANCELADA_PROFESSOR)) {
            return false;
        }

        // pega as avaliações do professor da aula (ou lista vazia, se não existir)
        List<Avaliacao> avaliacoesDoProfessor = avaliacoesPorProfessor
                .getOrDefault(aula.getProfessor(), Collections.emptyList());

        // Se a aula já foi avaliada por este estudante, não pode avaliar de novo
        for (Avaliacao a : avaliacoesDoProfessor) {
            if (a.getAula().getIdAula().equals(aula.getIdAula()) &&
                    a.getEstudante().getId().equals(estudante.getId())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Retorna todas as avaliações feitas por um estudante específico.
     *
     * @param estudante O estudante cujas as avaliações devem ser retornadas.
     * @return uma lista contendo as avaliações feitas pelo estudante.
     */
    public List<Avaliacao> getAvaliacaoPorEstudante(Estudante estudante) {
        List<Avaliacao> resultado = new ArrayList<>();

        // Itera por todas as listas de avaliação de todos os professores
        for (List<Avaliacao> lista : avaliacoesPorProfessor.values()) {
            for (Avaliacao a : lista) {
                if (a.getEstudante().getId().equals(estudante.getId())) {
                    resultado.add(a);
                }
            }
        }
        return resultado;
    }

    /**
     * Ler os dados das avaliações feitas que foram salvas em arquivo.
     */
    private void lerArquivo() {
        try {
            FileInputStream fluxoArquivo = new FileInputStream("src/main/resources/data/avaliacoes.ser");
            ObjectInputStream fluxoObjetos = new ObjectInputStream(fluxoArquivo);

            avaliacoesPorProfessor = (Map<Professor, List<Avaliacao>>) fluxoObjetos.readObject();

            fluxoArquivo.close();
            fluxoObjetos.close();

        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Salava as avaliações feitas em um arquivo.
     */
    private void salvarArquivo() {
        if (avaliacoesPorProfessor.isEmpty()) {
            return;
        } else {
            try {
                FileOutputStream fluxoArquivo = new FileOutputStream("src/main/resources/data/avaliacoes.ser");
                ObjectOutputStream fluxoObjetos = new ObjectOutputStream(fluxoArquivo);

                fluxoObjetos.writeObject(avaliacoesPorProfessor);

                fluxoArquivo.close();
                fluxoObjetos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}