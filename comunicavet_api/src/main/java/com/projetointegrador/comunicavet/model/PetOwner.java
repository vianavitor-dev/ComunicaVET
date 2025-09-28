package com.projetointegrador.comunicavet.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@DiscriminatorValue("pet_owner")
public class PetOwner extends User {

    @Column(name = "update_at", nullable = false)
    private LocalDate updateAt;

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }
}
