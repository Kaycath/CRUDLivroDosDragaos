package com.Interface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principal que inicia a aplicação JavaFX.
 * Ela herda de 'Application', que é a classe base para qualquer app JavaFX.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Carrega o arquivo FXML que contém o desenho da interface gráfica
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();

        // 2. Configura a janela principal (Title e a Scene com o tamanho do FXML)
        primaryStage.setTitle("Livro dos Dragões - CRUD");
        primaryStage.setScene(new Scene(root, 900, 650));

        // 3. Impede que o usuário redimensione a tela, mantendo o design intacto
        primaryStage.setResizable(false);

        // 4. Exibe a janela na tela do computador
        primaryStage.show();
    }

    public static void main(String[] args) {
        // O método launch chama internamente o método start() acima
        launch(args);
    }
}