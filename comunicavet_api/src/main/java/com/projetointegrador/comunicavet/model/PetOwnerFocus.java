package com.projetointegrador.comunicavet.model;

import jakarta.persistence.*;

@Entity
@Table(name = "looking_for") // "looking for a focus"
public class PetOwnerFocus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "focus_id")
    private Focus focus;

    @ManyToOne
    @JoinColumn(name = "pet_owner_id")
    private PetOwner petOwner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Focus getFocus() {
        return focus;
    }

    public void setFocus(Focus focus) {
        this.focus = focus;
    }

    public PetOwner getPetOwner() {
        return petOwner;
    }

    public void setPetOwner(PetOwner petOwner) {
        this.petOwner = petOwner;
    }
}
