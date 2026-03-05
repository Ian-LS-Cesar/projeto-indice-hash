package com.grupo8.indicehash.classes;
public class Bucket{
    ChaveValor[] chaveValors;
    int nchaveValor;


    public Bucket(int quantidadeTuplas){
        chaveValors = new ChaveValor[quantidadeTuplas];
        nchaveValor = 0;
    }

    public boolean estaCheio(){
        return nchaveValor == chaveValors.length;
    }

    public void adicionaChaveValor(ChaveValor chaveValor){
        if(estaCheio()){
            return;
        }
        chaveValors[nchaveValor] = chaveValor;
        nchaveValor++;
    }

    
}
