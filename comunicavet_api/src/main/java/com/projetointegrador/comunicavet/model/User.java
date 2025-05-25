package com.projetointegrador.comunicavet.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;


@Entity
@Table(name = "user")
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name= "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name= "profile_image")
    private String profileImage;

    @Column(name = "create_at", nullable = false)
    private LocalDate createAt;

    @Column(name = "update_at", nullable = false)
    private LocalDate updateAt;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    // private Address address;

    public User() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserType getType() {
        return type;
    }

    public void setPetOwner() {
        this.type = UserType.PET_OWNER;
    }

    public void setClinic() {
        this.type = UserType.CLINIC;
    }

    public boolean isPetOwner() {
        return this.type.compare("pet_owner");
    }

    public boolean isClinic() {
        return this.type.compare("clinic");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Atribui um valor email para o email do usuário
     * @param email email do usuário a ser atrelado a classe User
     * @throws NullPointerException email não pode ser nulo
     * @throws RuntimeException email inválido
     * */
    public void setEmail(String email) {
        if (email == null) {
            throw new NullPointerException("email cannot be null");
        }

        Pattern pattern = Pattern.compile("^[^\\s{|}=,]+@(.+)$"); // Validação super simples
        boolean isEmailValid = pattern.matcher(email).matches();

        if (!isEmailValid) {
            throw new RuntimeException("invalid email");
        }

        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String zoneId) {
        Instant now = Instant.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, ZoneId.of(zoneId));

        this.createAt = zonedDateTime.toLocalDate();
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String zoneId) {
        Instant now = Instant.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, ZoneId.of(zoneId));

        this.createAt = zonedDateTime.toLocalDate();
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    @Override
    public String toString() {
        String format = """
                User:
                \tname: %s\s
                \ttype: %s\s
                \temail: %s\s
                \tpassword: %s
                \tprofileImage: %s\s
                \tcreateAt: %s\s
                \tupdateAt: %s\s
                \tactive: %b
        """;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return String.format(format,
                this.name, this.type, this.email,
                this.password, this.profileImage,
                this.createAt.format(formatter), this.updateAt.format(formatter),
                this.active
        );
    }
}
