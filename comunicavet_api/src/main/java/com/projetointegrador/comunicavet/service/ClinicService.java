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
import com.projetointegrador.comunicavet.service.nominatimApi.LocationApiService;
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

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    @Autowired
    private PetOwnerFocusRepository petOwnerFocusRepository;

    @Autowired
    private FavoriteClinicRepository favoriteClinicRepository;

    @Autowired
    private StaredClinicRepository staredClinicRepository;

    @Autowired
    private LocationApiService locationApi;

    @Autowired
    private ClinicFocusService clinicFocusService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * Salva um novo usuário (Clínica) no Banco de Dados. Não registra usuários duplicados
     * @param dto Dados do novo usuário
     * @throws NotFoundResourceException Caso uma das pesquisas ao BD não retorne resultado
     * @throws DuplicateResourceException Caso o email já esteja em uso
     */
    public Long register(NewClinicDTO dto)
            throws NotFoundResourceException, DuplicateResourceException, IllegalAccessException {

        // Verifica se este email já foi registrado
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new DuplicateResourceException("Este email já está em uso");
        }

        /*
            Busca por paises, estados e cidades compativeis com oque o usuário prencheu.
            Verifica se de fato essas informações estão contidas no banco,
            então prossede criando um Endereço e salvando uma nova Clínica no Banco de Dados
         */
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
                .map(clinic -> {
                    // Busca os contatos da Clínica atual pelo Id da Clínica
                    List<Contact> contacts = contactRepository.findByClinicId(clinic.getId());

                    return ClinicDTOMapper.toClinicDto(clinic, contacts.toArray(new Contact[0]));
                })
                .toList();

        return result;
    }

    /**
     *
     * @param optionalTags Tags que o usuário está interessado
     * @param optionalDto Dados do endereço, caso o usuário esteja de viajem
     * @throws NotFoundResourceException Caso não consiga encontra o usuário no sistema
     * @return
     */
    public Iterable<ClinicCardDTO> filter
        (Long userId, Optional<List<String>> optionalTags, Optional<SearchLocationDTO> optionalDto)
            throws NotFoundResourceException, IllegalAccessException {

        PetOwner petOwner = petOwnerRepository.findById(userId)
                .orElseThrow(() -> new NotFoundResourceException("Dono de Pet não encontrado"));

        // Caso o Dono de Pet não tenha providenciado nenhuma tag como filtro
        // o sistema ira buscar as tags(Focos) que ele indicou ter interesse anteriormente
        List<String> tagNames = optionalTags.orElseGet(() ->
                // Busca pelos Focos que o usuário escolheu
                petOwnerFocusRepository.findByPetOwner(petOwner)
                        .stream()
                        .map(
                                petOwnerFocus -> petOwnerFocus.getFocus().getName()
                        ).toList()
        );

        double userLat, userLon;
        boolean isNewAddress = optionalDto.isPresent();

        // Caso o Dono de Pet não tenha providenciado um endereço diferente
        // o sistema ira utilizar o endereço o próprio endereço dele
        SearchLocationDTO dto = optionalDto.orElseGet(() ->
                LocationDTOMapper.toSearchLocationDTO(petOwner.getAddress())
        );

        // Quando endereço informado for um endereço diferente do que o Dono de Pet tem
        // busca a localização deste endereço
        if (isNewAddress) {
            LocationDTO[] result = locationApi
                    .getLocationByAddress(dto, Optional.empty(), Optional.empty());

            userLat = Double.parseDouble(result[0].lat());
            userLon = Double.parseDouble(result[0].lon());

            // Caso este seja o endereço padrão do Dono de Pet, apenas pega latitude e longitude
        } else {
            userLat = petOwner.getAddress().getLocation().getLatitude();
            userLon = petOwner.getAddress().getLocation().getLongitude();
        }

        return repository
                .filterBy(tagNames, dto.country(), dto.state(), dto.city(), userLat, userLon)
                .stream()
                .map(clinic -> {
                    // Busca os contatos da Clínica atual pelo Id da Clínica
                    List<Contact> contacts = contactRepository.findByClinicId(clinic.getId());

                    // Verifica se o usuário já favoritou determinada Clínica
                    boolean wasFavorited = favoriteClinicRepository
                            .findByPetOwner(petOwner)
                            .stream()
                            .anyMatch(favoriteClinic ->
                                favoriteClinic.getClinic().getEmail().equals(clinic.getEmail())
                            );

                    return ClinicDTOMapper
                            .clinicCardDTO(clinic, contacts.toArray(new Contact[0]), wasFavorited);
                })
                .toList();
    }

    public Iterable<ClinicCardDTO> getByName(@NotNull String name) {
        List<ClinicCardDTO> result = repository.findByName(name)
                .stream()
                .map(clinic -> {
                    // Busca os contatos da Clínica atual pelo Id da Clínica
                    List<Contact> contacts = contactRepository.findByClinicId(clinic.getId());

                    return ClinicDTOMapper.clinicCardDTO(clinic, contacts.toArray(new Contact[0]), false);
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

    public ClinicProfileDTO viewProfile(@NotNull Long id) throws NotFoundResourceException {
        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrada"));

        List<Contact> contacts = contactRepository.findByClinicId(clinic.getId());

        List<FocusDTO> focuses = ((List<ClinicFocusDTO>) clinicFocusService.getByClinicId(id))
                .stream()
                .map(ClinicFocusDTO::focus)
                .toList();

        return ClinicDTOMapper.toClinicProfileDto(clinic, contacts, focuses);
    }

    /**
     *Busca o usuário pelo e-mail e senha
     * @param login Contem dados como e-mail e senha
     * @return Id do usuário
     * @throws NotFoundResourceException Caso não encontre o usuário pelo e-mail
     * @throws InvalidCredentialsException Caso os campos estejam incorretos
     */
    public Long logIn(LoginDTO login) throws NotFoundResourceException, InvalidCredentialsException {
        // Busca Clínica pelo e-mail
        Clinic clinic = repository.findByEmail(login.email())
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrada"));

        // Verifica se as senhas são compativeis
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

        if (modifyAddress) {
            addressService.modify(clinic.getAddress().getId(), profile.address(), null, null);
        }

        if (modifyContacts) {
            contactService.editClinicProfileContacts(id, profile.contacts());
        }

        repository.save(clinic);
    }

    public ClinicInfoDTO showInfo(@NotNull Long id) throws NotFoundResourceException {
        Clinic clinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrado"));

        List<Contact> contacts = contactRepository.findByClinicId(clinic.getId());

        List<FocusDTO> focuses = ((List<ClinicFocusDTO>) clinicFocusService.getByClinicId(id))
                .stream()
                .map(ClinicFocusDTO::focus)
                .toList();

        boolean wasFavorited = favoriteClinicRepository.findByClinic(clinic)
                .stream()
                .anyMatch(favorited ->
                    favorited.getClinic().getEmail().equals(clinic.getEmail())
                );

        // Adiciona uma visualização a página da clínica
        clinic.setViewers(clinic.getViewers() + 1);
        repository.save(clinic);

        return ClinicDTOMapper.toClinicInfoDTO(clinic, contacts, focuses, wasFavorited);
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
