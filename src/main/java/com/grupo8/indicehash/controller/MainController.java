package com.grupo8.indicehash.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controlador principal da interface gráfica.
 * Gerencia ações do usuário e exibe dados das estruturas.
 */
public class MainController {
    @FXML private TextField filePathField;
    @FXML private TextField pageSizeField;
    @FXML private ListView<String> firstPageList;
    @FXML private ListView<String> lastPageList;
    @FXML private Label totalPagesLabel;
    @FXML private ListView<String> bucketList;
    @FXML private Label collisionLabel;
    @FXML private Label overflowLabel;
    @FXML private TextField searchField;
    @FXML private Label searchResultLabel;
    @FXML private Label statusLabel;

    /**
     * Ação ao clicar em "Carregar". Valida entradas, lê arquivo e exibe dados reais.
     */
    @FXML
    protected void onLoadFile() {
        String filePath = filePathField.getText().trim();
        String pageSizeStr = pageSizeField.getText().trim();
        int pageSize;
        if (filePath.isEmpty()) {
            statusLabel.setText("Status: Informe o caminho do arquivo.");
            return;
        }
        try {
            pageSize = Integer.parseInt(pageSizeStr);
            if (pageSize <= 0) {
                statusLabel.setText("Status: Tamanho da página deve ser > 0.");
                return;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Status: Tamanho da página inválido.");
            return;
        }
        try {
            com.grupo8.indicehash.classes.GerenciadorArquivo gerenciador = new com.grupo8.indicehash.classes.GerenciadorArquivo(pageSize);
            gerenciador.carregarArquivo(filePath);
            statusLabel.setText("Status: Arquivo carregado com sucesso.");
            totalPagesLabel.setText("Total de páginas: " + gerenciador.getTotalPaginas());
            // CA02: Exibir total de palavras carregadas
            statusLabel.setText(statusLabel.getText() + " | Total de palavras: " + gerenciador.getTotalPalavras());
            // Exibir primeiras 5 da primeira página
            var primeira = gerenciador.getPrimeiraPagina();
            if (primeira != null) {
                var registros = primeira.getRegistros();
                firstPageList.getItems().setAll(registros.stream().limit(5).toList());
            } else {
                firstPageList.getItems().clear();
            }
            // Exibir últimas 5 da última página
            var ultima = gerenciador.getUltimaPagina();
            if (ultima != null) {
                var registros = ultima.getRegistros();
                lastPageList.getItems().setAll(registros.stream().limit(5).toList());
            } else {
                lastPageList.getItems().clear();
            }
            // Buckets, colisões, overflow: implementar depois
            bucketList.getItems().setAll("(Implementar visualização de buckets)");
            collisionLabel.setText("Colisões: (implementar)");
            overflowLabel.setText("Overflow: (implementar)");
        } catch (Exception e) {
            statusLabel.setText("Erro ao carregar arquivo: " + e.getMessage());
            totalPagesLabel.setText("Total de páginas: -");
            firstPageList.getItems().clear();
            lastPageList.getItems().clear();
            bucketList.getItems().clear();
            collisionLabel.setText("Colisões: -");
            overflowLabel.setText("Overflow: -");
        }
    }

    /**
     * Ação ao clicar em "Buscar". Destaca bucket e página acessados.
     */
    @FXML
    protected void onSearch() {
        String key = searchField.getText().trim();
        if (key.isEmpty()) {
            searchResultLabel.setText("Resultado: Informe uma chave.");
            return;
        }
        // TODO: Integrar busca real
        searchResultLabel.setText("Resultado: Registro encontrado na página 1, bucket 0 (mock)");
    }
}
