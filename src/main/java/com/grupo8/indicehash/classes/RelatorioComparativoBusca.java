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
        String reducaoAcessos = calcularReducaoPercentualCusto(resultadoIndice, resultadoTableScan);
        String ganhoTempo = calcularGanhoPercentualTempo(resultadoIndice, resultadoTableScan);

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("=== COMPARATIVO DE BUSCA ===\n");
        relatorio.append("Palavra buscada: ").append(palavra).append("\n\n");

        relatorio.append("[Busca por indice]\n");
        relatorio.append("Encontrado: ").append(resultadoIndice.isEncontrado()).append("\n");
        relatorio.append("Pagina destino: ").append(resultadoIndice.getPaginaDestino()).append("\n");
        relatorio.append("Custo estimado de acessos: ").append(resultadoIndice.getCustoLeitura()).append("\n");
        relatorio.append("Tempo de execucao: ").append(resultadoIndice.tempoExecucao).append(" ns\n\n");
        if (erroIndice != null) {
            relatorio.append("Erro na busca por indice: ").append(erroIndice).append("\n\n");
        }

        relatorio.append("[Table Scan]\n");
        relatorio.append("Encontrado: ").append(resultadoTableScan.isEncontrado()).append("\n");
        relatorio.append("Pagina destino: ").append(resultadoTableScan.getPaginaDestino()).append("\n");
        relatorio.append("Custo estimado de acessos: ").append(resultadoTableScan.getCustoLeitura()).append("\n");
        relatorio.append("Tempo de execucao: ").append(resultadoTableScan.tempoExecucao).append(" ns\n\n");
        if (erroTableScan != null) {
            relatorio.append("Erro no table scan: ").append(erroTableScan).append("\n\n");
        }

        relatorio.append("[Conclusao]\n");
        relatorio.append("Menor custo estimado: ").append(menorCusto).append("\n");
        relatorio.append("Busca mais rapida: ").append(maisRapida).append("\n");
        relatorio.append("Reducao de acessos: ").append(reducaoAcessos).append("\n");
        relatorio.append("Ganho de tempo: ").append(ganhoTempo);

        return relatorio.toString();
    }

    private String compararCusto(ResultadoBusca indice, ResultadoBusca tableScan) {
        if (indice.getCustoLeitura() < tableScan.getCustoLeitura()) {
            return "Busca por indice";
        }
        if (tableScan.getCustoLeitura() < indice.getCustoLeitura()) {
            return "Table Scan";
        }
        return "Empate no custo estimado";
    }

    private String compararTempo(ResultadoBusca indice, ResultadoBusca tableScan) {
        if (indice.tempoExecucao < tableScan.tempoExecucao) {
            return "Busca por indice";
        }
        if (tableScan.tempoExecucao < indice.tempoExecucao) {
            return "Table Scan";
        }
        return "Empate no tempo de execucao";
    }

    private String calcularReducaoPercentualCusto(ResultadoBusca indice, ResultadoBusca tableScan) {
        if (tableScan.getCustoLeitura() <= 0) {
            return "Nao foi possivel calcular";
        }

        double percentual = ((double) (tableScan.getCustoLeitura() - indice.getCustoLeitura())
                / tableScan.getCustoLeitura()) * 100.0;

        if (percentual > 0) {
            return String.format("Indice reduz %.2f%% os acessos", percentual);
        }
        if (percentual < 0) {
            return String.format("Indice aumenta %.2f%% os acessos", Math.abs(percentual));
        }
        return "Sem diferenca percentual de acessos";
    }

    private String calcularGanhoPercentualTempo(ResultadoBusca indice, ResultadoBusca tableScan) {
        if (tableScan.tempoExecucao <= 0) {
            return "Nao foi possivel calcular";
        }

        double percentual = ((double) (tableScan.tempoExecucao - indice.tempoExecucao)
                / tableScan.tempoExecucao) * 100.0;

        if (percentual > 0) {
            return String.format("Indice e %.2f%% mais rapido", percentual);
        }
        if (percentual < 0) {
            return String.format("Indice e %.2f%% mais lento", Math.abs(percentual));
        }
        return "Sem diferenca percentual de tempo";
    }
}
