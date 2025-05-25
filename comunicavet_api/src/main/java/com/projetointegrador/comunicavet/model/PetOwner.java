package com.projetointegrador.comunicavet.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "pet_owner")
public class PetOwner {
    @Id
    private long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "update_at", nullable = false)
    private LocalDate updateAt;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String zoneId) {
        Instant now = Instant.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, ZoneId.of(zoneId));

        this.updateAt = zonedDateTime.toLocalDate();
    }
}
