package com.projetointegrador.comunicavet.model;

public enum UserType {
    PET_OWNER("pet_owner"), CLINIC("clinic");

    private final String value;

    private UserType(String value) {
        this.value = value;
    }

    /**
     * Compara dois valores e devolve um tipo booleano com base nisso.
     * @param value o tipo do usuário (“pet_owner” ou “clinic”)
     * @return se os valores forem iguais retorna <b>true</b> se não, <b>false</b>
     */
    public boolean compare(String value) {
        return this.value.equals(value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
