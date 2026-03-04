package com.grupo8.indicehash.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controlador inicial para testes da interface.
 * Futuramente será expandido para controlar as ações do usuário.
 */
public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
