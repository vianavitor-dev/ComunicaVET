package com.projetointegrador.comunicavet.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite_clinic")
public class FavoriteClinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @OneToMany
    @JoinColumn(name = "pet_owner_id")
    private PetOwner petOwner;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public PetOwner getPetOwner() {
        return petOwner;
    }

    public void setPetOwner(PetOwner petOwner) {
        this.petOwner = petOwner;
    }
}
