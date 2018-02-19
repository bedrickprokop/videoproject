package br.com.videoproject.model.service.impl;

import br.com.videoproject.model.entity.Video;
import br.com.videoproject.model.repository.VideoRepository;
import br.com.videoproject.model.service.CloudStorageService;
import br.com.videoproject.model.service.VideoEncodingService;
import br.com.videoproject.model.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private CloudStorageService cloudStorageService;

    @Autowired
    private VideoEncodingService videoEncodingService;

    @Override
    public Video add(Video video) {
        logger.info("method/add");

        byte[] bytes = video.getBytes();
        String contentType = video.getContentType();

        isVideoFileValid(bytes);
        isVideoTypeValid(contentType);

        cloudStorageService.addOnCloud(video);

        video = videoEncodingService.processVideo(video);

        return videoRepository.save(video);
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
