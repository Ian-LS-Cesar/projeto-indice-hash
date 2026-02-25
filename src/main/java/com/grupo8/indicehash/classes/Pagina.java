package com.grupo8.indicehash.classes;

import java.util.ArrayList;
import java.util.List;

public class Pagina<T> {
    private final int capacidade;
    private final List<T> registros;

    public Pagina(int capacidade, List<T> registros) {
        this.capacidade = capacidade;
        this.registros = new ArrayList<>(capacidade);
    }

    public boolean inserir (T registro){
        if (isCheio()) return false;
        registros.add(registro);
        return true;
    }

    public boolean isCheio(){
        return registros.size() >= capacidade;
    }

    public List<T> getRegistros(){
        return registros;
    }
}
