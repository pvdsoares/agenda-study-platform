import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * Controlador para a tela inicial da aplicação AgendaStudy.
 * Responsável pela navegação inicial entre login e cadastro de usuários.
 * 
 * @author Paulo Vitor Dias Soares
 * @version 1.0
 * @since 2025
 */
public class TelaInicialController implements ScreenController {

    private MainApp mainApp;

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
     * Método chamado quando a tela inicial é exibida.
     * Pode ser utilizado para inicializações específicas ou logs de debug.
     */
    @Override
    public void onScreenShown() {
        System.out.println("Tela inicial carregada");
    }

    /**
     * Manipula o evento de clique no botão de login.
     * Navega para a tela de autenticação de usuário.
     */
    @FXML
    private void handleLogin() {
        if (mainApp != null) {
            mainApp.setScreen("login");
        }
    }

    /**
     * Manipula o evento de clique no botão de cadastro.
     * Navega para a tela de pré-cadastro para seleção do tipo de usuário.
     */
    @FXML
    private void handleCadastro() {
        if (mainApp != null) {
            mainApp.setScreen("preCadastro");
        }
    }
}