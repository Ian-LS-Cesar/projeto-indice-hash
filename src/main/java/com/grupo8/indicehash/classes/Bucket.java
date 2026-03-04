package com.grupo8.indicehash.classes;
public class Bucket{
    ChaveValor[] chaveValors;
    int nTuplas;


    public Bucket(int quantidadeTuplas){
        chaveValors = new ChaveValor[quantidadeTuplas];
        nTuplas = 0;
    }

    public boolean estaCheio(){
        return nTuplas == chaveValors.length;
    }

    public void adicionaTupla(ChaveValor chaveValor){
        if(estaCheio()){
            return;
        }
        chaveValors[nTuplas] = chaveValor;
        nTuplas++;
    }

    
}
