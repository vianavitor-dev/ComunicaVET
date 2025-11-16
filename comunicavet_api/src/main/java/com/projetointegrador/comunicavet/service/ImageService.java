package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.image.ImageAndContentTypeDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Value("${image.path}")
    private String baseImageUrlPath;

    public String uploadImage
            (@NotNull boolean isBackgroundImage, @NotNull MultipartFile imageFile, @NotNull Long userId)
            throws IOException {

        String uniqueImageName = userId.toString() + "_" + imageFile.getOriginalFilename();
        String imageUrlPath = baseImageUrlPath + "/profileImages";

        if (isBackgroundImage) {
            imageUrlPath = baseImageUrlPath + "/backgroundImages";
        }

        Path uploadPath = Path.of(imageUrlPath);
        Path filePath = uploadPath.resolve(uniqueImageName); // junta os caminhos

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Optional<File> optionalImage = this.getById(userId, uploadPath.toFile());

        if (optionalImage.isPresent()) {
            // Deleta a imagem antiga do usuário
            if (!optionalImage.get().delete()) {
                throw new IOException("Erro ao excluir arquivo");
            }
        }

        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // retorna o caminho para salvar a imagem no banco de dados
        return uniqueImageName;
    }

    public ImageAndContentTypeDTO getImageAndContentType(@PathVariable Long userId, @PathVariable boolean isBackgroundImage)
            throws IOException, MalformedURLException {

        String imageUrlPath = baseImageUrlPath + "/profileImages";
        if (isBackgroundImage) {
            imageUrlPath = baseImageUrlPath + "/backgroundImages";
        }

        Path foulderPath = Path.of(imageUrlPath);

        File image = this.getById(userId, foulderPath.toFile())
                .orElseThrow(() -> new IOException("Imagem não encontrada"));

        Path filePath = Path.of(image.getPath()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() && !resource.isReadable()) {
            throw new IOException("Erro ao retornar imagem");
        }

        String contentType = "image/jpeg";
        if(filePath.endsWith(".png")) {
            contentType = "image/png";
        }

        return new ImageAndContentTypeDTO(resource, contentType);
    }

    private Optional<File> getById(Long id, File directory) throws IOException {
        File[] files = directory.listFiles();

        if (files == null) {
            return Optional.empty();
        }

        List<File> existentFile = Arrays.stream(files).filter(file -> {
            String fileId = file.getName().split("_")[0];

            return fileId.equals(id.toString());
        }).toList();

        return existentFile.stream().findFirst();
    }
}
