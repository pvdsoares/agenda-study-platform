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

public class CadastroProfessorController {

    private ProfessorDAO ProfessorDAO = new ProfessorDAO();
    
    // Campos básicos do professor
    @FXML private TextField fieldNome;
    @FXML private TextField fieldEmail;
    @FXML private PasswordField fieldSenha;
    @FXML private TextField fieldTelefone;
    @FXML private TextArea fieldBiografia;

    // Campos para disciplinas e qualificações
    @FXML private TextField fieldNovaDisciplina;
    @FXML private TextField fieldNovaQualificacao;
    @FXML private ListView<String> listDisciplinas;
    @FXML private ListView<String> listQualificacoes;

    private List<String> disciplinas = new ArrayList<>();
    private List<String> qualificacoes = new ArrayList<>();

    


}
