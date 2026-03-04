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
    private int totalPalavrasCarregadas = 0;

    public GerenciadorArquivo(int capacidadePorPagina) {
        if (capacidadePorPagina <= 0) {
            throw new IllegalArgumentException("Capacidade por página deve ser maior que zero.");
        }
        this.capacidadePorPagina = capacidadePorPagina;
        this.paginas = new ArrayList<>();
    }

    public void carregarArquivo(String caminhoArquivo) throws IOException {
        Set<String> palavrasUnicas = new LinkedHashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String palavra = linha.trim();
                if (!palavra.isEmpty()) {
                    palavrasUnicas.add(palavra);
                }
            }
        } catch (IOException e) {
            throw new IOException("Arquivo ilegível ou não encontrado: " + caminhoArquivo, e);
        }

        if (palavrasUnicas.isEmpty()) {
            throw new IOException("O arquivo está vazio ou não contém palavras válidas.");
        }

        totalPalavrasCarregadas = palavrasUnicas.size();
        dividirEmPaginas(new ArrayList<>(palavrasUnicas));
    }

    private void dividirEmPaginas(List<String> registros) {
        paginas.clear();
        Pagina<String> paginaAtual = new Pagina<>(capacidadePorPagina);
        paginas.add(paginaAtual);

        for (String registro : registros) {
            if (!paginaAtual.inserir(registro)) {
                paginaAtual = new Pagina<>(capacidadePorPagina);
                paginas.add(paginaAtual);
                paginaAtual.inserir(registro);
            }
        }
    }

    public List<Pagina<String>> getPaginas() {
        return paginas;
    }

    public int getTotalPaginas() {
        return paginas.size();
    }

    public int getTotalPalavras() {
        return totalPalavrasCarregadas;
    }

    public Pagina<String> getPrimeiraPagina() {
        if (paginas.isEmpty()) return null;
        return paginas.get(0);
    }

    public Pagina<String> getUltimaPagina() {
        if (paginas.isEmpty()) return null;
        return paginas.get(paginas.size() - 1);
    }

    // CA07 — exibe no console primeira e última página com primeiros 5 registros
    public void exibirPrimeiraEUltimaPagina() {
        if (paginas.isEmpty()) return;

        exibirPagina(0);
        if (paginas.size() > 1) {
            exibirPagina(paginas.size() - 1);
        }
    }

    private void exibirPagina(int indice) {
        Pagina<String> pagina = paginas.get(indice);
        System.out.println("Página " + (indice + 1) + ":");
        List<String> registros = pagina.getRegistros();
        registros.stream().limit(5).forEach(r -> System.out.println("  - " + r));
    }

    public void exibirResumo() {
        System.out.println("Total de palavras carregadas: " + getTotalPalavras());
        System.out.println("Total de páginas: " + getTotalPaginas());
        for (int i = 0; i < paginas.size(); i++) {
            Pagina<String> pagina = paginas.get(i);
            System.out.println("Página " + (i + 1) + ": " + pagina.getOcupacao() + "/" + pagina.getCapacidade() + " registros");
        }
    }

    public ResultadoBusca executarTableScan(String palavraAlvo){
        long inicio = System.nanoTime();
        int paginasLidas = 0;

        for (int i = 0; i < paginas.size(); i++) {
            paginasLidas++;
            Pagina<String> p = paginas.get(i);
            for (String registro : p.getRegistros()) {
                if (registro.equalsIgnoreCase(palavraAlvo)) {
                    long fim = System.nanoTime();
                    return new ResultadoBusca(true, i, paginasLidas, fim - inicio);
                }
            }
        }
        return new ResultadoBusca(false, -1, paginasLidas, System.nanoTime() - inicio);
    }
}
