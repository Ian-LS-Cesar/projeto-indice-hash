package com.grupo8.indicehash.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principal da interface gráfica JavaFX.
 * Responsável por carregar o FXML e exibir a janela principal.
 */
public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Carrega o layout principal da interface a partir do FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grupo8/indicehash/view/main-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Índice Hash Estático - Visualização");
        stage.setScene(scene);
        stage.show();
    }
}
