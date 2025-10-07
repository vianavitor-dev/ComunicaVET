package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.focus.FocusDTO;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.mapper.FocusDTOMapper;
import com.projetointegrador.comunicavet.model.Focus;
import com.projetointegrador.comunicavet.repository.FocusRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FocusService {
    private FocusRepository repository;

    public Iterable<FocusDTO> getAll() {
        List<FocusDTO> result = ((List<Focus>) repository.findAll())
                .stream()
                .map(FocusDTOMapper::toFocusDto)
                .toList();

        return result;
    }

    public FocusDTO getById(@NotNull Byte id) throws NotFoundResourceException {
        Focus focus = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Este Foco não existe"));

        return FocusDTOMapper.toFocusDto(focus);
    }

    public FocusDTO getByName(@NotNull String name) throws NotFoundResourceException {
        Focus focus = repository.findByName(name)
                .orElseThrow(() -> new NotFoundResourceException("Este Foco não existe"));

        return FocusDTOMapper.toFocusDto(focus);
    }
}
