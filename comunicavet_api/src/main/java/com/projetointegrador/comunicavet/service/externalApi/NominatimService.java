package com.projetointegrador.comunicavet.service.externalApi;

import com.projetointegrador.comunicavet.dto.location.ReverseLocationDTO;
import com.projetointegrador.comunicavet.dto.location.SearchLocationDTO;
import com.projetointegrador.comunicavet.dto.location.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class NominatimService {
    /*
        RestTemplate:
        - Utilizado para acessar outras APIs de forma sincrona
     */
    @Autowired
    private RestTemplate restTemplate;

    // URL da API
    private final static String url = "https://nominatim.openstreetmap.org";

    public List<?> getLocationByCity(String city) {
        String urlWithParameters = url + "?city="+city+"&format=jsonv2";

        return restTemplate.getForObject(urlWithParameters, ArrayList.class);
    }

    /**
     * Busca uma localização(latitude, longitude) baseado nos campos presentes no endereço
     * na API do Nominatim
     * @param dto Dados do endereço
     * @param optionalFormat Formato do resultado da API
     * @param optionalLimit Limite de resultados
     * @return Retorna um array de DTOs contendo a localização do endereço
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public LocationDTO[] getLocationByAddress
            (SearchLocationDTO dto, Optional<String> optionalFormat, Optional<Integer> optionalLimit)
            throws IllegalArgumentException, IllegalAccessException {

        // Preenchendo as variaveis com os seus respectivos valores padrões
        String format = optionalFormat.orElse("jsonv2");
        int limit = optionalLimit.orElse(1);

        // Construtor da URL que será utilizada para fazer a requisição ao NominatimAPI
        StringBuilder requestUrlBuilder = new StringBuilder(url + "/search?");

        // Recebe todos os campos presentes no SearchLocationDTO
        Field[] fields = dto.getClass().getDeclaredFields();

        /*
            Percorre por todos os campos de SearchLocationDTO (dto passado como parametro)
            e apenas adiciona ao 'requestUrlBuilder' os campos do DTO
            que não forem vazios
         */
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(dto);

            if (fieldValue != null) {
                requestUrlBuilder
                        .append(field.getName().toLowerCase()).append("=")
                        .append(fieldValue).append("&");
            }
        }

        requestUrlBuilder
                .append("format=").append(format.toLowerCase()).append("&")
                .append("limit=").append(limit);

        // Transforma o StringBuilder em uma String
        String requestUrl = requestUrlBuilder.toString();

        return restTemplate.getForObject(requestUrl, LocationDTO[].class);
    }

    /**
     * Busca um endereço pela API do Nominatim baseado na localização (latitude, longitude)
     * @param dto Contem a latitude e a longitude
     * @param optionalFormat Formato do resultado da API
     * @return Retorna um DTO contendo os dados do endereço
     */
    public ReverseLocationDTO getAddressByLocation(LocationDTO dto, Optional<String> optionalFormat) {
        String format = optionalFormat.orElse("jsonv2");

        String requestUrl = url + "/reverse?" +
                "lat=" + dto.lat() +
                "&lon=" + dto.lon() +
                "&format=" + format +
                "&addressdetails=" + "1";

        return restTemplate.getForObject(requestUrl, ReverseLocationDTO.class);
    }

}
