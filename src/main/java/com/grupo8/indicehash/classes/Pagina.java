package com.grupo8.indicehash.classes;

import java.util.ArrayList;
import java.util.List;

public class Pagina<T> {
    private final int capacidade;
    private final List<T> registros;

    public Pagina(int capacidade) {
        if (capacidade <= 0){
            throw new IllegalArgumentException("Capacidade deve ser maior que zero.");
        }

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

    public int getCapacidade() {
        return capacidade;
    }

    public int getOcupacao() {
        return registros.size();
    }

    public List<T> getRegistros(){
        return registros;
    }
}
