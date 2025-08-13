package com.projetointegrador.comunicavet.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "report_count")
    private int reportCount;

    @Column(name = "likes_count")
    private int likesCount;

    @Column(name = "create_at", nullable = false)
    private LocalDate createAt;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getReportCount() {
        return reportCount;
    }

    /**
     * Atribui um valor ao número de denuncias
     * @param reportCount valor numérico que o 'reportCount' irá receber
     * @throws IllegalArgumentException as estrelas da clínica apenas suportam valores entre 0-10
     * */
    public void setReportCount(int reportCount) throws IllegalArgumentException {
        if (reportCount > 10 || reportCount < 0) {
            throw new IllegalArgumentException("The comment report count only supports values between 0 and 10");
        }

        this.reportCount = reportCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String zoneId) {
        Instant now = Instant.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, ZoneId.of(zoneId));

        this.createAt = zonedDateTime.toLocalDate();
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}
