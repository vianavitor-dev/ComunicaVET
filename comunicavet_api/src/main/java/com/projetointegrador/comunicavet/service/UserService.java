package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.user.LoginDTO;
import com.projetointegrador.comunicavet.dto.user.UserIdentityDTO;
import com.projetointegrador.comunicavet.exceptions.InvalidCredentialsException;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.PetOwner;
import com.projetointegrador.comunicavet.model.User;
import com.projetointegrador.comunicavet.repository.PetOwnerRepository;
import com.projetointegrador.comunicavet.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    // Providencia uma função genérica para fazer o Login no sistema
    public UserIdentityDTO logIn(LoginDTO login) throws NotFoundResourceException, InvalidCredentialsException {
        // Busca Usuário pelo e-mail
        User user = repository.findByEmail(login.email())
                .orElseThrow(() -> new NotFoundResourceException("Usuário não encontrado"));

        boolean isClinic = petOwnerRepository.findById(user.getId()).isEmpty();

        // Verifica se as senhas são compativeis
        if (!encoder.matches(login.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Senha incorreta");
        }

        return new UserIdentityDTO(user.getId(), isClinic);
    }

    public void changePassword(@NotNull Long id, @NotNull String password) throws NotFoundResourceException {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Usuário não encontrado"));

        // Criptografa a senha
        String hashedPassword = encoder.encode(password);

        // Muda a senha e salva a alteração
        user.setPassword(hashedPassword);
        repository.save(user);
    }

    public void deactivate(@NotNull Long id, @NotNull String password)
            throws NotFoundResourceException, InvalidCredentialsException {

        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Usuário não encontrado"));

        // Verifica se as senhas são compativeis
        if (!encoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Email ou senha incorretos");
        }

        // Desativa a conta do usuário
        // Não apaga os dados do usuário no banco de dados, mas impede o acesso à conta
        user.deactivate();

        repository.save(user);
    }
}
