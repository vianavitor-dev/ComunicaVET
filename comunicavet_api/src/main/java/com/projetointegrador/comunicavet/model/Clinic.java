package com.projetointegrador.comunicavet.model;

import jakarta.persistence.*;

import javax.naming.LimitExceededException;

@Entity
@DiscriminatorValue("clinic")
public class Clinic extends User {

    @Column(nullable = false)
    private int stars;

    @Column(name = "background_image", nullable = false)
    private String backgroundImage;

    public Clinic() {
        this.backgroundImage = "/example/static/images/default_background_image.png";
        this.stars = 0;
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
        if (backgroundImage == null) {
            return;
        }

        this.backgroundImage = backgroundImage;
    }
}
