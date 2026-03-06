package com.grupo8.indicehash;

import java.io.IOException;

import com.grupo8.indicehash.classes.Buckets;
import com.grupo8.indicehash.classes.GerenciadorArquivo;

public class Teste {
    public static void main(String[] args){
        System.out.println("teste");
        try {
            GerenciadorArquivo gerenciador = new GerenciadorArquivo(5);
            gerenciador.carregarArquivo("/home/luizrct/Downloads/words.txt");
            System.out.println(gerenciador.buckets.qColisoes);
            System.out.println(gerenciador.buckets.qOverFlow);
        } catch (IOException e) {
            // CA03 — erro de arquivo
            System.out.println("\nErro ao carregar arquivo: " + e.getMessage());
        }
    }
}
