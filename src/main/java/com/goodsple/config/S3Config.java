package com.goodsple.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${aws.s3.region}")
    private String region;

    String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
    String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");

    @Bean
    public AmazonS3 amazonS3() {
        System.out.println("Access Key: " + (accessKey != null ? accessKey.substring(0, 4) + "..." : "null"));
        System.out.println("Secret Key: " + (secretKey != null ? "***설정됨***" : "null"));

        if (accessKey != null && !accessKey.isEmpty()) {
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            return AmazonS3ClientBuilder.standard().withRegion(region).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        }
        return AmazonS3ClientBuilder.standard().withRegion(region).build();
    }
}
