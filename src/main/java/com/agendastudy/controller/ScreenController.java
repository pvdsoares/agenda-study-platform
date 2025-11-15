package com.agendastudy.controller;

/**
 * Interface que define os métodos obrigatórios para todos os controladores de tela.
 * Perminte a integração com o sistema principal de navegação.
 * 
 * @author Paulo Vitor Dias Soares
 * @version 1.0
 * @since 2025
 */
public interface ScreenController {
    
    /**
     * Define a referência para a aplicação principal.
     * Permite ao controlador acessar funcionalidades de navegação.
     *
     * @param mainApp Instância da aplicação principal
     */
    void setMainApp(MainApp mainApp);
    
    /**
     * Método chamado quando a tela é exibida.
     * Pode ser usado para inicializações específicas da tela.
     */
    void onScreenShown();
}