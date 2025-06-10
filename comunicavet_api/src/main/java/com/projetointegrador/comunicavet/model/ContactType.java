package com.projetointegrador.comunicavet.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contact_type")
public class ContactType {
    @Id
    @Column(columnDefinition = "TINYINT(4)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private byte id;

    @Column(nullable = false, unique = true)
    private String name;

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
