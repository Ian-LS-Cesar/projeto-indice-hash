package com.grupo8.indicehash.classes;

import java.util.LinkedList;

public class Buckets {

    LinkedList<Bucket>[] buckets;
    int qRegistros;
    int qTuplas;
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

        for(int i = 0; i < buckets.length; i++){
            buckets[i] = new LinkedList<Bucket>();
            buckets[i].add(new Bucket(qTuplas));
        }
    }

    public int fHash(String elemento){
        return 0;
    }

    public void adicionarElemento(String elemento){
        int nBucket = fHash(elemento);
        tentativasInsercao++;

        LinkedList<Bucket> listaDeOverflow = buckets[nBucket];

        // Verifica colisão (bucket já possui elementos)
        if (listaDeOverflow.getFirst().nTuplas > 0) {
            totalColisoes++;
        }

        // Verifica overflow (bucket primário cheio)
        if (listaDeOverflow.getFirst().nTuplas >= qTuplas) {
            totalOverflows++;
            listaDeOverflow.add(new Bucket(qTuplas));
        }

        buckets[nBucket].add(null);
        qRegistros++;

        // Recalcula as taxas
        calcularTaxas();
    }

    private void calcularTaxas(){
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

                    return new ResultadoBusca(true, b.chaveValors[i].pagina, custoAcessos + 1, fim - inicio);
                }
            }
            custoAcessos++;
        }
        return new ResultadoBusca(false, -1, custoAcessos, System.nanoTime() - inicio);
    }
}