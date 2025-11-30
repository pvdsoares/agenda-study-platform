package com.agendastudy.service;

import com.agendastudy.model.Aula;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.Professor;
import com.agendastudy.model.StatusAula;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

public class RelatoriodeRendimentoteste {

    @Test
    public void testCalcularTotalAulas() {

        Professor professor = new Professor("P1", "Carlos", "carlos@email.com", "123");

        Map<Professor, List<Aula>> mapa = new HashMap<>();
        List<Aula> aulas = criarAulasMock(professor);

        mapa.put(professor, aulas);

        RelatoriodeRendimento relatorio = new RelatoriodeRendimento(mapa);

        int total = relatorio.calcularTotalAulas(
                professor,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().plusDays(1)
        );

        assertEquals(3, total);
    }

    @Test
    public void testTaxaCancelamentoAluno() {
        Professor professor = new Professor("P1", "Carlos", "carlos@email.com", "123");

        Map<Professor, List<Aula>> mapa = new HashMap<>();
        List<Aula> aulas = criarAulasMock(professor);

        mapa.put(professor, aulas);

        RelatoriodeRendimento relatorio = new RelatoriodeRendimento(mapa);

        double taxa = relatorio.calcularTaxaCancelamentoAluno(professor);

        assertEquals(20.0, taxa); // 1 em 5 aulas = 20%
    }

    @Test
    public void testCancelamentosPorAluno() {
        Professor professor = new Professor("P1", "Carlos", "carlos@email.com", "123");

        Map<Professor, List<Aula>> mapa = new HashMap<>();
        List<Aula> aulas = criarAulasMock(professor);

        mapa.put(professor, aulas);

        RelatoriodeRendimento relatorio = new RelatoriodeRendimento(mapa);

        Map<String, Integer> cancelamentos = relatorio.calcularCancelamentosPorAluno(professor);

        assertTrue(cancelamentos.containsKey("João"));
        assertEquals(1, cancelamentos.get("João"));
    }

    // ---------------------------------------------------
    // MOCK REAL COMPATÍVEL COM SUA CLASSE AULA
    // ---------------------------------------------------
    private List<Aula> criarAulasMock(Professor professor) {

        Estudante ana = new Estudante("E1", "Ana", "ana@email.com", "123");
        Estudante pedro = new Estudante("E2", "Pedro", "pedro@email.com", "123");
        Estudante joao = new Estudante("E3", "João", "joao@email.com", "123");
        Estudante bruno = new Estudante("E4", "Bruno", "bruno@email.com", "123");

        List<Aula> aulas = new ArrayList<>();

        // 3 AULAS CONCLUÍDAS
        aulas.add(novaAula("A1", professor, ana, StatusAula.CONCLUIDA, -5));
        aulas.add(novaAula("A2", professor, ana, StatusAula.CONCLUIDA, -4));
        aulas.add(novaAula("A3", professor, pedro, StatusAula.CONCLUIDA, -3));

        // CANCELADA PELO ALUNO
        aulas.add(novaAula("A4", professor, joao, StatusAula.CANCELADA_ALUNO, -2));

        // CANCELADA PELO PROFESSOR
        aulas.add(novaAula("A5", professor, bruno, StatusAula.CANCELADA_PROFESSOR, -1));

        return aulas;
    }

    private Aula novaAula(
            String id,
            Professor prof,
            Estudante est,
            StatusAula status,
            int dias
    ) {
        Aula a = new Aula(
                id,
                "Título " + id,
                "Descrição " + id,
                prof,
                est,
                LocalDateTime.now().plusDays(dias),
                60
        );
        a.setStatus(status);
        return a;
    }
}
