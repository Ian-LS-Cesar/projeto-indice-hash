package com.grupo8.indicehash.controller;

import com.grupo8.indicehash.classes.GerenciadorArquivo;
import com.grupo8.indicehash.classes.RelatorioComparativoBusca;
import com.grupo8.indicehash.classes.ResultadoBusca;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Controlador principal da interface grafica.
 * Gerencia acoes do usuario e exibe dados das estruturas.
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
                statusLabel.setText("Status: Tamanho da pagina deve ser > 0.");
                return;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Status: Tamanho da pagina invalido.");
            return;
        }

        try {
            gerenciador = new GerenciadorArquivo(pageSize);
            gerenciador.carregarArquivo(filePath);

            statusLabel.setText("Status: Arquivo carregado com sucesso. | Total de palavras: "
                    + gerenciador.getTotalPalavras());
            totalPagesLabel.setText("Total de paginas: " + gerenciador.getTotalPaginas());

            var primeira = gerenciador.getPrimeiraPagina();
            if (primeira != null) {
                firstPageList.getItems().setAll(primeira.getRegistros().stream().limit(5).toList());
            } else {
                firstPageList.getItems().clear();
            }

            var ultima = gerenciador.getUltimaPagina();
            if (ultima != null) {
                lastPageList.getItems().setAll(ultima.getRegistros().stream().limit(5).toList());
            } else {
                lastPageList.getItems().clear();
            }

            int colisoes = gerenciador.buckets.qColisoes;
            int overflows = gerenciador.buckets.qOverFlow;
            int totalBuckets = gerenciador.buckets.qBuckets;
            int totalPalavras = gerenciador.getTotalPalavras();

            double taxaColisao = totalPalavras > 0 ? (colisoes * 100.0 / totalPalavras) : 0;
            double taxaOverflow = totalPalavras > 0 ? (overflows * 100.0 / totalPalavras) : 0;

            collisionLabel.setText(String.format("Colisoes: %d (%.1f%%)", colisoes, taxaColisao));
            overflowLabel.setText(String.format("Overflow: %d (%.1f%%)", overflows, taxaOverflow));

            bucketList.getItems().clear();
            bucketList.getItems().add("Total de buckets: " + totalBuckets);
            bucketList.getItems().add("Chaves por bucket: " + gerenciador.buckets.qChaveValor);
        } catch (Exception e) {
            statusLabel.setText("Erro ao carregar arquivo: " + e.getMessage());
            totalPagesLabel.setText("Total de paginas: -");
            firstPageList.getItems().clear();
            lastPageList.getItems().clear();
            bucketList.getItems().clear();
            collisionLabel.setText("Colisoes: -");
            overflowLabel.setText("Overflow: -");
            gerenciador = null;
        }
    }

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

        RelatorioComparativoBusca relatorioComparativo =
                new RelatorioComparativoBusca(gerenciador.buckets, gerenciador);
        searchResultLabel.setText(relatorioComparativo.comparar(key));
    }

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
                ? "Encontrado na pagina " + resultado.getPaginaDestino()
                : "Nao encontrado";

        searchResultLabel.setText(String.format(
                "[Table Scan] %s | Custo: %d paginas lidas | Tempo: %.3f ms",
                status,
                resultado.getCustoLeitura(),
                resultado.getTempoExecucao() / 1_000_000.0
        ));
    }
}
