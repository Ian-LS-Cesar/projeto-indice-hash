package com.grupo8.indicehash.classes;

import java.util.LinkedList;
import java.util.Set;

public class Buckets {

    LinkedList<Bucket>[] buckets;
    int qRegistros;
    int qChaveValor;
    public int qColisoes;
    public int qOverFlow; 
    int qPalavras;
    int qBuckets;

    public Buckets(int qChaveValor, int qPalavras, Set<String> palavrasUnicas, int tamPagina){
        qColisoes = 0;
        qOverFlow = 0;
        qRegistros = 0;
        this.qPalavras = qPalavras;
        this.qBuckets = qPalavras / qChaveValor;
        buckets = new LinkedList[qBuckets];
        this.qChaveValor = qChaveValor;

        for(int i = 0; i < buckets.length; i++){
            buckets[i] = new LinkedList<Bucket>();
            buckets[i].add(new Bucket(qChaveValor));
        }

        System.out.println("Quantidade de buckets: "+buckets.length);        
        for(String palavra : palavrasUnicas){
            int qualBucket = fHash(palavra);
            LinkedList<Bucket> listaAtual = buckets[qualBucket];
            int tamLista = listaAtual.size();
            int contador = 0;
            boolean inserido = false;

            for (Bucket bucket : listaAtual) {
                contador++;
                
                if (!bucket.estaCheio()) {
                    inserido = true;
                    int qualPagina =  qRegistros/tamPagina;
                    bucket.adicionaChaveValor(new ChaveValor(palavra, qualPagina));
                    qRegistros++;
                    break;
                }

                if(contador == 1){
                    qColisoes++;
                }
            }

            if(!inserido){
                Bucket novoBucket = new Bucket(2);
                int qualPagina =  qRegistros/tamPagina;
                novoBucket.adicionaChaveValor(new ChaveValor(palavra, qualPagina));
                
                listaAtual.addLast(novoBucket);
                qOverFlow++;
                qRegistros++;
            }
        }
    }



    public int fHash(String elemento){
        return (elemento.hashCode() & 0x7fffffff) % (qBuckets + 1);
    }

    public void adicionarElemento(String elemento){
        int nBucket = fHash(elemento);
        buckets[nBucket].add(null);
    }

    public ResultadoBusca buscarPorIndice(String chave){
        long inicio = System.nanoTime();
        int indiceBucket = fHash(chave);
        int custoAcessos = 1;

        LinkedList<Bucket> listaDeOverflow = buckets[indiceBucket];

        for (Bucket b : listaDeOverflow){
            for (int i = 0; i < b.nchaveValor; i++) {
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
