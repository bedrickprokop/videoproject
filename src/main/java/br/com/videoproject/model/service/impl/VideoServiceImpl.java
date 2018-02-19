package br.com.videoproject.model.service.impl;

import br.com.videoproject.model.entity.Video;
import br.com.videoproject.model.repository.VideoRepository;
import br.com.videoproject.model.service.VideoService;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {

    private Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private AmazonS3 s3client;

    @Value("${jsa.s3.bucket}")
    private String bucketName;

    @Override
    public Video add(Video video) {
        logger.info("method/add");

        //TODO validate uploaded file to only video type

        byte[] bytes = video.getBytes();
        if (bytes.length == 0) {
            logger.info("method/add - exception empty video file");
            throw new RuntimeException("Empty video file");
        }

        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(bytes.length);
            //objectMetadata.setContentType("video/mp4");

            s3client.putObject(new PutObjectRequest(bucketName, video.getName(),
                    byteArrayInputStream, objectMetadata));

            logger.info("method/add - upload video file to aws-s3 done");

            return videoRepository.save(video);

        } catch (AmazonServiceException e) {
            logger.error("method/add - exception with some reasons:");
            logger.error("message - " + e.getMessage());
            logger.error("status code - " + e.getStatusCode());
            logger.error("aws error code - " + e.getErrorCode());
            logger.error("error type - " + e.getErrorType());

            throw new RuntimeException("Error uploading video");
        } catch (AmazonClientException e) {
            logger.error("method/add - exception with some reasons:");
            logger.error("error message: " + e.getMessage());

            throw new RuntimeException("Error uploading video");
        }
    }

    @Override
    public List<Video> list() {
        logger.info("method/list");

        return videoRepository.findAll();
    }

    @Override
    public List<Video> findByName(String name) {
        logger.info("method/findByName");

        if (name != null && !name.isEmpty()) {
            return videoRepository.findByName(name);
        }
        return videoRepository.findAll();
    }
}
