package com.projetointegrador.comunicavet.model;

public enum PetSize {
    SMALL("Pequeno"),MEDIUM("Médio"),BIG("Grande");

    private String value;

    PetSize(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
