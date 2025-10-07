package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.address.AddressDTO;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.mapper.AddressDTOMapper;
import com.projetointegrador.comunicavet.model.*;
import com.projetointegrador.comunicavet.repository.AddressRepository;
import com.projetointegrador.comunicavet.repository.CityRepository;
import com.projetointegrador.comunicavet.repository.CountryRepository;
import com.projetointegrador.comunicavet.repository.StateRepository;
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

    /**
     *Cria um novo Endereço. Não registra Endereços duplicados
     * @param dto Dados necessários para criação do Endereço
     * @param returnAddress Indica se a função deve retornar o Endereço ou não
     * @return Caso o parametro 'returnAddress' seja verdadeiro, retorna o Endereço criado
     * @throws NotFoundResourceException Caso uma das pesquisas ao BD não retorne resultado
     */
    public Address register(AddressDTO dto, boolean returnAddress)
            throws NotFoundResourceException {
        /*
        Busca por países, estados, e cidades com nomes compativeis com oque está presente no DTO
         */
        Country country = countryRepository.findByName(dto.country())
                .orElseThrow(() -> new NotFoundResourceException("País não encontrado"));

        State state = stateRepository.findByName(dto.state())
                .orElseThrow(() -> new NotFoundResourceException("Estado não encontrado"));

        List<City> cities = cityRepository.findByName(dto.city());

        if (cities.isEmpty()) {
            throw new NotFoundResourceException("Cidade não encontrada");
        }

        // Verifica se este endereço já existe no Banco de Dados
        Optional<Address> result = repository
                .findByCityAndStateAndCountry(dto.city(), dto.state(), dto.country());

        // Retorna o Endereço caso o 'result' não esteja vazio,
        // se não, mapea os dados coletados e retorna um Endereço
        Address address = result.orElseGet(() ->
                AddressDTOMapper.toAddress(dto, country, state, cities.getFirst())
        );

        // TODO: Buscar localização deste Endereço via API, e atrelar localização ao Endereço

        repository.save(address);

        if (returnAddress) {
            return address;
        }

        return null;
    }

    public Iterable<AddressDTO> getAll() {
        List<AddressDTO> result = ((List<Address>) repository.findAll())
                .stream()
                .map(AddressDTOMapper::toDto)
                .toList();

        return result;
    }

    public AddressDTO getById(@NotNull(message = "Id não pode ser vazio") Long id)
            throws NotFoundResourceException {

        Address address = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Endereço não encontrado"));

        return AddressDTOMapper.toDto(address);
    }

    public AddressDTO findByCityAndStateAndCountry(String cityStr, String stateStr, String countryStr)
            throws NotFoundResourceException{

        Address address = repository.findByCityAndStateAndCountry(cityStr, stateStr, countryStr)
                .orElseThrow(() -> new NotFoundResourceException("Erro ao buscar endereço"));

        return AddressDTOMapper.toDto(address);
    }

    public AddressDTO findByCityAndStateAndCountry(City city, State state, Country country)
            throws NotFoundResourceException {

        Address address = repository.findByCityAndStateAndCountry(city, state, country)
                .orElseThrow(() -> new NotFoundResourceException("Erro ao buscar endereço"));

        return AddressDTOMapper.toDto(address);
    }

    /**
     * Modifica os seguintes campos:
     * país, estado, cidade, vizinhança, rua, numero, e complemento
     * @param dto Dados do Endereço alterados
     * @throws NotFoundResourceException Caso uma das pesquisas ao BD não retorne resultado
     */
    public void modify(AddressDTO dto) throws NotFoundResourceException {
        Address address = repository.findById(dto.id())
                .orElseThrow(() -> new NotFoundResourceException("Endereço não encontrado"));

        Country country = countryRepository.findByName(dto.country())
                .orElseThrow(() -> new NotFoundResourceException("País não encontrado"));

        State state = stateRepository.findByName(dto.state())
                .orElseThrow(() -> new NotFoundResourceException("Estado não encontrado"));

        List<City> cities = cityRepository.findByName(dto.city());

        if (cities.isEmpty()) {
            throw new NotFoundResourceException("Cidade não encontrada");
        }

        // Modifica todos os campos
        address.setState(state);
        address.setCity(cities.getFirst());
        address.setCountry(country);
        address.setNeighborhood(dto.neighborhood());
        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setComplement(dto.complement());
        // TODO: Buscar nova localização pela API e atualizando a do Endereço

        repository.save(address);
    }

    public void deleteById(Long id) throws NotFoundResourceException {
        Address address = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Endereço não encontrado"));

        repository.delete(address);
    }
}
