package com.grupo8.indicehash.classes;
public class Bucket{
    Tupla[] tuplas;
    int nTuplas;


    public Bucket(int quantidadeTuplas){
        tuplas = new Tupla[quantidadeTuplas];
        nTuplas = 0;
    }

    public boolean estaCheio(){
        return nTuplas == tuplas.length;
    }

    public void adicionaTupla(Tupla tupla){
        if(estaCheio()){
            return;
        }
        tuplas[nTuplas] = tupla;
        nTuplas++;
    }

    
}
