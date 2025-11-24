package com.projetointegrador.comunicavet.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate dateBirth;

    @Column(nullable = false)
    private String breed = "Unknown"; // raça do cachorro

    @Column(nullable = false)
    private String secondBreed = "Unknown"; // segunda raça do cachorro

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetType type; // exemplo: cachorro, gato, outro

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetSize size = PetSize.MEDIUM;

    @ManyToOne
    @JoinColumn(name = "pet_owner_id")
    private PetOwner petOwner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(LocalDate dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSecondBreed() {
        return secondBreed;
    }

    public void setSecondBreed(String secondBreed) {
        this.secondBreed = secondBreed;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public PetSize getSize() {
        return size;
    }

    public void setSize(PetSize size) {
        this.size = size;
    }

    public PetOwner getPetOwner() {
        return petOwner;
    }

    public void setPetOwner(PetOwner petOwner) {
        this.petOwner = petOwner;
    }
}
