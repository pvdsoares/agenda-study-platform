package com.agendastudy.DAO;

import com.agendastudy.model.Professor;
import com.agendastudy.model.Usuario;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO extends UsuarioDAO  {
    
    
    /**
     * Salva um professor no sistema
     * @param professor Professor a ser salvo
     */
    public void salvarProfessor(Professor professor) {
        salvar(professor);
        System.out.println("Professor salvo: " + professor.getNome());
    }

    /**
     * 
     * @param id
     * @return
     */ 
    public Professor buscarPorId(String id) {
        Usuario usuario = usuarios.get(id);
        return (usuario instanceof Professor) ? (Professor) usuario : null;
    }

    
    /**
     * 
     * @param id
     * @param foto
     * @param tipoImagem
     */

    public void atualizarFotoPerfil(String id, byte[] foto, String tipoImagem) {
        Professor professor = buscarPorId(id);
        if (professor != null) {
            professor.setFotoPerfil(foto, tipoImagem);
            System.out.println("Foto do professor " + professor.getNome() + " atualizada");
        }
    }

     
    /**
     * 
     * @param professor
     * @return
     */
    public boolean validarQualificacoes(Professor professor) {
        if (professor.getQualificacoes() == null || professor.getQualificacoes().isEmpty()) {
            return false;
        }
        
        for (String qualificacao : professor.getQualificacoes()) {
            if (qualificacao != null && !qualificacao.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param professor
     * @return
     */
    public boolean podeSerVerificado(Professor professor) {
        return validarQualificacoes(professor) && 
               professor.getDisciplinas() != null && 
               !professor.getDisciplinas().isEmpty() &&
               professor.temFoto();
    }

    /**
     * solicita o cancelamento de uma aula previamente agendada (trata-se de um protótipo, ainda falta o método de agendar)
     * @param idAula ID da aula a ser cancelada
     * @param aulaDAO DAO de aula para cancelar a aula
     */
    public void solicitarCancelamento(String idAula, AulaDAO aulaDAO){
        try{
            aulaDAO.cancelarAula(idAula);
            System.out.println("Cancelamento solicitado.");
        } catch (Exception e){
            System.err.println("Erro ao cancelar aula: " + e.getMessage());
        }
    }
}
