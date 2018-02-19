package br.com.videoproject.model.service.impl;

import br.com.videoproject.model.entity.Video;
import br.com.videoproject.model.repository.VideoRepository;
import br.com.videoproject.model.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "/home/break/Development/java";

    @Autowired
    private VideoRepository videoRepository;

    @Override
    public Video add(Video video) {
        byte[] bytes = video.getBytes();
        if (bytes.length == 0) {
            throw new RuntimeException("File is empty");
        }

        try {
            Path path = Paths.get(UPLOADED_FOLDER + video.getName());
            Files.write(path, bytes);

            video.setPath("path/to/video");
            return videoRepository.save(video);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading video");
        }
    }

    @Override
    public List<Video> list() {
        return videoRepository.findAll();
    }

    @Override
    public List<Video> findByName(String name) {
        if (name != null && !name.isEmpty()) {
            return videoRepository.findByName(name);
        }
        return videoRepository.findAll();
    }
}
