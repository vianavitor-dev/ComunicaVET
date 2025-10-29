package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.address.AddressDTO;
import com.projetointegrador.comunicavet.dto.address.ProfileAddressDTO;
import com.projetointegrador.comunicavet.dto.location.LocationDTO;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.mapper.AddressDTOMapper;
import com.projetointegrador.comunicavet.mapper.LocationDTOMapper;
import com.projetointegrador.comunicavet.model.*;
import com.projetointegrador.comunicavet.repository.*;
import com.projetointegrador.comunicavet.service.nominatimApi.LocationApiService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository repository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private LocationApiService nominatimService;

    @Autowired
    private LocationRepository locationRepository;

    /**
     *Cria um novo Endereço. Não registra Endereços duplicados
     * @param dto Dados necessários para criação do Endereço
     * @param returnAddress Indica se a função deve retornar o Endereço ou não
     * @return Caso o parametro 'returnAddress' seja verdadeiro, retorna o Endereço criado
     * @throws NotFoundResourceException Caso uma das pesquisas ao BD não retorne resultado
     */
    public Address register
            (@NotNull AddressDTO dto, String format, Integer limit, boolean returnAddress)
            throws NotFoundResourceException, IllegalAccessException {

        Address address;
        Optional<Address> result = repository
                .findByAllFields(dto.street(), dto.neighborhood(), dto.city(), dto.state(), dto.country());

        if (result.isEmpty()) {
            /*
                Busca por países, estados, e cidades com nomes compativeis com oque está presente no DTO
             */
            Country country = countryRepository.findByName(dto.country())
                    .orElseThrow(() -> new NotFoundResourceException("País não encontrado"));

            State state = stateRepository.findByName(dto.state())
                    .orElseThrow(() -> new NotFoundResourceException("Estado não encontrado"));

            List<City> cities = cityRepository.findByName(dto.city());

            if (cities.isEmpty()) {
                City newCity = new City();
                newCity.setName(dto.city());

                cityRepository.save(newCity);
            }

            address = AddressDTOMapper.toAddress(dto, country, state, cities.getFirst());

            // Busca localização deste Endereço via API, e atrelar localização ao Endereço
            LocationDTO apiResponseData = nominatimService.getLocationByAddress(
                    LocationDTOMapper.toSearchLocationDTO(address),
                    Optional.ofNullable(format), Optional.ofNullable(limit)
            )[0];

            Location location = (LocationDTOMapper.toLocation(apiResponseData));
            locationRepository.save(location);

            address.setLocation(location);
            repository.save(address);

        } else {
            address = result.get();
        }

        if (returnAddress) {
            return address;
        }

        return null;
    }

    public Iterable<AddressDTO> getAll() {
        List<AddressDTO> result = ((List<Address>) repository.findAll())
                .stream()
                .map(AddressDTOMapper::toAddressDto)
                .toList();

        return result;
    }

    public AddressDTO getById(@NotNull(message = "Id não pode ser vazio") Long id)
            throws NotFoundResourceException {

        Address address = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Endereço não encontrado"));

        return (AddressDTOMapper.toAddressDto(address));
    }

    public AddressDTO findByCityAndStateAndCountry(String cityStr, String stateStr, String countryStr)
            throws NotFoundResourceException{

        Address address = repository.findByCityAndStateAndCountry(cityStr, stateStr, countryStr)
                .orElseThrow(() -> new NotFoundResourceException("Erro ao buscar endereço"));

        return (AddressDTOMapper.toAddressDto(address));
    }

    public AddressDTO findByCityAndStateAndCountry(City city, State state, Country country)
            throws NotFoundResourceException {

        Address address = repository.findByCityAndStateAndCountry(city, state, country)
                .orElseThrow(() -> new NotFoundResourceException("Erro ao buscar endereço"));

        return (AddressDTOMapper.toAddressDto(address));
    }

    /**
     * Modifica os seguintes campos:
     * país, estado, cidade, vizinhança, rua, numero, e complemento. E busca a nova localização
     * @param dto Dados do Endereço alterados
     * @throws NotFoundResourceException Caso uma das pesquisas ao BD não retorne resultado
     */
    public void modify(@NotNull Long id, ProfileAddressDTO dto, String format, Integer limit)
            throws NotFoundResourceException, IllegalAccessException {

        Address address = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Endereço não encontrado"));

        Country country = countryRepository.findByName(dto.country())
                .orElseThrow(() -> new NotFoundResourceException("País não encontrado"));

        State state = stateRepository.findByName(dto.state())
                .orElseThrow(() -> new NotFoundResourceException("Estado não encontrado"));

        List<City> cities = cityRepository.findByName(dto.city());

        if (cities.isEmpty()) {
            City newCity = new City();
            newCity.setName(dto.city());

            cityRepository.save(newCity);
        }

        // Modifica todos os campos
        address.setState(state);
        address.setCity(cities.getFirst());
        address.setCountry(country);
        address.setNeighborhood(dto.neighborhood());
        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setComplement(dto.complement());

        // Buscando nova localização pela API do Nominatim e Atualizando a Localização do Endereço
        LocationDTO apiResponseData = nominatimService.getLocationByAddress(
                LocationDTOMapper.toSearchLocationDTO(address),
                Optional.ofNullable(format), Optional.ofNullable(limit)
        )[0];

        /*
            Transforma o resultado da busca do Nominatim API (DTO) em uma
            entidade (Location), então atualiza o valor da Localização presente
            no Endereço pela nova antes de salvar as alterações do Endereço
         */
        Location location = LocationDTOMapper.toLocation(apiResponseData);
        locationRepository.save(location);

        address.setLocation(location);
        repository.save(address);
    }

    public void deleteById(Long id) throws NotFoundResourceException {
        Address address = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Endereço não encontrado"));

        repository.delete(address);
    }
}
