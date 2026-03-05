package com.grupo8.indicehash.classes;

public class RelatorioComparativoBusca {

    private final Buckets buckets;
    private final GerenciadorArquivo gerenciadorArquivo;

    public RelatorioComparativoBusca(Buckets buckets, GerenciadorArquivo gerenciadorArquivo) {
        this.buckets = buckets;
        this.gerenciadorArquivo = gerenciadorArquivo;
    }

    public String comparar(String palavra) {
        ResultadoBusca resultadoIndice = buckets.buscarPorIndice(palavra);
        ResultadoBusca resultadoTableScan = gerenciadorArquivo.executarTableScan(palavra);

        String menorCusto = compararCusto(resultadoIndice, resultadoTableScan);
        String maisRapida = compararTempo(resultadoIndice, resultadoTableScan);

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("=== COMPARATIVO DE BUSCA ===\n");
        relatorio.append("Palavra buscada: ").append(palavra).append("\n\n");

        relatorio.append("[Busca por índice]\n");
        relatorio.append("Encontrado: ").append(resultadoIndice.isEncontrado()).append("\n");
        relatorio.append("Página destino: ").append(resultadoIndice.getPaginaDestino()).append("\n");
        relatorio.append("Custo estimado de acessos: ").append(resultadoIndice.getCustoLeitura()).append("\n");
        relatorio.append("Tempo de execução: ").append(resultadoIndice.tempoExecucao).append(" ns\n\n");

        relatorio.append("[Table Scan]\n");
        relatorio.append("Encontrado: ").append(resultadoTableScan.isEncontrado()).append("\n");
        relatorio.append("Página destino: ").append(resultadoTableScan.getPaginaDestino()).append("\n");
        relatorio.append("Custo estimado de acessos: ").append(resultadoTableScan.getCustoLeitura()).append("\n");
        relatorio.append("Tempo de execução: ").append(resultadoTableScan.tempoExecucao).append(" ns\n\n");

        relatorio.append("[Conclusão]\n");
        relatorio.append("Menor custo estimado: ").append(menorCusto).append("\n");
        relatorio.append("Busca mais rápida: ").append(maisRapida);

        return relatorio.toString();
    }

    private String compararCusto(ResultadoBusca indice, ResultadoBusca tableScan) {
        if (indice.getCustoLeitura() < tableScan.getCustoLeitura()) {
            return "Busca por índice";
        }
        if (tableScan.getCustoLeitura() < indice.getCustoLeitura()) {
            return "Table Scan";
        }
        return "Empate no custo estimado";
    }

    private String compararTempo(ResultadoBusca indice, ResultadoBusca tableScan) {
        if (indice.tempoExecucao < tableScan.tempoExecucao) {
            return "Busca por índice";
        }
        if (tableScan.tempoExecucao < indice.tempoExecucao) {
            return "Table Scan";
        }
        return "Empate no tempo de execução";
    }
}
