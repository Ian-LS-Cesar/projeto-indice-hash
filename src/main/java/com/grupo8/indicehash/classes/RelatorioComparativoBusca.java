package com.grupo8.indicehash.classes;

public class RelatorioComparativoBusca {

    private final Buckets buckets;
    private final GerenciadorArquivo gerenciadorArquivo;

    public RelatorioComparativoBusca(Buckets buckets, GerenciadorArquivo gerenciadorArquivo) {
        this.buckets = buckets;
        this.gerenciadorArquivo = gerenciadorArquivo;
    }

    public String comparar(String palavra) {
        ResultadoBusca resultadoIndice;
        ResultadoBusca resultadoTableScan;
        String erroIndice = null;
        String erroTableScan = null;

        long inicioIndice = System.nanoTime();
        try {
            resultadoIndice = buckets.buscarPorIndice(palavra);
        } catch (Exception e) {
            long tempoFalha = System.nanoTime() - inicioIndice;
            resultadoIndice = new ResultadoBusca(false, -1, Integer.MAX_VALUE, tempoFalha);
            erroIndice = e.getClass().getSimpleName() + ": " + e.getMessage();
        }

        long inicioTableScan = System.nanoTime();
        try {
            resultadoTableScan = gerenciadorArquivo.executarTableScan(palavra);
        } catch (Exception e) {
            long tempoFalha = System.nanoTime() - inicioTableScan;
            resultadoTableScan = new ResultadoBusca(false, -1, Integer.MAX_VALUE, tempoFalha);
            erroTableScan = e.getClass().getSimpleName() + ": " + e.getMessage();
        }

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
        if (erroIndice != null) {
            relatorio.append("Erro na busca por índice: ").append(erroIndice).append("\n\n");
        }

        relatorio.append("[Table Scan]\n");
        relatorio.append("Encontrado: ").append(resultadoTableScan.isEncontrado()).append("\n");
        relatorio.append("Página destino: ").append(resultadoTableScan.getPaginaDestino()).append("\n");
        relatorio.append("Custo estimado de acessos: ").append(resultadoTableScan.getCustoLeitura()).append("\n");
        relatorio.append("Tempo de execução: ").append(resultadoTableScan.tempoExecucao).append(" ns\n\n");
        if (erroTableScan != null) {
            relatorio.append("Erro no table scan: ").append(erroTableScan).append("\n\n");
        }

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
