package com.agendastudy.controller;

import java.util.ArrayList;
import java.util.List;

import com.agendastudy.DAO.EstudanteDAO;
import com.agendastudy.model.Estudante;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * P
 * Controller (JavaFX) responsável pela interface de cadastro de estudantes.
 * Implementa as funcionalidades de validação, coleta de dados
 * e interação com o EstudanteDAO.
 *
 * @author VINÍCIUS ALVES RIBEIRO SILVA
 * @version 1.0
 * @since 2025-11-13
 */
public class CadastroEstudanteController {

    /** DAO para operações de persistência de estudantes */
    // ATENÇÃO: Esta linha causará erro pois EstudanteDAO agora requer AvaliacaoDAO
    // no construtor.
    private EstudanteDAO estudanteDAO = new EstudanteDAO();

    // --- Campos FXML ---
    @FXML
    private TextField fieldNome;
    @FXML
    private TextField fieldEmail;
    @FXML
    private PasswordField fieldSenha;
    @FXML
    private TextField fieldNovoInteresse;
    @FXML
    private ListView<String> listInteresses;

    /** lista temporária de interesses antes de salvar */
    private List<String> interesses = new ArrayList<>();

    /**
     * * Método de inicialização automática do JavaFX.
     * Configura o estado inicial da interface.
     */
    @FXML
    private void initialize() {
        atualizarLista();
    }

    /**
     * * Manipulador do evento de clique no botão "Salvar".
     * Valida os campos e salva o novo estudante.
     */
    @FXML
    private void handleSalvar() {
        try {
            if (estudanteDAO == null) {
                mostrarAlerta("Erro Crítico", "EstudanteDAO não foi inicializado.");
                return;
            }

            if (!validarCamposBasicos()) {
                return;
            }

            Estudante estudante = criarEstudanteComDadosFormulario();

            estudanteDAO.salvar(estudante); // Usa o salvar() herdado de UsuarioDAO

            mostrarAlerta("Sucesso", "Estudante cadastrado com sucesso!");
            limparCampos();

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao cadastrar estudante: " + e.getMessage());
        }
    }

    /**
     * * Cria um objeto Estudante com os dados preenchidos no formulário.
     * 
     * @return O novo objeto Estudante.
     */
    private Estudante criarEstudanteComDadosFormulario() {
        Estudante estudante = new Estudante(
                null, // ID será gerado pelo DAO
                fieldNome.getText(),
                fieldEmail.getText(),
                fieldSenha.getText());

        interesses.forEach(estudante::adicionarInteresse);

        return estudante;
    }

    /**
     * Manipulador do evento para adicionar um novo interesse à lista.
     */
    @FXML
    private void handleAdicionarInteresse() {
        String interesse = fieldNovoInteresse.getText().trim();

        if (!interesse.isEmpty() && !interesses.contains(interesse)) {
            interesses.add(interesse);
            fieldNovoInteresse.clear();
            atualizarLista();
        }
    }

    /**
     * Manipulador do evento para remover um interesse selecionado da lista.
     */
    @FXML
    private void handleRemoverInteresse() {
        String selecionado = listInteresses.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            interesses.remove(selecionado);
            atualizarLista();
        }
    }

    /**
     * * Valida os campos obrigatórios do formulário.
     * 
     * @return true se os campos básicos são válidos, false caso contrário.
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

        if (estudanteDAO.emailExiste(fieldEmail.getText())) {
            mostrarAlerta("Atenção", "Este email já está cadastrado.");
            return false;
        }

        return true;
    }

    /**
     * * Atualiza a ListView de interesses na interface.
     */
    private void atualizarLista() {
        listInteresses.getItems().setAll(interesses);
    }

    /**
     * * Manipulador do evento de clique no botão "Cancelar".
     * Limpa todos os campos.
     */
    @FXML
    private void handleCancelar() {
        limparCampos();
    }

    /**
     * Limpa todos os campos de entrada e a lista de interesses.
     */
    private void limparCampos() {
        fieldNome.clear();
        fieldEmail.clear();
        fieldSenha.clear();
        fieldNovoInteresse.clear();
        interesses.clear();
        atualizarLista();
    }

    /**
     * * Exibe um pop-up de alerta padrão.
     * 
     * @param titulo   O título da janela de alerta.
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