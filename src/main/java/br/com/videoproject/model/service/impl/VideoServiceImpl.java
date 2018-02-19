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

    private static final String[] VIDEO_FORMATS = {
            "video/3gpp2", "video/3gpp", ".3gp2", ".3gp", ".asf", "video/avi", ".avs", ".dv", ".f4v", ".flc", ".fli",
            "video/x-flv", ".gvi", ".m1v", "video/mpeg", ".m4e", ".m4u", "video/x-m4v", ".mjp", "video/x-matroska",
            ".moov", "video/quicktime", ".movie", "video/mp4", ".mpv2", ".rm", ".ts", ".vfw", ".vob", ".wm",
            "video/x-ms-wmv"};

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

        byte[] bytes = video.getBytes();
        String contentType = video.getContentType();

        isVideoFileValid(bytes);
        isVideoTypeValid(contentType);

        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(bytes.length);
            objectMetadata.setContentType(contentType);

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

    private void isVideoFileValid(byte[] bytes) {
        logger.info("method/isVideoFileValid");
        if (bytes.length == 0) {
            logger.info("method/isVideoFileValid - exception empty video file");
            throw new RuntimeException("Empty video file");
        }
    }

    private void isVideoTypeValid(String contentType) {
        logger.info("method/isVideoTypeValid");
        boolean isValid = false;
        for (String format : VIDEO_FORMATS) {
            if (contentType.equalsIgnoreCase(format)) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            logger.info("method/isVideoTypeValid - exception invalid video format");
            throw new RuntimeException("Invalid video format");
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
