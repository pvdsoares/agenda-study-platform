package com.agendastudy.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe principal da aplicação AgendaStudy.
 * Responsável pelo gerenciamento de telas e navegação entre elas.
 * Implementa o padrão MVC com controle centralizado de interfaces.
 * 
 * @author Paulo Vitor Dias Soares
 * @version 1.0
 * @since 2025
 */
public class MainApp extends Application {
    
    private Stage primaryStage;
    private Map<String, Object> controllers = new HashMap<>();
    private Map<String, Parent> screens = new HashMap<>();
    private Map<String, String> fxmlPaths = new HashMap<>();
    
    /**
     * Método principal de inicialização da aplicação JavaFX.
     * Configura o palco principal e carrega a tela inicial.
     *
     * @param primaryStage Palco principal da aplicação
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AgendaStudy");
        this.primaryStage.setResizable(false);
        
        configurarTelas();
        loadScreen("inicial");
        setScreen("inicial");
        
        primaryStage.show();
    }
    
    /**
     * Configura os caminhos FXML para todas as telas da aplicação.
     * Deve ser atualizado conforme novas telas são adicionadas.
     */
    private void configurarTelas() {
        fxmlPaths.put("inicial", "/com/agendastudy/view/TelaInicial.fxml");
        fxmlPaths.put("preCadastro", "/com/agendastudy/view/PreCadastro.fxml");
        fxmlPaths.put("login", "/com/agendastudy/view/LoginView.fxml");
        fxmlPaths.put("cadastroProfessor", "/com/agendastudy/view/CadastroProfessorView.fxml");
        fxmlPaths.put("cadastroEstudante", "/com/agendastudy/view/CadastroEstudanteView.fxml");
    }
    
    /**
     * Carrega uma tela na memória a partir do arquivo FXML.
     * Registra o controlador da tela para futura navegação.
     *
     * @param screenName Nome identificador da tela
     */
    public void loadScreen(String screenName) {
        try {
            String fxmlPath = fxmlPaths.get(screenName);
            if (fxmlPath == null) {
                throw new IllegalArgumentException("Tela não configurada: " + screenName);
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent screen = loader.load();
            
            Object controller = loader.getController();
            
            if (controller instanceof ScreenController) {
                ((ScreenController) controller).setMainApp(this);
            }
            
            screens.put(screenName, screen);
            controllers.put(screenName, controller);
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar tela: " + screenName);
            e.printStackTrace();
        }
    }
    
    /**
     * Exibe uma tela previamente carregada no palco principal.
     * Notifica o controlador que a tela está sendo exibida.
     *
     * @param screenName Nome identificador da tela a ser exibida
     */
    public void setScreen(String screenName) {
        if (!screens.containsKey(screenName)) {
            loadScreen(screenName);
        }
        
        Parent screen = screens.get(screenName);
        if (screen != null) {
            Scene scene = primaryStage.getScene();
            if (scene == null) {
                scene = new Scene(screen);
                primaryStage.setScene(scene);
            } else {
                scene.setRoot(screen);
            }
            
            Object controller = controllers.get(screenName);
            if (controller instanceof ScreenController) {
                ((ScreenController) controller).onScreenShown();
            }
            
            primaryStage.sizeToScene();
        }
    }
    
    /**
     * Exibe uma tela passando parâmetros para o controlador.
     *
     * @param screenName Nome identificador da tela
     * @param data Dados a serem passados para o controlador
     */
    public void setScreen(String screenName, Object data) {
        setScreen(screenName);
        // Implementar lógica de passagem de dados se necessário
    }
    
    /**
     * Obtém o controlador de uma tela específica.
     *
     * @param screenName Nome identificador da tela
     * @return Controlador da tela ou null se não encontrado
     */
    public Object getController(String screenName) {
        return controllers.get(screenName);
    }
    
    /**
     * Retorna o palco principal da aplicação.
     *
     * @return Instância do palco principal
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    /**
     * Método principal de inicialização do JavaFX.
     *
     * @param args Argumentos da linha de comando
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Pré-carrega todas as telas da aplicação.
     * Útil para melhor performance em aplicações com múltiplas telas.
     */
    public void preloadAllScreens() {
        for (String screenName : fxmlPaths.keySet()) {
            loadScreen(screenName);
        }
    }
    
    /**
     * Remove uma tela da memória.
     *
     * @param screenName Nome identificador da tela a ser removida
     */
    public void unloadScreen(String screenName) {
        screens.remove(screenName);
        controllers.remove(screenName);
    }
}