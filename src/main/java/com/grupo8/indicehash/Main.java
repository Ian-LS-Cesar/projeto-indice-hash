package com.grupo8.indicehash;

import com.grupo8.indicehash.classes.GerenciadorArquivo;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Teste do Gerenciador de Arquivo ===\n");

        // CA01 — informar caminho do arquivo
        System.out.print("Digite o caminho do arquivo .txt: ");
        String caminho = scanner.nextLine().trim();

        // CA04/CA05 — informar tamanho da página
        int tamanhoPagina = 0;
        while (tamanhoPagina <= 0) {
            System.out.print("Digite o número de registros por página: ");
            try {
                tamanhoPagina = Integer.parseInt(scanner.nextLine().trim());
                if (tamanhoPagina <= 0) {
                    System.out.println("Erro: informe um número maior que zero.");
                }
            } catch (NumberFormatException e) {
                // CA05 — entrada inválida
                System.out.println("Erro: entrada inválida. Digite um número inteiro.");
            }
        }

        try {
            GerenciadorArquivo gerenciador = new GerenciadorArquivo(tamanhoPagina);
            gerenciador.carregarArquivo(caminho);

            System.out.println("\n--- Resumo ---");

            // CA02
            System.out.println("Total de palavras únicas: " + gerenciador.getTotalPalavras());

            // CA06
            System.out.println("Total de páginas: " + gerenciador.getTotalPaginas());

            // CA07
            System.out.println("\n--- Primeira e Última Página (primeiros 5 registros) ---");
            gerenciador.exibirPrimeiraEUltimaPagina();

            // Resumo completo opcional
            System.out.print("\nExibir resumo completo de todas as páginas? (s/n): ");
            String opcao = scanner.nextLine().trim();
            if (opcao.equalsIgnoreCase("s")) {
                gerenciador.exibirResumo();
            }

        } catch (IOException e) {
            // CA03 — erro de arquivo
            System.out.println("\nErro ao carregar arquivo: " + e.getMessage());
        }

        scanner.close();System.out.println("\nEncerrando...");
    }
}
