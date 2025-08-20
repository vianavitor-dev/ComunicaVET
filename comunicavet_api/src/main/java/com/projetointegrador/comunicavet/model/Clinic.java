package com.projetointegrador.comunicavet.model;

import jakarta.persistence.*;

import javax.naming.LimitExceededException;


@Entity
@Table(name = "clinic")
public class Clinic {
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(nullable = false)
    private int stars;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Column(name = "background_image")
    private String backgroundImage;

    public Clinic() {
        this.backgroundImage = "/example/static/images/default_background_image.png";
        this.stars = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getStars() {
        return stars;
    }

    /**
     * Soma um valor para o campo estrelas da clínica
     * @param value valor numérico a ser adicionado as estrelas da clínica
     * @throws IllegalArgumentException as estrelas da clínica apenas suportam valores entre 0-5
     * */
    public void addStars(int value) throws IllegalArgumentException {
        if (this.stars + value > 5) {
            throw new IllegalArgumentException("the stars of a clinic only supports values between 0-5");
        }

        this.stars += value;
    }

    /**
     * Subtrai um valor do campo estrelas da clínica
     * @param value valor numérico a ser subtraido as estrelas da clínica
     * @throws IllegalArgumentException as estrelas da clínica apenas suportam valores entre 0-5
     * */
    public void subStars(int value) throws IllegalArgumentException {
        if (this.stars - value > 5) {
            throw new IllegalArgumentException("the stars of a clinic only supports values between 0-5");
        }

        this.stars -= value;
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
