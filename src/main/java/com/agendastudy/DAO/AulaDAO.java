package com.agendastudy.DAO;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import com.agendastudy.model.Aula;

public class AulaDAO {
    private static Map<String, Aula> aulas = new HashMap<>();
    private static int proximoId = 1; //contagem de id

    // IMPLEMENTAR DEMAIS MÉTODOS RELACIONADOS AQUI

    public void cancelarAula(String idAula){
        Aula aula = aulas.get(idAula);

        if (aula == null){
            throw new NoSuchElementException("Aula de ID: " + idAula + "não encontrada!");
        }

        if (aula.isCancelada()){
            throw new IllegalStateException("Aula de ID: " + idAula + "já está cancelada!");
        }
        aula.cancelar();
        System.out.println("Aula: " + idAula + " cancelada com sucesso.");
    }
    
}