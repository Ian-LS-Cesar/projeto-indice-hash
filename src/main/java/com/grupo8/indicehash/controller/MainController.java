package com.grupo8.indicehash.controller;

import com.grupo8.indicehash.classes.GerenciadorArquivo;
import com.grupo8.indicehash.classes.ResultadoBusca;
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

    private GerenciadorArquivo gerenciador;

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
            gerenciador = new GerenciadorArquivo(pageSize);
            gerenciador.carregarArquivo(filePath);

            statusLabel.setText("Status: Arquivo carregado com sucesso. | Total de palavras: " + gerenciador.getTotalPalavras());
            totalPagesLabel.setText("Total de páginas: " + gerenciador.getTotalPaginas());

            // Exibir primeiras 5 da primeira página
            var primeira = gerenciador.getPrimeiraPagina();
            if (primeira != null) {
                firstPageList.getItems().setAll(primeira.getRegistros().stream().limit(5).toList());
            } else {
                firstPageList.getItems().clear();
            }

            // Exibir últimas 5 da última página
            var ultima = gerenciador.getUltimaPagina();
            if (ultima != null) {
                lastPageList.getItems().setAll(ultima.getRegistros().stream().limit(5).toList());
            } else {
                lastPageList.getItems().clear();
            }

            // Exibir colisões e overflows reais
            int colisoes = gerenciador.buckets.qColisoes;
            int overflows = gerenciador.buckets.qOverFlow;
            int totalBuckets = gerenciador.buckets.qBuckets;
            int totalPalavras = gerenciador.getTotalPalavras();

            double taxaColisao = totalPalavras > 0 ? (colisoes * 100.0 / totalPalavras) : 0;
            double taxaOverflow = totalPalavras > 0 ? (overflows * 100.0 / totalPalavras) : 0;

            collisionLabel.setText(String.format("Colisões: %d (%.1f%%)", colisoes, taxaColisao));
            overflowLabel.setText(String.format("Overflow: %d (%.1f%%)", overflows, taxaOverflow));

            // Exibir resumo dos buckets
            bucketList.getItems().clear();
            bucketList.getItems().add("Total de buckets: " + totalBuckets);
            bucketList.getItems().add("Chaves por bucket: " + gerenciador.buckets.qChaveValor);

        } catch (Exception e) {
            statusLabel.setText("Erro ao carregar arquivo: " + e.getMessage());
            totalPagesLabel.setText("Total de páginas: -");
            firstPageList.getItems().clear();
            lastPageList.getItems().clear();
            bucketList.getItems().clear();
            collisionLabel.setText("Colisões: -");
            overflowLabel.setText("Overflow: -");
            gerenciador = null;
        }
    }

    /**
     * Ação ao clicar em "Buscar". Usa busca por índice hash real.
     */
    @FXML
    protected void onSearch() {
        String key = searchField.getText().trim();
        if (key.isEmpty()) {
            searchResultLabel.setText("Resultado: Informe uma chave.");
            return;
        }
        if (gerenciador == null) {
            searchResultLabel.setText("Resultado: Carregue um arquivo primeiro.");
            return;
        }

        ResultadoBusca resultadoIndice = gerenciador.buckets.buscarPorIndice(key);
        ResultadoBusca resultadoScan = gerenciador.executarTableScan(key);

        String status = resultadoIndice.isEncontrado()
                ? "Encontrado na página " + resultadoIndice.getPaginaDestino()
                : "Não encontrado";

        searchResultLabel.setText(String.format(
                "[Índice] %s | Custo: %d acessos | Tempo: %.3f ms%n" +
                        "[Table Scan] Custo: %d acessos | Tempo: %.3f ms",
                status,
                resultadoIndice.getCustoLeitura(),
                resultadoIndice.getTempoExecucao() / 1_000_000.0,
                resultadoScan.getCustoLeitura(),
                resultadoScan.getTempoExecucao() / 1_000_000.0
        ));
    }

    /**
     * Ação ao clicar em "Table Scan". Executa varredura completa e exibe custo/tempo.
     */
    @FXML
    protected void onTableScan() {
        String key = searchField.getText().trim();
        if (key.isEmpty()) {
            searchResultLabel.setText("Resultado: Informe uma chave para o Table Scan.");
            return;
        }
        if (gerenciador == null) {
            searchResultLabel.setText("Resultado: Carregue um arquivo primeiro.");
            return;
        }

        ResultadoBusca resultado = gerenciador.executarTableScan(key);

        String status = resultado.isEncontrado()
                ? "Encontrado na página " + resultado.getPaginaDestino()
                : "Não encontrado";

        searchResultLabel.setText(String.format(
                "[Table Scan] %s | Custo: %d páginas lidas | Tempo: %.3f ms",
                status,
                resultado.getCustoLeitura(),
                resultado.getTempoExecucao() / 1_000_000.0
        ));
    }
}
