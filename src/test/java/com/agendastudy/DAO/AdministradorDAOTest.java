package com.agendastudy.DAO;

import com.agendastudy.model.Administrador;
import com.agendastudy.model.Estudante;
import com.agendastudy.model.Professor;
import com.agendastudy.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// ====================================================================
// SIMULAÇÃO DE MODELOS PARA TESTE (Necessário para a compilação/teste)
// Estas classes devem ser movidas para um pacote de testes ou mockadas
// se o teste for executado em um ambiente completo.
// ====================================================================

// Simplificação da classe base Usuario
class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;

    public Usuario(String id, String nome, String email, String senha, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
    }
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getTelefone() { return telefone; }
}

class Administrador extends Usuario {
    public Administrador(String id, String nome, String email, String senha) {
        super(id, nome, email, senha, null);
    }
}
class Professor extends Usuario {
    public Professor(String id, String nome, String email, String senha, String telefone) {
        super(id, nome, email, senha, telefone);
    }
}
class Estudante extends Usuario {
    public Estudante(String id, String nome, String email, String senha, String telefone) {
        super(id, nome, email, senha, telefone);
    }
}


public class AdministradorDAOTest {

    private AdministradorDAO administradorDAO;
    private final String MAP_FIELD_NAME = "usuarios"; // Nome do mapa de dados na classe pai
    
    // Usuários de teste
    private final Administrador adminCorreto = new Administrador("A001", "Admin Teste", "admin@test.com", "123");
    private final Professor professor1 = new Professor("P001", "Professora Maria", "maria@prof.com", "senhaP", "987");
    private final Professor professor2 = new Professor("P002", "Professor João", "joao@prof.com", "senhaP2", "654");
    private final Estudante estudante1 = new Estudante("E001", "Aluno Pedro", "pedro@aluno.com", "senhaE", null);
    private final Usuario usuarioComum = new Usuario("U001", "Usuario Comum", "comum@user.com", "789", null);


    @BeforeEach
    void setUp() throws Exception {
        administradorDAO = new AdministradorDAO();
        
        // **SETUP DE DADOS VIA REFLECTION**
        // Simula a injeção de dados no mapa 'usuarios' da classe pai (UsuarioDAO)
        try {
            // Acessa o mapa 'usuarios' que é herdado de UsuarioDAO
            Field usuariosField = AdministradorDAO.class.getSuperclass().getDeclaredField(MAP_FIELD_NAME);
            usuariosField.setAccessible(true);
            
            // Obtém e limpa o mapa
            Map<String, Usuario> usuarios = (Map<String, Usuario>) usuariosField.get(administradorDAO);
            usuarios.clear();

            // Popula o mapa com dados de teste
            usuarios.put(adminCorreto.getId(), adminCorreto);
            usuarios.put(professor1.getId(), professor1);
            usuarios.put(professor2.getId(), professor2);
            usuarios.put(estudante1.getId(), estudante1);
            usuarios.put(usuarioComum.getId(), usuarioComum);

        } catch (NoSuchFieldException e) {
            // Esta exceção é esperada se 'usuarios' não for um campo direto ou não for acessível.
            System.err.println("ERRO DE SETUP: Não foi possível acessar o campo 'usuarios' via Reflection. Verifique a visibilidade em UsuarioDAO.");
        }
    }
    
    // Método auxiliar para obter o mapa de usuários via Reflection
    private Map<String, Usuario> getUsuariosMap() throws Exception {
        Field usuariosField = AdministradorDAO.class.getSuperclass().getDeclaredField(MAP_FIELD_NAME);
        usuariosField.setAccessible(true);
        return (Map<String, Usuario>) usuariosField.get(administradorDAO);
    }
    
    // ====================================================================
    // TESTES DE CRUD BÁSICO (Salvar)
    // ====================================================================

    @Test
    void testSalvarAdministradorDeveAdicionarAoMapa() throws Exception {
        Administrador novoAdmin = new Administrador("A002", "Admin Novo", "novo@admin.com", "abc");
        
        // Act
        administradorDAO.salvar(novoAdmin);
        
        // Assert: Verifica se o usuário foi adicionado ao mapa
        Map<String, Usuario> usuarios = getUsuariosMap();
        assertTrue(usuarios.containsKey("A002"));
        assertEquals("Admin Novo", usuarios.get("A002").getNome());
    }

    // ====================================================================
    // TESTES DE AUTENTICAÇÃO
    // ====================================================================

    @Test
    void testAutenticarAdminComCredenciaisCorretasDeveRetornarAdmin() {
        // Act
        Administrador adminAutenticado = administradorDAO.autenticarAdmin(adminCorreto.getEmail(), adminCorreto.getSenha());
        
        // Assert
        assertNotNull(adminAutenticado);
        assertEquals(adminCorreto.getNome(), adminAutenticado.getNome());
        assertTrue(adminAutenticado instanceof Administrador);
    }

    @Test
    void testAutenticarAdminComSenhaIncorretaDeveRetornarNull() {
        // Act
        Administrador adminAutenticado = administradorDAO.autenticarAdmin(adminCorreto.getEmail(), "senha_errada");
        
        // Assert
        assertNull(adminAutenticado);
    }

    @Test
    void testAutenticarAdminComUsuarioNaoAdminDeveRetornarNull() {
        // Act
        Administrador adminAutenticado = administradorDAO.autenticarAdmin(professor1.getEmail(), professor1.getSenha());
        
        // Assert (Professor tem email/senha válidos, mas não é Administrador)
        assertNull(adminAutenticado);
    }
    
    // ====================================================================
    // TESTES DE FILTRAGEM DE USUÁRIOS
    // ====================================================================

    @Test
    void testListarTodosProfessoresDeveRetornarApenasProfessores() {
        // Act
        List<Professor> professores = administradorDAO.listarTodosProfessores();
        
        // Assert
        assertEquals(2, professores.size());
        assertTrue(professores.stream().allMatch(p -> p instanceof Professor));
        assertFalse(professores.stream().anyMatch(p -> p instanceof Estudante));
    }

    @Test
    void testListarTodosEstudantesDeveRetornarApenasEstudantes() {
        // Act
        List<Estudante> estudantes = administradorDAO.listarTodosEstudantes();
        
        // Assert
        assertEquals(1, estudantes.size());
        assertTrue(estudantes.stream().allMatch(e -> e instanceof Estudante));
        assertFalse(estudantes.stream().anyMatch(e -> e instanceof Professor));
    }
    
    // ====================================================================
    // TESTES DE RELATÓRIO DE CONTAGEM
    // ====================================================================

    @Test
    void testGerarRelatorioContagemDeveContarCorretamente() {
        // Arrange: 2 Professores e 1 Estudante injetados no setup
        int esperadoEstudantes = 1;
        int esperadoProfessores = 2;
        
        // Act
        Map<String, Integer> relatorio = administradorDAO.gerarRelatorioContagem();
        
        // Assert
        assertEquals(esperadoEstudantes, relatorio.get("Total Estudantes (Alunos)"));
        assertEquals(esperadoProfessores, relatorio.get("Total Professores (Tutores)"));
        assertEquals(2, relatorio.size()); // Apenas duas chaves
    }
}
