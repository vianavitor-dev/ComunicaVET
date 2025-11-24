package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.clinic.*;
import com.projetointegrador.comunicavet.dto.clinicFocus.ClinicFocusDTO;
import com.projetointegrador.comunicavet.dto.focus.FocusDTO;
import com.projetointegrador.comunicavet.dto.location.LocationDTO;
import com.projetointegrador.comunicavet.dto.location.SearchLocationDTO;
import com.projetointegrador.comunicavet.dto.user.LoginDTO;
import com.projetointegrador.comunicavet.exceptions.DuplicateResourceException;
import com.projetointegrador.comunicavet.exceptions.InvalidCredentialsException;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.mapper.ClinicDTOMapper;
import com.projetointegrador.comunicavet.mapper.LocationDTOMapper;
import com.projetointegrador.comunicavet.model.*;
import com.projetointegrador.comunicavet.repository.*;
import com.projetointegrador.comunicavet.service.externalApi.NominatimService;
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
import java.util.Optional;

@Service
public class ClinicService {
    @Autowired
    private ClinicRepository repository;

    @Autowired
    private AddressService addressService;

    // REMOVIDO: ContactRepository, ContactService

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    @Autowired
    private PetOwnerFocusRepository petOwnerFocusRepository;

    @Autowired
    private FavoriteClinicRepository favoriteClinicRepository;

    @Autowired
    private StaredClinicRepository staredClinicRepository;

    @Autowired
    private NominatimService locationApi;

    @Autowired
    private ClinicFocusService clinicFocusService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public Long register(NewClinicDTO dto)
            throws NotFoundResourceException, DuplicateResourceException, IllegalAccessException {

        // Verifica se este email já foi registrado
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new DuplicateResourceException("Este email já está em uso");
        }

        Address address = addressService.register(dto.address(), null, null, true);

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
        String hashedPassword = encoder.encode(dto.password());

        clinic.setPassword(hashedPassword);

        repository.save(clinic);

        return clinic.getId();
    }

    public Iterable<ClinicDTO> getAll() {
        List<ClinicDTO> result = ((List<Clinic>) repository.findAll())
                .stream()
                .map(ClinicDTOMapper::toClinicDto) // ALTERADO: Não precisa mais buscar contatos
                .toList();

        return result;
    }

    public Iterable<ClinicCardDTO> filter
            (Long userId, Optional<List<String>> optionalTags, Optional<SearchLocationDTO> optionalDto)
            throws NotFoundResourceException, IllegalAccessException {

        PetOwner petOwner = petOwnerRepository.findById(userId)
                .orElseThrow(() -> new NotFoundResourceException("Dono de Pet não encontrado"));

        List<String> tagNames = optionalTags.orElseGet(() ->
                petOwnerFocusRepository.findByPetOwner(petOwner)
                        .stream()
                        .map(
                                petOwnerFocus -> petOwnerFocus.getFocus().getName()
                        ).toList()
        );

        double userLat, userLon;
        boolean isNewAddress = optionalDto.isPresent();

        SearchLocationDTO dto = optionalDto.orElseGet(() ->
                LocationDTOMapper.toSearchLocationDTO(petOwner.getAddress())
        );

        if (isNewAddress) {
            System.out.println("Is new address?!");
            LocationDTO[] result = locationApi
                    .getLocationByAddress(dto, Optional.empty(), Optional.empty());

            if (result.length == 0) {
                throw new NotFoundResourceException("Localização não encontrada");
            }
            userLat = Double.parseDouble(result[0].lat());
            userLon = Double.parseDouble(result[0].lon());

        } else {
            userLat = petOwner.getAddress().getLatitude();
            userLon = petOwner.getAddress().getLongitude();
        }

        return repository
                .filterBy(tagNames, dto.country(), dto.state(), dto.city(), userLat, userLon)
                .stream()
                .map(clinic -> {
                    // REMOVIDO: Busca de contatos
                    boolean wasFavorited = favoriteClinicRepository
                            .findByPetOwner(petOwner)
                            .stream()
                            .anyMatch(favoriteClinic ->
                                    favoriteClinic.getClinic().getEmail().equals(clinic.getEmail())
                            );

                    return ClinicDTOMapper.clinicCardDTO(clinic, wasFavorited);
                })
                .toList();
    }

    public Iterable<ClinicCardDTO> getByName(@NotNull String name) {
        List<ClinicCardDTO> result = repository.findByName(name)
                .stream()
                .map(clinic -> ClinicDTOMapper.clinicCardDTO(clinic, false)) // ALTERADO: Não precisa mais buscar contatos
                .toList();

        return result;
    }

    public ClinicDTO getByEmail(@NotNull String email) throws NotFoundResourceException {
        Clinic clinic = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrada"));

        return ClinicDTOMapper.toClinicDto(clinic); // ALTERADO: Não precisa mais buscar contatos
    }

    public ClinicProfileDTO viewProfile(@NotNull Long id) throws NotFoundResourceException {
        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrada"));

        List<FocusDTO> focuses = ((List<ClinicFocusDTO>) clinicFocusService.getByClinicId(id))
                .stream()
                .map(ClinicFocusDTO::focus)
                .toList();

        return ClinicDTOMapper.toClinicProfileDto(clinic, focuses); // ALTERADO: Não precisa mais buscar contatos
    }

    public Long logIn(LoginDTO login) throws NotFoundResourceException, InvalidCredentialsException {
        Clinic clinic = repository.findByEmail(login.email())
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrada"));

        if (!encoder.matches(login.password(), clinic.getPassword())) {
            throw new InvalidCredentialsException("Email ou senha incorretos");
        }

        return clinic.getId();
    }

    public void editProfile
            (@NotNull Long id, ClinicProfileDTO profile, boolean modifyAddress, boolean modifyContacts)
            throws NotFoundResourceException, IllegalAccessException {

        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrado"));

        clinic.setName(profile.name());
        clinic.setEmail(profile.email());
        clinic.setDescription(profile.description());
        clinic.setPhone(profile.phone()); // NOVO: definir telefone

        if (modifyAddress) {
            addressService.modify(clinic.getAddress().getId(), profile.address(), null, null);
        }

        // REMOVIDO: modifyContacts (não existe mais)

        repository.save(clinic);
    }

    public ClinicInfoDTO showInfo(@NotNull Long id) throws NotFoundResourceException {
        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrado"));

        List<FocusDTO> focuses = ((List<ClinicFocusDTO>) clinicFocusService.getByClinicId(id))
                .stream()
                .map(ClinicFocusDTO::focus)
                .toList();

        boolean wasFavorited = favoriteClinicRepository.findByClinic(clinic)
                .stream()
                .anyMatch(favorited ->
                        favorited.getClinic().getEmail().equals(clinic.getEmail())
                );

        clinic.setViewers(clinic.getViewers() + 1);
        repository.save(clinic);

        return ClinicDTOMapper.toClinicInfoDTO(clinic, focuses, wasFavorited); // ALTERADO: Não precisa mais buscar contatos
    }

    public void rateClinic(@NotNull Long id, @NotNull Long petOwnerId, @NotNull Integer stars)
            throws NotFoundResourceException, IllegalArgumentException {

        if (stars > 5) {
            throw new IllegalArgumentException("Número de estrelas informado inválido");
        }

        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrado"));

        PetOwner petOwner = petOwnerRepository.findById(petOwnerId)
                .orElseThrow(() -> new NotFoundResourceException("Dono de Pet não encontrado"));

        // Registra nota do usuário (em forma de estrelas) no Banco de Dados
        StaredClinic staredClinic = new StaredClinic();
        staredClinic.setRating(stars);
        staredClinic.setClinic(clinic);
        staredClinic.setPetOwner(petOwner);

        staredClinicRepository.save(staredClinic);

        // Busca por todas as avaliações da Clínica
        List<Integer> ratingList = staredClinicRepository.getByClinic(clinic)
                .stream()
                .map(StaredClinic::getRating)
                .toList(); // Integer func(s) { return s.getRating(); }

        int ratingListSum = 0;

        for (Integer rating : ratingList) {
            ratingListSum += rating;
        }

        /*
            Calcula a média das avaliações da Clínica
            e utiliza essa média para atualizar a avaliação da Clínica em forma de estrelas
         */
        int ratingAverage = ratingListSum / ratingList.size();

        clinic.setStars(ratingAverage);
        repository.save(clinic);
    }

    public void changePassword(@NotNull Long id, String newPassword) throws NotFoundResourceException {
        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrado"));

        String hashedPassword = encoder.encode(newPassword);
        clinic.setPassword(hashedPassword);

        repository.save(clinic);
    }

    public void changeProfileImage(@NotNull Long id, @NotNull MultipartFile file)
            throws NotFoundResourceException, IOException {

        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrado"));

        String imagePath = imageService.uploadImage(false, file, id);
        clinic.setProfileImage(imagePath);

        repository.save(clinic);
    }

    public void changeBackgroundImage(@NotNull Long id, @NotNull MultipartFile file)
            throws NotFoundResourceException, IOException {

        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrado"));

        String imagePath = imageService.uploadImage(true, file, id);
        clinic.setBackgroundImage(imagePath);

        repository.save(clinic);
    }

    public void deleteById(@NotNull Long id)  throws NotFoundResourceException {
        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrado"));

        repository.delete(clinic);
    }
}
