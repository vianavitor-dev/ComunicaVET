package com.projetointegrador.comunicavet.model;

public enum PetType {
    DOG("Cão"),CAT("Gato"),OTHER("Outro");

    private String value;

    PetType(String value) {
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
