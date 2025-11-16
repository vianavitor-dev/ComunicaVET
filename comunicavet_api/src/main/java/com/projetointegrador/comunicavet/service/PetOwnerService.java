package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.petOwner.NewPetOwnerDTO;
import com.projetointegrador.comunicavet.dto.petOwner.PetOwnerDTO;
import com.projetointegrador.comunicavet.dto.petOwner.PetOwnerProfileDTO;
import com.projetointegrador.comunicavet.dto.user.LoginDTO;
import com.projetointegrador.comunicavet.exceptions.DuplicateResourceException;
import com.projetointegrador.comunicavet.exceptions.InvalidCredentialsException;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.mapper.PetOwnerDTOMapper;
import com.projetointegrador.comunicavet.model.*;
import com.projetointegrador.comunicavet.repository.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;


@Service
public class PetOwnerService {
    @Autowired
    private PetOwnerRepository repository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * Salva um novo usuário (Dono de Pet) no Banco de Dados. Não registra usuários duplicados
     * @param dto Dados do novo usuário
     * @throws NotFoundResourceException Caso uma das pesquisas ao BD não retorne resultado
     * @throws DuplicateResourceException Caso o email já esteja em uso
     */
    public Long register(NewPetOwnerDTO dto) throws NotFoundResourceException, DuplicateResourceException, IllegalAccessException {
        // Verifica se este email já foi registrado
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new DuplicateResourceException("Este email já está em uso");
        }

        /*
            Busca por paises, estados e cidades compativeis com oque o usuário preencheu.
            Verifica se de fato essas informações estão contidas no banco,
            então prossede criando um Endereço e salvando um novo Dono de Pet no Banco de Dados
         */
        Address address = addressService.register(dto.address(), null, null, true);

        PetOwner petOwner = PetOwnerDTOMapper.toPetOwner(dto, address);

        // Pega a data de hoje com base na "zona" informada
        Instant now = Instant.now();
        LocalDate localDateNow = ZonedDateTime.ofInstant(
                now,
                ZoneId.of("America/Sao_Paulo")
        ).toLocalDate();

        // Preenche os valores padrões em determinados campos
        petOwner.setCreateAt(localDateNow);
        petOwner.activate();

        // Criptografa a senha
        String hashedPassword = encoder.encode(dto.password());

        petOwner.setPassword(hashedPassword);

        repository.save(petOwner);

        return petOwner.getId();
    }

    public Iterable<PetOwnerDTO> getAll() {
        // Busca todos os Donos de Pet registrados no Banco de Dados
        // e mapea o resultado pra o tipo List<PetOwnerDTO>
        List<PetOwnerDTO> result = ((List<PetOwner>) repository.findAll())
                .stream()
                .map(PetOwnerDTOMapper::toDto)
                .toList();

        return result;
    }

    public PetOwnerProfileDTO viewProfile(@NotNull(message = "Id não pode ser vazio") Long id)
            throws NotFoundResourceException {

        PetOwner petOwner = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Dono de Pet não encontrado"));

        return PetOwnerDTOMapper.petOwnerProfileDto(petOwner);
    }

//    public Iterable<PetOwnerDTO> getByName(@NotNull String name) {
//        // Busca por Donos de Pet com nome compativel ao campo 'name'
//        // e mapea o resultado pra o tipo List<PetOwnerDTO>
//        List<PetOwnerDTO> result = repository.findByName(name)
//                .stream()
//                .map(PetOwnerDTOMapper::toDto)
//                .toList();
//
//        return result;
//    }

    public PetOwnerDTO getByEmail(@NotNull String email) throws NotFoundResourceException {
        PetOwner petOwner =  repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundResourceException("Dono de Pet não encontrado"));

        return PetOwnerDTOMapper.toDto(petOwner);
    }

    /**
     *Busca o usuário pelo email e senha
     * @param login Contem dados como email e senha
     * @return Id do usuário
     * @throws NotFoundResourceException Caso não encontre o usuário pelo email
     * @throws InvalidCredentialsException Caso os campos estejam incorretos
     */
    public Long logIn(LoginDTO login) throws NotFoundResourceException, InvalidCredentialsException {
        // Busca Dono de Pet pelo email
        PetOwner petOwner = repository.findByEmail(login.email())
                .orElseThrow(() -> new NotFoundResourceException("Dono de Pet não encontrado"));

        // Verifica se as senhas são compativeis
        if (!encoder.matches(login.password(), petOwner.getPassword())) {
            throw new InvalidCredentialsException("Email ou senha incorretos");
        }

        return petOwner.getId();
    }

    public void editProfile(@NotNull Long id, @NotNull PetOwnerProfileDTO profile)
            throws NotFoundResourceException, IllegalAccessException {

        PetOwner petOwner = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Dono de Pet não encontrado"));

        petOwner.setName(profile.name());
        petOwner.setEmail(profile.email());

        // Altera o endereço do Dono de Pet
        addressService.modify(petOwner.getAddress().getId(), profile.address(), null, null);

        repository.save(petOwner);
    }

    public void changePassword(Long id, String newPassword) throws NotFoundResourceException {
        PetOwner petOwner = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Dono de Pet não encontrado"));

        // Criptografa a senha
        String hashedPassword = encoder.encode(newPassword);

        petOwner.setPassword(hashedPassword);

        repository.save(petOwner);
    }

    public void changeProfileImage(@NotNull Long id, @NotNull MultipartFile file)
            throws NotFoundResourceException, IOException {

        PetOwner petOwner = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Dono de Pet não encontrado"));

        String imagePath = imageService.uploadImage(false, file, id);
        petOwner.setProfileImage(imagePath);

        repository.save(petOwner);
    }

    public void deleteById(@NotNull(message = "Id não pode ser vazio") Long id)
            throws NotFoundResourceException {

        PetOwner petOwner = repository.findById(id)
                        .orElseThrow(() -> new NotFoundResourceException("Dono de Pet não encontrado"));

        repository.delete(petOwner);
    }
}
