package com.projetointegrador.comunicavet.service;


import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.model.ValidCode;
import com.projetointegrador.comunicavet.repository.ValidCodeRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidCodeService {
    @Autowired
    private ValidCodeRepository repository;

    public void create(int code) {
        ValidCode validCode = new ValidCode();
        validCode.setCode(code);

        repository.save(validCode);
    }

    public boolean isThisCodeValid(int code) {
        return repository.findByCode(code).isPresent();
    }

    public void deleteByCode(@NotNull Integer code) {
        ValidCode validCode = repository.findByCode(code)
                .orElseThrow(() -> new NotFoundResourceException("Este código não existe"));

        repository.delete(validCode);
    }
}
