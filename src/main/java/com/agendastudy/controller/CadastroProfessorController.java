package com.agendastudy.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;
import com.agendastudy.DAO.ProfessorDAO;
import com.agendastudy.model.Professor;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;


/**
 * Controller (JavaFX) responsável pela interface de cadastro de professores.
 * Implementa as funcionalidades relacionadas a cadastro e validação de professores.
 *
 * @author Paulo Vitor Dias 
 * @version 3.0
 * @since 2025-11-13
 */
public class CadastroProfessorController {

    /** DAO para operações de persistência de professores */
    private ProfessorDAO professorDAO = new ProfessorDAO();
    
    // --- Campos FXML ---
    @FXML private TextField fieldNome;
    @FXML private TextField fieldEmail;
    @FXML private PasswordField fieldSenha;
    @FXML private TextField fieldTelefone;
    @FXML private TextArea fieldBiografia;

    @FXML private TextField fieldNovaDisciplina;
    @FXML private TextField fieldNovaQualificacao;
    @FXML private ListView<String> listDisciplinas;
    @FXML private ListView<String> listQualificacoes;

    /** Listas temporárias antes da persistência  */
    private List<String> disciplinas = new ArrayList<>();
    private List<String> qualificacoes = new ArrayList<>();

    /**
     * Método de inicialização de controller chamado automaticamente pelo JavaFX.
     * Configura o estado inicial da interface.
     */
    @FXML
    private void initialize() {
        atualizarListas();
    }

    /**
     * Manipulador do evento de clique no botão "Salvar".
     * Valida e persiste o novo professor.
     */
    @FXML
    private void handleSalvar(){
        try{
           
            // valida campos básicos com dados básicos 
            if(!validarCamposBasicos()){
                return;
            }

            //cria um professor com dados basicos
            Professor professor = criarProfessorComDadosFormulario();

            if(!professorDAO.validarQualificacoes(professor)){
                mostrarAlerta("Atenção", "adicione ao menos uma qualificação valida.");
                return;
            }

            //persiste o professor no sistema
            professorDAO.salvarProfessor(professor);
            mostrarAlerta("sucesso", "professor cadastrado com sucesso!");
            limparCampos();

        } catch(Exception e){
            mostrarAlerta("Erro", "Erro ao cadastrar professor: " + e.getMessage());
        }
    }

    /**
     * Cria um objeto Professor com os dados preenchidos no formulário.
     * @return O novo objeto Professor.
     */
    private Professor criarProfessorComDadosFormulario(){
        Professor professor = new Professor(null, fieldNome.getText(), fieldEmail.getText(), fieldSenha.getText());

        professor.setTelefone(fieldTelefone.getText());
        professor.setBiografia(fieldBiografia.getText());

        disciplinas.forEach(professor:: adicionarDisciplina);
        qualificacoes.forEach(professor::adicionarQualificacao);

        return professor;
    }


   /** * Manipulador do evento para adicionar uma nova disciplina à lista.
    * (Permite ao professor definir as disciplinas que leciona)
    */
   @FXML
    private void handleAdicionarDisciplina() {
        String disciplina = fieldNovaDisciplina.getText().trim();
        if (!disciplina.isEmpty() && !disciplinas.contains(disciplina)) {
            disciplinas.add(disciplina);
            fieldNovaDisciplina.clear();
            atualizarListas();
        }
    }

    /**
     * Manipulador do evento para adicionar uma nova qualificação à lista.
     * (Coleta as qualificações do professor para validação)
     */
    @FXML
    private void handleAdicionarQualificacao() {
        String qualificacao = fieldNovaQualificacao.getText().trim();
        if (!qualificacao.isEmpty() && !qualificacoes.contains(qualificacao)) {
            qualificacoes.add(qualificacao);
            fieldNovaQualificacao.clear();
            atualizarListas();
        }
    }

    /** * Manipulador do evento para remover uma disciplina selecionada.
     */
    @FXML
    private void handleRemoverDisciplina() {
        String selecionada = listDisciplinas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            disciplinas.remove(selecionada);
            atualizarListas();
        }
    }

    /** * Manipulador do evento para remover uma qualificação selecionada.
     */
    @FXML
    private void handleRemoverQualificacao() {
        String selecionada = listQualificacoes.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            qualificacoes.remove(selecionada);
            atualizarListas();
        }
    }

    /**
     * Manipulador do evento de clique no botão "Cancelar".
     * Limpa todos os campos do formulário.
     */
    @FXML
    private void handleCancelar() {
        limparCampos();
    }

    /**
     * Valida os campos básicos obrigatórios do formulário.
     * * @return true se campos obrigatórios são válidos, false caso contrário.
     */
    private boolean validarCamposBasicos() {
        if (fieldNome.getText().isEmpty()) {
            mostrarAlerta("Atenção", "O nome é obrigatório.");
            return false;
        }
        
        if (fieldEmail.getText().isEmpty()) {
            mostrarAlerta("Atenção", "O email é obrigatório.");
            return false;
        }
        
        if (fieldSenha.getText().isEmpty()) {
            mostrarAlerta("Atenção", "A senha é obrigatória.");
            return false;
        }
        
        if (professorDAO.emailExiste(fieldEmail.getText())) {
            mostrarAlerta("Atenção", "Este email já está cadastrado.");
            return false;
        }
        
        return true;
    }

    /** * Atualiza as listas visuais (ListViews) da interface com os dados atuais.
     */
    private void atualizarListas() {
        listDisciplinas.getItems().setAll(disciplinas);
        listQualificacoes.getItems().setAll(qualificacoes);
    }

    /** * Limpa todos os campos de entrada e listas do formulário.
     */
    private void limparCampos() {
        fieldNome.clear();
        fieldEmail.clear();
        fieldSenha.clear();
        fieldTelefone.clear();
        fieldBiografia.clear();
        fieldNovaDisciplina.clear();
        fieldNovaQualificacao.clear();
        disciplinas.clear();
        qualificacoes.clear();
        atualizarListas();
    }

    /**
     * Exibe um pop-up de alerta padrão.
     * @param titulo O título da janela de alerta.
     * @param mensagem A mensagem a ser exibida.
     */
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}