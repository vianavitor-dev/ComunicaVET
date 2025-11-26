package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.image.ImageAndContentTypeDTO;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.model.User;
import com.projetointegrador.comunicavet.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private UserRepository userRepository;

    public String uploadImage(@NotNull boolean isBackgroundImage,
                              @NotNull MultipartFile imageFile,
                              @NotNull Long userId) throws IOException {

        String folder = isBackgroundImage ? "backgroundImages" : "profileImages";
        String uniqueImageName = userId + "_" + imageFile.getOriginalFilename();
        String key = folder + "/" + uniqueImageName;

        // remove imagem antiga, se existir
        deleteUserOldImage(folder, userId);

        // envia para S3
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(imageFile.getContentType())
                        .build(),
                RequestBody.fromBytes(imageFile.getBytes())
        );

        return uniqueImageName;
    }

    public ImageAndContentTypeDTO getImageAndContentType(
            @NotNull Optional<Long> optionalUserId,
            @NotNull Optional<String> optionalEmail,
            @NotNull boolean isBackgroundImage
    ) throws IOException, MalformedURLException, NotFoundResourceException {

        User user;

        // buscar usuário
        if (optionalEmail.isPresent()) {
            user = userRepository.findByEmail(optionalEmail.get())
                    .orElseThrow(() -> new NotFoundResourceException("Usuário não encontrado"));
        } else if (optionalUserId.isPresent()) {
            user = userRepository.findById(optionalUserId.get())
                    .orElseThrow(() -> new NotFoundResourceException("Usuário não encontrado"));
        } else {
            throw new NullPointerException("Nenhum campo foi preenchido");
        }

        String folder = isBackgroundImage ? "backgroundImages" : "profileImages";

        // encontra imagem no bucket
        String key = findUserFileInS3(folder, user.getId())
                .orElseThrow(() -> new NotFoundResourceException("Imagem não encontrada"));

        // baixa imagem do S3
        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()
        );

        String contentType = s3Object.response().contentType();

        return new ImageAndContentTypeDTO(
                new InputStreamResource(s3Object),
                contentType
        );
    }

    // ======================= AUXILIARES =============================

    private void deleteUserOldImage(String folder, Long userId) {

        ListObjectsV2Response response = s3Client.listObjectsV2(
                ListObjectsV2Request.builder()
                        .bucket(bucketName)
                        .prefix(folder + "/" + userId + "_")
                        .build()
        );

        for (S3Object obj : response.contents()) {
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(obj.key())
                            .build()
            );
        }
    }

    private Optional<String> findUserFileInS3(String folder, Long userId) {

        ListObjectsV2Response response = s3Client.listObjectsV2(
                ListObjectsV2Request.builder()
                        .bucket(bucketName)
                        .prefix(folder + "/" + userId + "_")
                        .build()
        );

        return response.contents().stream()
                .map(S3Object::key)
                .findFirst();
    }
}
