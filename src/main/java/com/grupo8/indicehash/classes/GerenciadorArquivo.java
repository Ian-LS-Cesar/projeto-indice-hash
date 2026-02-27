package com.grupo8.indicehash.classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GerenciadorArquivo {

    private final int capacidadePorPagina;
    private final List<Pagina<String>> paginas;

    public GerenciadorArquivo(int capacidadePorPagina){
        if (capacidadePorPagina <= 0){
            throw new IllegalArgumentException("Capacidade por página deve ser maior que zero.");
        }
        this.capacidadePorPagina = capacidadePorPagina;
        this.paginas = new ArrayList<>();
    }

    public void carregarArquivo(String caminhoArquivo) throws IOException {
        Set<String> palavrasUnicas = new LinkedHashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String palavra = linha.trim();
                if (!palavra.isEmpty()) {
                    palavrasUnicas.add(palavra);
                }
            }
        }

        dividirEmPaginas(new ArrayList<>(palavrasUnicas));
    }

    private void dividirEmPaginas(List<String> registros){
        paginas.clear();
        Pagina<String> paginaAtual = new Pagina<>(capacidadePorPagina);
        paginas.add(paginaAtual);

        for (String registro : registros) {
            if (!paginaAtual.inserir(registro)){
                paginaAtual = new Pagina<>(capacidadePorPagina);
                paginas.add(paginaAtual);
                paginaAtual.inserir(registro);
            }
        }
    }

    public List<Pagina<String>> getPaginas(){
        return paginas;
    }

    public int getTotalPaginas(){
        return paginas.size();
    }

    public void exibirResumo(){
        System.out.println("Total de páginas: " + getTotalPaginas());
        for (int i = 0; i < paginas.size(); i++) {
            Pagina<String> pagina = paginas.get(i);
            System.out.println("Página " + (i + 1) + ": " + pagina.getOcupacao() + "/" + pagina.getCapacidade() + " registros");
        }
    }
}
