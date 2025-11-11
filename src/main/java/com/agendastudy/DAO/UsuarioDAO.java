package com.agendastudy.DAO;

import com.agendastudy.model.Usuario;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private static Map<String, Usuario> usuarios = new HashMap<>();
    private static int proximoId = 1;
}
