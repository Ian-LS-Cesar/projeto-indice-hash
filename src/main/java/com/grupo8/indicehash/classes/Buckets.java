package com.grupo8.indicehash.classes;

import java.util.LinkedList;

public class Buckets {

    LinkedList<Bucket>[] buckets;
    int qRegistros;
    int qTuplas; // Representa o FR (Capacidade do bucket)
    double pColisoes;
    double pOverFlow;
    int totalColisoes;
    int totalOverflows;
    int tentativasInsercao;

    public Buckets(int qBuckets, int qTuplas){
        pColisoes = 0;
        pOverFlow = 0;
        qRegistros = 0;
        totalColisoes = 0;
        totalOverflows = 0;
        tentativasInsercao = 0;
        buckets = new LinkedList[qBuckets];
        this.qTuplas = qTuplas;

        // Inicializa o array com uma lista encadeada vazia em cada posição
        // e já adiciona o primeiro bucket (primário) em cada lista
        for(int i = 0; i < buckets.length; i++){
            buckets[i] = new LinkedList<Bucket>();
            buckets[i].add(new Bucket(qTuplas));
        }
    }

    // A função hash deve ser projetada pela equipe
    public int fHash(String elemento){
        return 0; // Temporário. Você precisará implementar a lógica real aqui.
    }

    public void adicionarElemento(String elemento, int pagina){
        int nBucket = fHash(elemento);
        tentativasInsercao++;

        LinkedList<Bucket> listaDeOverflow = buckets[nBucket];

        // Pega o último bucket da cadeia para verificar se ainda tem espaço
        Bucket bucketAtual = listaDeOverflow.getLast();

        // Verifica se o bucket atual já está cheio (atingiu o limite qTuplas/FR)
        if (bucketAtual.nTuplas >= qTuplas) {
            // Conta a colisão apenas quando o bucket enche, conforme a regra de negócio
            totalColisoes++;

            // O bucket excedeu a capacidade, gerando um overflow
            totalOverflows++;

            // Cria um novo bucket de overflow, adiciona na lista e o define como o atual
            bucketAtual = new Bucket(qTuplas);
            listaDeOverflow.add(bucketAtual);
        }

        // Insere a chave de busca e o endereço da página na primeira posição vazia do bucket
        // Nota: Certifique-se de que a classe se chama ChaveValor no seu projeto
        bucketAtual.chaveValors[bucketAtual.nTuplas] = new ChaveValor(elemento, pagina);

        // Incrementa a quantidade de tuplas ocupadas neste bucket
        bucketAtual.nTuplas++;

        // Incrementa o total geral de registros inseridos no índice
        qRegistros++;

        // Recalcula as taxas de colisão e overflow a cada inserção
        calcularTaxas();
    }

    private void calcularTaxas(){
        // Calcula a porcentagem com base no total de inserções tentadas
        pColisoes = tentativasInsercao > 0 ? (double) totalColisoes / tentativasInsercao * 100 : 0;
        pOverFlow = tentativasInsercao > 0 ? (double) totalOverflows / tentativasInsercao * 100 : 0;
    }

    public double getTaxaColisoes(){
        return pColisoes;
    }

    public double getTaxaOverflows(){
        return pOverFlow;
    }

    public void exibirTaxas(){
        System.out.printf("Taxa de Colisões: %.2f%%\n", pColisoes);
        System.out.printf("Taxa de Overflows: %.2f%%\n", pOverFlow);
    }

    public ResultadoBusca buscarPorIndice(String chave){
        long inicio = System.nanoTime();
        int indiceBucket = fHash(chave);
        int custoAcessos = 1;

        LinkedList<Bucket> listaDeOverflow = buckets[indiceBucket];

        for (Bucket b : listaDeOverflow){
            for (int i = 0; i < b.nTuplas; i++) {
                if (b.chaveValors[i] != null && b.chaveValors[i].palavra.equals(chave)) {
                    long fim =  System.nanoTime();
                    // Retorna o resultado com a página onde o registro está
                    return new ResultadoBusca(true, b.chaveValors[i].pagina, custoAcessos, fim - inicio);
                }
            }
            // Se não encontrou neste bucket e vai para o próximo na lista de overflow, o custo de acesso aumenta
            custoAcessos++;
        }
        // Se percorreu tudo e não achou
        return new ResultadoBusca(false, -1, custoAcessos, System.nanoTime() - inicio);
    }
}