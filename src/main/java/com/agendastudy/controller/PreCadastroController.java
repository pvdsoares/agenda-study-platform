package com.agendastudy.controller;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;


/**
 * Controlador para a tela de pré-cadastro da aplicação AgendaStudy.
 * Responsável pela seleção do tipo de usuário (Estudante ou Professor)
 * antes do cadastro completo.
 * 
 * @author Paulo Vitor Dias Soares
 * @version 1.0
 * @since 2025
 */
public class PreCadastroController implements ScreenController {

    private MainApp mainApp;

    @FXML
    private RadioButton radioEstudante;
    
    @FXML
    private RadioButton radioProfessor;
    
    @FXML
    private ToggleGroup grupoTipo;

    /**
     * Define a referência para a aplicação principal.
     * Permite ao controlador acessar funcionalidades de navegação entre telas.
     *
     * @param mainApp Instância da aplicação principal
     */
    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Método chamado quando a tela de pré-cadastro é exibida.
     * Configura o estado inicial dos componentes, definindo "Professor"
     * como opção padrão selecionada.
     */
    @Override
    public void onScreenShown() {
        radioProfessor.setSelected(true); // Professor como padrão
    }

    /**
     * Manipula o evento de clique no botão "Continuar".
     * Obtém o tipo de usuário selecionado e navega para a tela de cadastro
     * correspondente, passando o tipo como parâmetro.
     */
    @FXML
    private void handleContinuar() {
        String tipoUsuario = getTipoUsuarioSelecionado();
        
        if (mainApp != null) {
            mainApp.setScreen("cadastro", tipoUsuario);
        }
    }

    /**
     * Manipula o evento de clique no botão "Login".
     * Navega para a tela de autenticação de usuários existentes.
     */
    @FXML
    private void handleLogin() {
        if (mainApp != null) {
            mainApp.setScreen("login");
        }
    }

    /**
     * Obtém o tipo de usuário selecionado nas opções de rádio.
     *
     * @return String representando o tipo de usuário: "ESTUDANTE" ou "PROFESSOR"
     */
    public String getTipoUsuarioSelecionado() {
        return radioEstudante.isSelected() ? "ESTUDANTE" : "PROFESSOR";
    }
}