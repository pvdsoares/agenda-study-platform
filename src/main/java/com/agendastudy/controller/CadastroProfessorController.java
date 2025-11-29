package com.agendastudy.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;
import com.agendastudy.DAO.ProfessorDAO;
import com.agendastudy.model.Professor;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import com.agendastudy.service.ProfessorService;

/**
 * Controlador para a interface de cadastro de professores.
 * Implementa a lógica de interface para cadastro, validação e persistência de
 * dados de professores.
 * 
 * @author Paulo Vitor Dias Soares
 * @version 2.0
 * @since 2025
 */
public class CadastroProfessorController implements ScreenController {

    private final ProfessorService professorService = new ProfessorService(new ProfessorDAO());
    private MainApp mainApp;

    @FXML
    private TextField fieldNome;
    @FXML
    private TextField fieldEmail;
    @FXML
    private PasswordField fieldSenha;
    @FXML
    private TextField fieldTelefone;
    @FXML
    private TextArea fieldBiografia;
    @FXML
    private TextField fieldNovaDisciplina;
    @FXML
    private TextField fieldNovaQualificacao;
    @FXML
    private ListView<String> listDisciplinas;
    @FXML
    private ListView<String> listQualificacoes;
    @FXML
    private Label labelFotoSelecionada;

    private byte[] fotoTemporaria;
    private String tipoImagemTemporaria;
    private List<String> disciplinas = new ArrayList<>();
    private List<String> qualificacoes = new ArrayList<>();

    /**
     * Define a referência para a aplicação principal para controle de navegação.
     *
     * @param mainApp Instância da aplicação principal
     */
    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Método chamado quando a tela é exibida.
     * Realiza a limpeza dos campos do formulário.
     */
    @Override
    public void onScreenShown() {
        limparCampos();
    }

    /**
     * Inicializa o controlador.
     * Configura o estado inicial das listas de disciplinas e qualificações.
     */
    @FXML
    private void initialize() {
        atualizarListas();
    }

    /**
     * Manipula o evento de salvamento do professor.
     * Valida os dados, persiste o professor e navega para a tela de login.
     */
    @FXML
    private void handleSalvar() {
        // 1. Validação simples de interface (se campos básicos estão vazios)
        if (!validarCamposBasicos()) {
            return;
        }

        try {
            // 2. Cria o objeto Professor (Model)
            Professor professor = criarProfessorComDadosFormulario();

            // 3. Chama o Service. Ele fará a validação de email, qualificações, etc.
            professorService.cadastrarProfessor(professor);

            // 4. Sucesso
            mostrarAlerta("Sucesso", "Professor cadastrado com sucesso!");

            if (mainApp != null) {
                mainApp.setScreen("login");
            }

            limparCampos();

            // O Service lança exceções específicas que o Controller captura e exibe
        } catch (IllegalArgumentException e) {
            // Captura erros de regra de negócio, como e-mail duplicado
            mostrarAlerta("Erro de Cadastro", e.getMessage());
        } catch (IllegalStateException e) {
            // Captura erros de estado, como senha fraca ou dados essenciais
            mostrarAlerta("Dados Incompletos", e.getMessage());
        } catch (Exception e) {
            // Captura erros inesperados (ex: IO, problemas de persistência)
            mostrarAlerta("Erro Inesperado", "Erro ao cadastrar professor: " + e.getMessage());
        }
    }

    /**
     * Manipula o evento de voltar para a tela anterior.
     * Navega para a tela de pré-cadastro.
     */
    @FXML
    private void handleVoltar() {
        if (mainApp != null) {
            mainApp.setScreen("preCadastro");
        }
    }

    /**
     * Manipula a adição de uma nova disciplina à lista.
     * Adiciona a disciplina se não estiver vazia e não for duplicada.
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
     * Manipula a adição de uma nova qualificação à lista.
     * Adiciona a qualificação se não estiver vazia e não for duplicada.
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

    /**
     * Manipula a remoção de uma disciplina selecionada da lista.
     */
    @FXML
    private void handleRemoverDisciplina() {
        String selecionada = listDisciplinas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            disciplinas.remove(selecionada);
            atualizarListas();
        }
    }

    /**
     * Manipula a remoção de uma qualificação selecionada da lista.
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
     * Manipula o upload de foto de perfil.
     * Abre um seletor de arquivos e carrega a imagem selecionada.
     */
    @FXML
    private void handleUploadFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Foto de Perfil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("Todos os arquivos", "*.*"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                fotoTemporaria = Files.readAllBytes(file.toPath());
                String nomeArquivo = file.getName();
                tipoImagemTemporaria = nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1).toLowerCase();
                labelFotoSelecionada.setText("Foto selecionada: " + nomeArquivo);
                labelFotoSelecionada.setStyle("-fx-text-fill: green;");
            } catch (IOException e) {
                mostrarAlerta("Erro", "Erro ao carregar imagem: " + e.getMessage());
                limparFoto();
            }
        }
    }

    /**
     * Manipula a remoção da foto de perfil selecionada.
     */
    @FXML
    private void handleRemoverFoto() {
        limparFoto();
    }

    /**
     * Manipula o evento de cancelamento do cadastro.
     * Navega de volta para a tela anterior.
     */
    @FXML
    private void handleCancelar() {
        handleVoltar();
    }

    /**
     * Cria um objeto Professor com os dados preenchidos no formulário.
     *
     * @return Objeto Professor com os dados do formulário
     */
    private Professor criarProfessorComDadosFormulario() {
        Professor professor = new Professor(null, fieldNome.getText(), fieldEmail.getText(), fieldSenha.getText());
        professor.setTelefone(fieldTelefone.getText());
        professor.setBiografia(fieldBiografia.getText());
        professor.setDisciplinas(new ArrayList<>(disciplinas));
        professor.setQualificacoes(new ArrayList<>(qualificacoes));

        if (fotoTemporaria != null) {
            professor.setFotoPerfil(fotoTemporaria, tipoImagemTemporaria);
        }
        return professor;
    }

    /**
     * Valida os campos básicos obrigatórios do formulário.
     *
     * @return true se todos os campos obrigatórios são válidos, false caso
     *         contrário
     */
    private boolean validarCamposBasicos() {
        if (fieldNome.getText().trim().isEmpty()) {
            mostrarAlerta("Atenção", "O nome é obrigatório.");
            return false;
        }

        if (fieldEmail.getText().trim().isEmpty()) {
            mostrarAlerta("Atenção", "O email é obrigatório.");
            return false;
        }

        if (fieldSenha.getText().trim().isEmpty()) {
            mostrarAlerta("Atenção", "A senha é obrigatória.");
            return false;
        }

        return true;
    }

    /**
     * Atualiza as listas visuais de disciplinas e qualificações.
     */
    private void atualizarListas() {
        listDisciplinas.getItems().setAll(disciplinas);
        listQualificacoes.getItems().setAll(qualificacoes);
    }

    /**
     * Limpa todos os campos do formulário e listas.
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
        limparFoto();
        atualizarListas();
    }

    /**
     * Limpa os dados da foto de perfil.
     */
    private void limparFoto() {
        fotoTemporaria = null;
        tipoImagemTemporaria = null;
        labelFotoSelecionada.setText("Nenhuma foto selecionada");
        labelFotoSelecionada.setStyle("-fx-text-fill: gray;");
    }

    /**
     * Exibe um alerta na interface.
     *
     * @param titulo   Título do alerta
     * @param mensagem Mensagem do alerta
     */
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}