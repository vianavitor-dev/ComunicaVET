package com.projetointegrador.comunicavet.model;

import jakarta.persistence.*;

import javax.naming.LimitExceededException;

@Entity
@DiscriminatorValue("clinic")
public class Clinic extends User {

    @Column(nullable = false, columnDefinition = "int DEFAULT 0")
    private int stars;

    @Column(nullable = false, columnDefinition = "int DEFAULT 0")
    private int viewers;

    @Column(name = "background_image", columnDefinition = "varchar(255) DEFAULT 'images/background/default.png'")
    private String backgroundImage;

    @Column(columnDefinition = "text")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public int getViewers() {
        return viewers;
    }

    public void setViewers(int viewers) {
        this.viewers = viewers;
    }
}
