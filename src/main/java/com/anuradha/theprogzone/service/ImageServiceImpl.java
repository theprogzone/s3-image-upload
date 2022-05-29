package com.anuradha.theprogzone.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.anuradha.theprogzone.dto.ImageDTO;
import com.anuradha.theprogzone.exception.ConvertingMultiPartToFileException;
import com.anuradha.theprogzone.exception.ImageUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Value("${amazon.s3.bucket-name}")
    private String bucketName;
    @Value("${amazon.s3.access-key}")
    private String accessKey;
    @Value("${amazon.s3.secret-key}")
    private String secretKey;
    @Value("${amazon.s3.region}")
    private String region;

    private AmazonS3 amazonS3;

    @PostConstruct
    private void initialize() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(region).build();
    }

    @Override
    public void uploadImage(ImageDTO imageDTO) {
        try {
            File file = convertMultiPartToFile(imageDTO.getImage());
            String fileName = String.valueOf(System.currentTimeMillis()).concat("_").concat(imageDTO.getImage().getOriginalFilename());
            log.info("File name => {}", fileName);
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
            file.delete();
        } catch (Exception e) {
            log.error("Error image uploading => {}", e.getMessage());
            throw new ImageUploadException("Error in image uploading", "400");
        }
    }

    @Override
    public void deleteImage(String imageName) {
        log.info("imageName => {}", imageName);
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, imageName));
    }

    @Override
    public List<String> getImageUrls() {
        List<String> imageUrlList = new ArrayList<>();
        ObjectListing objectListing = amazonS3.listObjects(bucketName);
        for (S3ObjectSummary s3ObjectSummary : objectListing.getObjectSummaries()) {
            imageUrlList.add(amazonS3.getUrl(bucketName, null).toString().concat(s3ObjectSummary.getKey()));
        }
        log.info("imageUrlList => {}", imageUrlList);
        return imageUrlList;
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) {
        File file = null;
        try {
            log.info("Image name => {}", multipartFile.getOriginalFilename());
            file = new File(multipartFile.getOriginalFilename());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            log.error("Error in converting multipart file to file => {}", e.getMessage());
            throw new ConvertingMultiPartToFileException("Error in converting multipart file to file", "400");
        }
        return file;
    }
}
