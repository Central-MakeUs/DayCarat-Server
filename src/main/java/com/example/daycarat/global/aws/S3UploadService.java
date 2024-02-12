package com.example.daycarat.global.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.daycarat.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static com.example.daycarat.global.error.exception.ErrorCode.AI_RECOMMENDATION_NOT_FOUND;

@Service @RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile, String path) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        String filename = LocalDateTime.now() + "_" + originalFilename;

        amazonS3.putObject(bucket + "/" + path, filename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket + "/" + path, filename).toString();
    }

    public void saveJsonFileContent(String path, String fileName, String jsonContent) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(jsonContent.getBytes(StandardCharsets.UTF_8).length);
        metadata.setContentType("application/json");

        amazonS3.putObject(bucket + "/content/" + path, fileName, new ByteArrayInputStream(jsonContent.getBytes(StandardCharsets.UTF_8)), metadata);
    }

    // retreive json file content
    public String getJsonFileContent(Long episodeId, String fileName) {
        try {
            return amazonS3.getObjectAsString(bucket + "/ai-generated/content/" + episodeId.toString(), fileName + ".json");
        }
        catch (Exception e) {
            throw new CustomException(AI_RECOMMENDATION_NOT_FOUND);
        }

    }

}
