package com.projetointegrador.comunicavet.dto.image;

import org.springframework.core.io.Resource;

public record ImageAndContentTypeDTO(
    Resource image, String contentType
) { }
