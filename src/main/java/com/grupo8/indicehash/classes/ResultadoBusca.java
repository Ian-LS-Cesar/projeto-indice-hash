package com.grupo8.indicehash.classes;

public class ResultadoBusca {

    public boolean encontrado;
    public int numPaginaDestino;
    public int custoPaginas;
    public long tempoExecucao;

    public ResultadoBusca(boolean encontrado, int numPaginaDestino, int custoPaginas, long tempoExecucao){
        this.encontrado = encontrado;
        this.numPaginaDestino = numPaginaDestino;
        this.custoPaginas = custoPaginas;
        this.tempoExecucao = tempoExecucao;
    }

    public boolean isEncontrado() { return encontrado; }
    public int getPaginaDestino() { return numPaginaDestino; }
    public int getCustoLeitura() { return custoPaginas; }
    public long getTempoExecucao() { return getTempoExecucao(); }

}
