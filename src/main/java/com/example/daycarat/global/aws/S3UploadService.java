package com.example.daycarat.global.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

        amazonS3.putObject(bucket + "/" + path, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket + "/" + path, originalFilename).toString();
    }

    public void saveJsonFileContent(String path, String fileName, String jsonContent) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(jsonContent.getBytes(StandardCharsets.UTF_8).length);
        metadata.setContentType("application/json");

        amazonS3.putObject(bucket + "/content/" + path, fileName, new ByteArrayInputStream(jsonContent.getBytes(StandardCharsets.UTF_8)), metadata);
    }

}
