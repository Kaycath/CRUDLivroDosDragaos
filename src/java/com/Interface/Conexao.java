package com.Interface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class Conexao {
    // Constantes ajudam a padronizar e facilitar alterações futuras
    // Constantes ajudam a padronizar e facilitar alterações futuras
    private static final String URL = "jdbc:postgresql://localhost:5432/LivroDosDragoes";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "postgres";

    public static Connection conectaBD() throws SQLException {
        // Retorna a conexão. O tratamento de erro será feito por quem chamar este metodo (o DAO).
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
