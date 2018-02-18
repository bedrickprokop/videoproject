package br.com.videoproject.model.service.impl;

import br.com.videoproject.model.entity.Video;
import br.com.videoproject.model.repository.VideoRepository;
import br.com.videoproject.model.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Override
    public Video add(@Valid Video video) {
        return videoRepository.save(video);
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
