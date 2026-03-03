package com.grupo8.indicehash.classes;

import java.util.LinkedList;

public class Buckets {

    LinkedList<Bucket>[] buckets;
    int qRegistros;
    int qTuplas;
    double pColisoes;
    double pOverFlow; 


    public Buckets(int qBuckets, int qTuplas){
        pColisoes = 0;
        pOverFlow = 0;
        qRegistros = 0;
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
        buckets[nBucket].add(null);
    }
}
