package com.agendastudy; // Pacote raiz do projeto

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Ponto de entrada (Main) da aplicação JavaFX.
 * (MODIFICADO PARA TESTE ISOLADO DO FRONTEND)
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        
        // --- IMPORTANTE: Coloque aqui o FXML que você quer testar ---
        
        // String fxmlFile = "view/ProfessorProfile.fxml";
        String fxmlFile = "view/AllReviewsScreen.fxml";
        // String fxmlFile = "view/admin-login.fxml";
        
        // -----------------------------------------------------------

        System.out.println("Carregando FXML de teste: " + fxmlFile);
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        
        // O getClass().getResource() busca o arquivo dentro de 'src/main/resources/com/agendastudy/'
        URL fxmlUrl = getClass().getResource(fxmlFile);
        
        if (fxmlUrl == null) {
            System.err.println("ERRO CRÍTICO: Não foi possível encontrar o arquivo FXML.");
            System.err.println("Verifique o caminho: src/main/resources/com/agendastudy/" + fxmlFile);
            return;
        }
        
        fxmlLoader.setLocation(fxmlUrl);
        Parent root = fxmlLoader.load();
        
        Scene scene = new Scene(root);
        stage.setTitle("Teste de Tela Isolada (AgendaStudy)");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Método main que inicia a aplicação JavaFX.
     */
    public static void main(String[] args) {
        // O launch() inicia o toolkit do JavaFX e chama o método start()
        launch(args);
    }
}