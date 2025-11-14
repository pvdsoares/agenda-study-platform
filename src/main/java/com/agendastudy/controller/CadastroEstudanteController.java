package com.agendastudy.controller;

import java.util.ArrayList;
import java.util.List;

import com.agendastudy.DAO.EstudanteDAO;
import com.agendastudy.model.Estudante;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * controller responsável pela interface de cadastro de estudantes.
 * implementa as funcionalidades de validação, coleta de dados
 * 
 * @author VINÍCIUS ALVES RIBEIRO SILVA
 * @version 1.0
 */
public class CadastroEstudanteController {

    /** DAO para operações de persistência de estudantes */
    private EstudanteDAO estudanteDAO = new EstudanteDAO();

    // campos básicos
    @FXML private TextField fieldNome;
    @FXML private TextField fieldEmail;
    @FXML private PasswordField fieldSenha;

    // interesses
    @FXML private TextField fieldNovoInteresse;
    @FXML private ListView<String> listInteresses;

    /** lista temporária antes de salvar */
    private List<String> interesses = new ArrayList<>();

    /** inicialização automática do JavaFX */
    @FXML
    private void inicializar() {
        atualizarLista();
    }

    /** salva o estudante no sistema */
    @FXML
    private void handleSalvar() {
        try {

            if (!validarCamposBasicos()) {
                return;
            }

            Estudante estudante = criarEstudanteComDadosFormulario();

            estudanteDAO.salvar(estudante);

            mostrarAlerta("Sucesso", "Estudante cadastrado com sucesso!");
            limparCampos();

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao cadastrar estudante: " + e.getMessage());
        }
    }

    /** cria objeto Estudante com os dados do formulário */
    private Estudante criarEstudanteComDadosFormulario() {
        Estudante estudante = new Estudante(
            null,
            fieldNome.getText(),
            fieldEmail.getText(),
            fieldSenha.getText()
        );

        interesses.forEach(estudante::adicionarInteresse);

        return estudante;
    }

    @FXML
    private void handleAdicionarInteresse() {
        String interesse = fieldNovoInteresse.getText().trim();

        if (!interesse.isEmpty() && !interesses.contains(interesse)) {
            interesses.add(interesse);
            fieldNovoInteresse.clear();
            atualizarLista();
        }
    }

    @FXML
    private void handleRemoverInteresse() {
        String selecionado = listInteresses.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            interesses.remove(selecionado);
            atualizarLista();
        }
    }

    /** valida campos obrigatórios */
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

    /** atualiza lista na interface */
    private void atualizarLista() {
        listInteresses.getItems().setAll(interesses);
    }

    /** limpa tudo */
    @FXML
    private void handleCancelar() {
        limparCampos();
    }

    private void limparCampos() {
        fieldNome.clear();
        fieldEmail.clear();
        fieldSenha.clear();
        fieldNovoInteresse.clear();
        interesses.clear();
        atualizarLista();
    }

    /** alerta padrão */
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
