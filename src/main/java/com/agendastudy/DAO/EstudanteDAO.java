package com.agendastudy.DAO;

public class EstudanteDAO {
   
    // IMPLEMENTAÇÃO DE DEMAIS MÉTODOS AQUI

    public void solicitarCancelamento(String idAula, AulaDAO aulaDAO){
        try{
            aulaDAO.cancelarAula(idAula);
            System.out.println("Cancelamento solicitado.");
        } catch (Exception e){
            System.err.println("Erro ao cancelar aula: " + e.getMessage());
        }
    }
}