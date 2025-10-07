package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.clinic.ClinicDTO;
import com.projetointegrador.comunicavet.dto.clinic.NewClinicDTO;
import com.projetointegrador.comunicavet.dto.user.LoginDTO;
import com.projetointegrador.comunicavet.exceptions.DuplicateResourceException;
import com.projetointegrador.comunicavet.exceptions.InvalidCredentialsException;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.mapper.ClinicDTOMapper;
import com.projetointegrador.comunicavet.model.Address;
import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.Contact;
import com.projetointegrador.comunicavet.repository.ClinicRepository;
import com.projetointegrador.comunicavet.repository.ContactRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ClinicService {
    @Autowired
    private ClinicRepository repository;

    @Autowired
    private AddressService addressService;

    @Autowired
    ContactRepository contactRepository;

    /**
     * Salva um novo usuário (Clínica) no Banco de Dados. Não registra usuários duplicados
     * @param dto Dados do novo usuário
     * @throws NotFoundResourceException Caso uma das pesquisas ao BD não retorne resultado
     * @throws DuplicateResourceException Caso o email já esteja em uso
     */
    public void register(NewClinicDTO dto)
            throws NotFoundResourceException, DuplicateResourceException {

        // Verifica se este email já foi registrado
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new DuplicateResourceException("Este email já está em uso");
        }

        /*
            Busca por paises, estados e cidades compativeis com oque o usuário prencheu.
            Verifica se de fato essas informações estão contidas no banco,
            então prossede criando um Endereço e salvando uma nova Clínica no Banco de Dados
         */
        Address address = addressService.register(dto.address(), true);

        Clinic clinic = ClinicDTOMapper.toClinic(dto, address);

        // Pega a data de hoje com base na "zona" informada
        Instant now = Instant.now();
        LocalDate localDateNow = ZonedDateTime.ofInstant(
                now,
                ZoneId.of("America/Sao_Paulo")
        ).toLocalDate();

        clinic.setCreateAt(localDateNow);
        clinic.activate();

        // Criptografa a senha
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(dto.password());

        clinic.setPassword(hashedPassword);

        repository.save(clinic);
    }

    public Iterable<ClinicDTO> getAll() {
        List<ClinicDTO> result = ((List<Clinic>) repository.findAll())
                .stream()
                .map(clinic -> {
                    // Busca os contatos da Clínica atual pelo Id da Clínica
                    List<Contact> contacts = contactRepository.findByClinicId(clinic.getId());

                    return ClinicDTOMapper.toClinicDto(clinic, contacts.toArray(new Contact[0]));
                })
                .toList();

        return result;
    }

    public Iterable<ClinicDTO> getByName(@NotNull String name) {
        List<ClinicDTO> result = repository.findByName(name)
                .stream()
                .map(clinic -> {
                    // Busca os contatos da Clínica atual pelo Id da Clínica
                    List<Contact> contacts = contactRepository.findByClinicId(clinic.getId());

                    return ClinicDTOMapper.toClinicDto(clinic, contacts.toArray(new Contact[0]));
                })
                .toList();

        return result;
    }

    public ClinicDTO getByEmail(@NotNull String email) throws NotFoundResourceException {
        Clinic clinic = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrada"));

        List<Contact> contacts = contactRepository.findByClinicId(clinic.getId());

        return ClinicDTOMapper.toClinicDto(clinic, contacts.toArray(new Contact[0]));
    }

    /**
     *Busca o usuário pelo e-mail e senha
     * @param login Contem dados como e-mail e senha
     * @return Id do usuário
     * @throws NotFoundResourceException Caso não encontre o usuário pelo e-mail
     * @throws InvalidCredentialsException Caso os campos estejam incorretos
     */
    public Long logIn(LoginDTO login) throws NotFoundResourceException, InvalidCredentialsException {
        // Busca Dono de Pet pelo e-mail
        Clinic clinic = repository.findByEmail(login.email())
                .orElseThrow(() -> new NotFoundResourceException(" não encontrada"));

        // Verifica se as senhas são compativeis
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(login.password(), clinic.getPassword())) {
            throw new InvalidCredentialsException("Email ou senha incorretos");
        }

        return clinic.getId();
    }

    public void changeNameAndEmail(ClinicDTO dto) throws NotFoundResourceException {
        Clinic clinic = repository.findById(dto.id())
                .orElseThrow(() -> new NotFoundResourceException(" não encontrado"));

        clinic.setName(dto.name());
        clinic.setEmail(dto.email());

        repository.save(clinic);
    }

    public void changePassword(Long id, String newPassword) throws NotFoundResourceException {
        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException(" não encontrado"));

        clinic.setPassword(newPassword);

        repository.save(clinic);
    }

    public void changeProfileImage(Long id, String newImagePath) throws NotFoundResourceException {
        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException(" não encontrado"));

        clinic.setProfileImage(newImagePath);

        repository.save(clinic);
    }

    public void changeBackgroundImage(Long id, String newImagePath) throws NotFoundResourceException {
        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException(" não encontrado"));

        clinic.setBackgroundImage(newImagePath);

        repository.save(clinic);
    }

    public void deleteById(@NotNull Long id)  throws NotFoundResourceException {
        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException(" não encontrado"));

        repository.delete(clinic);
    }
}
