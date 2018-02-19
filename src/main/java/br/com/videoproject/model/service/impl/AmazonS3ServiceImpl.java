package br.com.videoproject.model.service.impl;

import br.com.videoproject.model.entity.Video;
import br.com.videoproject.model.service.CloudStorageService;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class AmazonS3ServiceImpl implements CloudStorageService {

    private Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);

    @Autowired
    protected TransferManager transferManager;

    @Value("${jsa.s3.bucket}")
    private String bucketName;

    @Override
    public Video addOnCloud(Video video) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(video.getBytes());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(video.getBytes().length);
            objectMetadata.setContentType(video.getInputFormat());

            final PutObjectRequest request = new PutObjectRequest(bucketName, video.getName()
                    , byteArrayInputStream, objectMetadata);

            request.setGeneralProgressListener(new ProgressListener() {
                @Override
                public void progressChanged(ProgressEvent progressEvent) {
                    String transferredBytes = "Uploaded bytes: " + progressEvent.getBytesTransferred();
                    logger.info(transferredBytes);
                }
            });

            Upload upload = transferManager.upload(request);
            try {
                upload.waitForCompletion();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.info("method/add - upload video file to aws-s3 done");

            video.setInputUrl("https://s3-sa-east-1.amazonaws.com.s3.amazonaws.com/"
                    .concat(request.getBucketName().concat("/")).concat(video.getName()));

            return video;

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

}
